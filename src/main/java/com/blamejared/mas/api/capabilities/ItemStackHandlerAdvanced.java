package com.blamejared.mas.api.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.items.*;

import java.util.*;

/**
 * Created by Jared on 7/10/2016.
 */
public class ItemStackHandlerAdvanced implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {
    
    private List<ItemStack> stacks;
    
    public ItemStackHandlerAdvanced() {
        stacks = new ArrayList<>();
    }
    
    
    public void addStack(ItemStack stack) {
        this.stacks.add(stack);
    }
    
    @Override
    @Deprecated
    public void setStackInSlot(int slot, ItemStack stack) {
        validateSlotIndex(slot);
        if(ItemStack.areItemStacksEqual(this.stacks.get(slot), stack))
            return;
        this.stacks.set(slot, stack);
        onContentsChanged(slot);
    }
    
    @Override
    public int getSlots() {
        return stacks.size();
    }
    
    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if(stack == null || stack.getCount() == 0)
            return null;
        
        validateSlotIndex(slot);
        
        ItemStack existing = this.stacks.get(slot);
        
        int limit = getStackLimit(slot, stack);
        
        if(existing != null) {
            if(!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;
            
            limit -= existing.getCount();
        }
        
        if(limit <= 0)
            return stack;
        
        boolean reachedLimit = stack.getCount() > limit;
        
        if(!simulate) {
            if(existing == null) {
                this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
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
        
        ItemStack existing = this.stacks.get(slot);
        
        if(existing == null)
            return null;
        
        int toExtract = Math.min(amount, existing.getMaxStackSize());
        
        if(existing.getCount() <= toExtract) {
            if(!simulate) {
                this.stacks.set(slot, null);
                onContentsChanged(slot);
            }
            return existing;
        } else {
            if(!simulate) {
                this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
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
        for(int i = 0; i < stacks.size(); i++) {
            if(stacks.get(i) != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                stacks.get(i).writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", stacks.size());
        return nbt;
    }
    
    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.stacks = new ArrayList<>();
        for(int i = 0; i < (nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : stacks.size()); i++) {
            stacks.add(null);
        }
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");
            if(slot >= 0 && slot < this.stacks.size()) {
                stacks.set(slot, new ItemStack(itemTags));
            }
        }
        onLoad();
    }
    
    protected void validateSlotIndex(int slot) {
        if(slot < 0 || slot >= stacks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }
    
    protected void onLoad() {
        
    }
    
    protected void onContentsChanged(int slot) {
        
    }
    
    public List<ItemStack> getStacks() {
        return stacks;
    }
    
    
    public void setStacks(List<ItemStack> stacks) {
        this.stacks = stacks;
    }
}
