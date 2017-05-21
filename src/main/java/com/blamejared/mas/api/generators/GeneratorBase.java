package com.blamejared.mas.api.generators;

import com.blamejared.mas.blocks.generators.base.BlockBaseGenerator;
import com.blamejared.mas.network.PacketHandler;
import com.blamejared.mas.network.messages.tiles.generator.MessageGenerator;
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
import net.minecraftforge.items.*;

import javax.annotation.Nullable;

public abstract class GeneratorBase extends TileEntity implements ITickable {
    
    public ItemStackHandler itemStackHandler;
    public int generationTimer = -1;
    public int generationTimerDefault = -1;
    
    public BaseTeslaContainer container;
    
    
    public GeneratorBase(int cap, int inventorySize) {
        container = new BaseTeslaContainer(cap, 50, 50);
        itemStackHandler = new ItemStackHandler(inventorySize);
    }
    
    public boolean isGenerating() {
        return generationTimer > -1 && generationTimerDefault > -1;
    }
    
    
    protected boolean pushEnergy() {
        boolean pushed = false;
        for(EnumFacing dir : EnumFacing.VALUES) {
            TileEntity tile = world.getTileEntity(getPos().offset(dir));
            if(tile != null)
                if(tile.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, dir.getOpposite())) {
                    BaseTeslaContainer cont = (BaseTeslaContainer) tile.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, dir.getOpposite());
                    container.takePower(cont.givePower(container.takePower(container.getOutputRate(), true), false), false);
                    if(!world.isRemote) {
                        tile.markDirty();
                        markDirty();
                        pushed = true;
                    }
                }
        }
        return pushed;
    }
    
    @Override
    public void update() {
        boolean sendUpdate = false;
        //            PacketHandler.INSTANCE.sendToAllAround(new MessageGenerator(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
        
        if(generationTimerDefault < 0 && this.container.getStoredPower() < this.container.getCapacity()) {
            if(itemStackHandler.getStackInSlot(0) != ItemStack.EMPTY) {
                if(canGenerateEnergy(itemStackHandler.getStackInSlot(0))) {
                    generationTimer = getGenerationTime(itemStackHandler.getStackInSlot(0));
                    generationTimerDefault = getGenerationTime(itemStackHandler.getStackInSlot(0));
                    itemStackHandler.extractItem(0, 1, false);
                    if(!world.isRemote)
                        sendUpdate = true;
                    
                }
            }
        }
        
        
        if(container.getStoredPower() > 0)
            if(pushEnergy()) {
                sendUpdate = true;
            }
        if(generationTimerDefault > 0 && this.container.getStoredPower() < this.container.getCapacity()) {
            generationTimer--;
            this.container.givePower(getEnergyGenerated(), false);
            if(!world.isRemote)
                sendUpdate = true;
        }
        if(generationTimer < 0 && generationTimerDefault > 0) {
            if(itemStackHandler.getStackInSlot(0) != ItemStack.EMPTY && canGenerateEnergy(itemStackHandler.getStackInSlot(0))) {
                generationTimer = getGenerationTime(itemStackHandler.getStackInSlot(0));
                generationTimerDefault = getGenerationTime(itemStackHandler.getStackInSlot(0));
                itemStackHandler.extractItem(0, 1, false);
            } else {
                generationTimerDefault = -1;
                generationTimer = -1;
            }
            if(!world.isRemote)
                sendUpdate = true;
        }
        if(!world.isRemote) {
            if(sendUpdate) {
                this.markDirty();
//                PacketHandler.INSTANCE.sendToAllAround(new MessageGenerator(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
                this.world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
            }
        }
        boolean active = true;
        if(!isGenerating()) {
            active = false;
        }
        if(itemStackHandler.getStackInSlot(0) == ItemStack.EMPTY) {
            active = false;
        }
        if(this.container.getStoredPower() == this.container.getCapacity()) {
            active = false;
        }
        
        if(world.getBlockState(getPos()).getValue(BlockBaseGenerator.isActive) != active) {
            BlockBaseGenerator.setState(active, this.world, this.pos);
        }
    }
    
    @Override
    public void markDirty() {
        super.markDirty();
        PacketHandler.INSTANCE.sendToAllAround(new MessageGenerator(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
    }
    
    public abstract boolean canGenerateEnergy(ItemStack stack);
    
    public abstract int getGenerationTime(ItemStack stack);
    
    public abstract int getEnergyGenerated();
    
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setTag("items", itemStackHandler.serializeNBT());
        nbt.setInteger("generationTimer", generationTimer);
        nbt.setInteger("generationTimerDefault", generationTimerDefault);
        nbt.setTag("TeslaContainer", this.container.serializeNBT());
        return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.itemStackHandler.deserializeNBT(nbt.getCompoundTag("items"));
        generationTimer = nbt.getInteger("generationTimer");
        generationTimerDefault = nbt.getInteger("generationTimerDefault");
        this.container = new BaseTeslaContainer(nbt.getCompoundTag("TeslaContainer"));
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(getPos(), 0, tag);
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        
        if(capability == TeslaCapabilities.CAPABILITY_PRODUCER)
            return (T) this.container;
        else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.itemStackHandler;
        }
        
        return super.getCapability(capability, facing);
    }
    
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == TeslaCapabilities.CAPABILITY_PRODUCER)
            return true;
        else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }
    
}
