package com.blamejared.mas.tileentities.misc;

import com.blamejared.mas.network.PacketHandler;
import com.blamejared.mas.network.messages.tiles.misc.MessageCrank;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class TileEntityCrank extends TileEntity implements ITickable {
    
    public BaseTeslaContainer container;
    public int generationTimer = -1;
    public int generationTimerDefault = -1;
    
    private float angle = 0;
    
    public TileEntityCrank() {
        container = new BaseTeslaContainer(5, 5, 5);
    }
    
    public boolean isGenerating() {
        return generationTimer > -1;
    }
    
    
    protected boolean pushEnergy() {
        boolean pushed = false;
        TileEntity tile = world.getTileEntity(getPos().offset(EnumFacing.DOWN));
        if(tile != null)
            if(tile.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, EnumFacing.UP) || tile.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.UP)) {
                BaseTeslaContainer cont = (BaseTeslaContainer) tile.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, EnumFacing.UP);
                container.takePower(cont.givePower(container.takePower(container.getOutputRate(), true), false), false);
                if(!world.isRemote) {
                    tile.markDirty();
                    markDirty();
                    pushed = true;
                }
            }
        return pushed;
    }
    
    @Override
    public void update() {
        boolean sendUpdate = false;
        //            PacketHandler.INSTANCE.sendToAllAround(new MessageGenerator(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
        //Sets the generation timer
        //        if(generationTimerDefault < 0 && this.container.getStoredPower() < this.container.getCapacity()) {
        //            if(itemStackHandler.getStackInSlot(0) != null) {
        //                if(canGenerateEnergy(itemStackHandler.getStackInSlot(0))) {
        //                    generationTimer = getGenerationTime(itemStackHandler.getStackInSlot(0));
        //                    generationTimerDefault = getGenerationTime(itemStackHandler.getStackInSlot(0));
        //                    itemStackHandler.extractItem(0, 1, false);
        //                    if(!world.isRemote)
        //                        sendUpdate = true;
        //
        //                }
        //            }
        //        }
        if(generationTimer < 0) {
            generationTimerDefault = -1;
            generationTimer = -1;
            if(!world.isRemote)
                sendUpdate = true;
        }
        if(container.getStoredPower() > 0)
            if(pushEnergy()) {
                sendUpdate = true;
            }
        if(generationTimerDefault > 0 && this.container.getStoredPower() < this.container.getCapacity()) {
            generationTimer--;
            this.container.givePower(5, false);
            if(!world.isRemote)
                sendUpdate = true;
        }
        
        if(!world.isRemote) {
            if(sendUpdate) {
                this.markDirty();
                PacketHandler.INSTANCE.sendToAllAround(new MessageCrank(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
                this.world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
            }
        }
        
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("generationTimer", generationTimer);
        nbt.setInteger("generationTimerDefault", generationTimerDefault);
        nbt.setTag("TeslaContainer", this.container.serializeNBT());
        return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
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
        
        return super.getCapability(capability, facing);
    }
    
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == TeslaCapabilities.CAPABILITY_PRODUCER)
            return true;
        return super.hasCapability(capability, facing);
    }
    
}
