package com.blamejared.mas.tileentities.machine;

import com.blamejared.mas.api.capabilities.ItemStackHandlerMachine;
import com.blamejared.mas.api.recipes.machines.RecipeMachineBase;
import com.blamejared.mas.blocks.machines.BlockMachine;
import com.blamejared.mas.network.PacketHandler;
import com.blamejared.mas.network.messages.tiles.machines.MessageMachineBase;
import com.blamejared.mas.util.NBTHelper;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Created by Jared on 5/31/2016.
 */
public abstract class TileEntityMachineBase<E extends RecipeMachineBase> extends TileEntity implements ITickable {
    
    public ItemStackHandlerMachine itemStackHandler;
    public int itemCycleTime; // How long the current cycle has been running
    public int deviceCycleTime; // How long the machine will cycle
    public byte state;
    public int needCycleTime; // Based on everything how long should this
    public BaseTeslaContainer container;
    // take?????
    private int currentTime = 0;
    private int totalTime = 0;
    // empty if not currently working on any valid recipe
    private String recipeIndex = "";
    private long prevEnergy;
    
    public TileEntityMachineBase() {
    }
    
    public TileEntityMachineBase(long energyCap, int invSize, long inputCap, long outputCap) {
        container = new BaseTeslaContainer(energyCap, inputCap, outputCap);
        itemStackHandler = new ItemStackHandlerMachine(this, invSize);
    }
    
    public int getTotalTime() {
        return totalTime;
    }
    
    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
    
    public String getRecipeIndex() {
        return recipeIndex;
    }
    
    public void setRecipeIndex(String recipeIndex) {
        this.recipeIndex = recipeIndex;
    }
    
    public int getCurrentTime() {
        return currentTime;
    }
    
    public abstract int getEnergyUsed();
    
    public abstract E getRecipe(String index);
    
    public abstract HashMap<String, E> getRecipes();
    
    public abstract boolean isValidInput(ItemStack stack);
    
    public void update() {
        boolean canWork = false;
        boolean sendUpdate = false;
        // If we are still working then cycle the counter down 1
        if(this.deviceCycleTime > 0 && container.getStoredPower()>getEnergyUsed()) {
            this.deviceCycleTime--;
            if(!world.getBlockState(getPos()).getValue(BlockMachine.isActive)) {
                ((BlockMachine) world.getBlockState(getPos()).getBlock()).setState(true, this.world, getPos());
            }
        } else {
            if(world.getBlockState(getPos()).getValue(BlockMachine.isActive)) {
                ((BlockMachine) world.getBlockState(getPos()).getBlock()).setState(false, this.world, getPos());
            }
        }
        if(!this.world.isRemote) {
            if(prevEnergy != this.container.getStoredPower()) {
                prevEnergy = this.container.getStoredPower();
                sendUpdate = true;
            }
            E recipe = null;
            if(!itemStackHandler.getStackInSlot(0).isEmpty() && !getRecipeIndex().isEmpty() && this.container.getStoredPower() > 0) {
                recipe = getRecipe(getRecipeIndex());
                if(recipe != null) {
                    if(this.container.getStoredPower() >= getEnergyUsed()) {
                        if(!itemStackHandler.getStackInSlot(1).isEmpty()) {
                            if(NBTHelper.isInputEqual(recipe.getOutput(), itemStackHandler.getStackInSlot(1))) {
                                if(itemStackHandler.getStackInSlot(1).getCount() + recipe.getOutputAmount() < itemStackHandler.getStackInSlot(1).getMaxStackSize()) {
                                    if(itemStackHandler.getStackInSlot(0).getCount() >= recipe.getInputamount()) {
                                        canWork = true;
                                    }
                                }
                            }
                        } else {
                            if(itemStackHandler.getStackInSlot(0).getCount() >= recipe.getInputamount()) {
                                canWork = true;
                            }
                        }
                        
                    }
                    
                    
                }
                
            }
            
            // Can we run a new item
            if(this.deviceCycleTime == 0 && canWork) {
                
                this.deviceCycleTime = 40;
                this.needCycleTime = 40;
                sendUpdate = true;
                
            }
            // Keep working if there is something in progress
            
            if(this.deviceCycleTime > 0 && canWork) {
                
                this.itemCycleTime++;
                if(this.itemCycleTime == 40) {
                    
                    this.itemCycleTime = 0;
                    process();
                    this.container.takePower(getEnergyUsed(), false);
                    sendUpdate = true;
                    
                }
                
            } else {
                
                this.itemCycleTime = 0;
                
            }
            
            // Last check
            if(this.deviceCycleTime > 0) {
                sendUpdate = true;
                
            }
        }
        
        
        if(sendUpdate) {
            this.markDirty();
            this.state = this.deviceCycleTime > 0 ? (byte) 1 : (byte) 0;
            getWorld().addBlockEvent(getPos(), this.getBlockType(), 1, this.state);
            PacketHandler.INSTANCE.sendToAllAround(new MessageMachineBase(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
            this.world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
            
        }
        
    }
    
    @Override
    public void markDirty() {
        super.markDirty();
        PacketHandler.INSTANCE.sendToAllAround(new MessageMachineBase(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
        
    }
    
    
    /* NBT */
    @Override
    public void readFromNBT(NBTTagCompound tags) {
        super.readFromNBT(tags);
        itemStackHandler.deserializeNBT(tags.getCompoundTag("items"));
        currentTime = tags.getInteger("currentTime");
        String index = tags.getString("recipeIndex");
        if(index.equals("___null")) {
            index = "";
        }
        setRecipeIndex(index);
        deviceCycleTime = tags.getInteger("deviceCycleTime");
        itemCycleTime = tags.getInteger("itemCycleTime");
        needCycleTime = tags.getInteger("needCycleTime");
        updateCurrentRecipe();
    }
    
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags) {
        super.writeToNBT(tags);
        tags.setTag("items", itemStackHandler.serializeNBT());
        tags.setInteger("currentTime", currentTime);
        String index = getRecipeIndex();
        if(index == null || index.isEmpty()) {
            index = "___null";
        }
        tags.setString("recipeIndex", index);
        
        tags.setInteger("deviceCycleTime", deviceCycleTime);
        tags.setInteger("itemCycleTime", itemCycleTime);
        tags.setInteger("needCycleTime", needCycleTime);
        return tags;
    }
    
    
    public boolean process() {
        if(!getRecipeIndex().isEmpty()) {
            E recipe = getRecipe(getRecipeIndex());
            if(recipe != null && !itemStackHandler.getStackInSlot(0).isEmpty() && recipe.matchesExact(itemStackHandler.getStackInSlot(0))) {
                if(itemStackHandler.getStackInSlot(1).isEmpty() || NBTHelper.isInputEqual(itemStackHandler.getStackInSlot(1), recipe.getOutput())) {
                    itemStackHandler.extractItem(0, 1, false);
                    currentTime++;
                    if(currentTime >= recipe.getInputamount()) {
                        ItemStack out = recipe.getOutput().copy();
                        out.setCount(recipe.getOutputAmount());
                        itemStackHandler.insertItem(1, out, false);
                        
                        currentTime = 0;
                    }
                }
                return true;
            }
        }
        currentTime = 0;
        return false;
    }
    
    
    public void updateCurrentRecipe() {
        String oldIndex = getRecipeIndex();
        setRecipeIndex("");
        ItemStack inputStack = itemStackHandler.getStackInSlot(0);
        if(!inputStack.isEmpty() && inputStack.getCount() > 0) {
            for(String id : getRecipes().keySet()) {
                E recipe = getRecipe(id);
                if(recipe != null && recipe.matchesExact(inputStack)) {
                    setRecipeIndex(id);
                    break;
                }
            }
        }
        if(!oldIndex.equals(getRecipeIndex())){
            currentTime = 0;
            itemCycleTime = 0;
            deviceCycleTime = 40;
            needCycleTime = 40;
        }
        
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }
    
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        
        if(capability == TeslaCapabilities.CAPABILITY_CONSUMER)
            return (T) this.container;
        else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.itemStackHandler;
        }
        
        return super.getCapability(capability, facing);
    }
    
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == TeslaCapabilities.CAPABILITY_CONSUMER)
            return true;
        else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }
    
}
