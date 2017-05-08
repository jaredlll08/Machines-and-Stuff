package com.blamejared.mas.api.capabilities;

import com.blamejared.mas.tileentities.machine.TileEntityMachineBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.items.*;

/**
 * Created by Jared on 7/16/2016.
 */
public class ItemStackHandlerMachine implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {
    
    public TileEntityMachineBase tile;
    protected ItemStack[] stacks;
    
    public ItemStackHandlerMachine(TileEntityMachineBase tile, int size) {
        this.tile = tile;
        stacks = new ItemStack[size];
    }
    
    public ItemStackHandlerMachine(TileEntityMachineBase tile, ItemStack[] stacks) {
        this.tile = tile;
        this.stacks = stacks;
    }
    
    public void setSize(int size) {
        stacks = new ItemStack[size];
    }
    
    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        validateSlotIndex(slot);
        stacks[slot] = stack;
        if(stack != null && stack.getCount() > 64) {
            stack.setCount(64);
        }
        onContentsChanged(slot);
    }
    
    @Override
    public int getSlots() {
        return stacks.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.stacks[slot];
    }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if(stack == null || stack.getCount() == 0)
            return null;
        
        validateSlotIndex(slot);
        
        ItemStack existing = this.stacks[slot];
        
        int limit = getStackLimit(slot, stack);
        
        if(existing != null) {
            if(!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;
            
            limit -= existing.getCount();
        }
        
        if(slot == 0 && !tile.isValidInput(stack)) {
            return stack;
        }
        if(limit <= 0)
            return stack;
        
        boolean reachedLimit = stack.getCount() > limit;
        
        if(!simulate) {
            if(existing == null) {
                this.stacks[slot] = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onContentsChanged(slot);
        }
        
        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : null;
    }
    
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(amount == 0)
            return null;
        
        validateSlotIndex(slot);
        
        ItemStack existing = this.stacks[slot];
        
        if(existing == null)
            return null;
        
        int toExtract = Math.min(amount, existing.getMaxStackSize());
        
        if(existing.getCount() <= toExtract) {
            if(!simulate) {
                this.stacks[slot] = null;
                onContentsChanged(slot);
            }
            return existing;
        } else {
            if(!simulate) {
                this.stacks[slot] = ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract);
                onContentsChanged(slot);
            }
            
            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }
    
    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }
    
    protected int getStackLimit(int slot, ItemStack stack) {
        return stack.getMaxStackSize();
    }
    
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for(int i = 0; i < stacks.length; i++) {
            if(stacks[i] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                stacks[i].writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", stacks.length);
        return nbt;
    }
    
    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        setSize(nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : stacks.length);
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");
            
            if(slot >= 0 && slot < stacks.length) {
                stacks[slot] = new ItemStack(itemTags);
            }
        }
        onLoad();
    }
    
    protected void validateSlotIndex(int slot) {
        if(slot < 0 || slot >= stacks.length)
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.length + ")");
    }
    
    protected void onLoad() {
        
    }
    
    protected void onContentsChanged(int slot) {
        if(slot == 0) {
            tile.updateCurrentRecipe();
        }
        if(!tile.getWorld().isRemote)
            tile.markDirty();
    }
}
