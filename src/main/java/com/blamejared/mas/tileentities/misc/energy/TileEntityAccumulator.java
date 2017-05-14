package com.blamejared.mas.tileentities.misc.energy;

import com.blamejared.mas.api.accumulators.EnumAccumulator;
import com.blamejared.mas.network.PacketHandler;
import com.blamejared.mas.network.messages.tiles.misc.MessageAccumulator;
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

public class TileEntityAccumulator extends TileEntity implements ITickable {
    
    public BaseTeslaContainer container;
    public EnumAccumulator enumAccumulator;
    
    public TileEntityAccumulator() {
    }
    
    public TileEntityAccumulator(EnumAccumulator acc) {
        container = new BaseTeslaContainer(acc.getCapacity(), acc.getInput(), acc.getOutput());
        this.enumAccumulator = acc;
    }
    
    protected boolean pushEnergy() {
        boolean pushed = false;
        for(EnumFacing dir : EnumFacing.VALUES) {
            TileEntity tile = world.getTileEntity(getPos().offset(dir));
            if(tile != null)
                if(tile.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, dir.getOpposite()) || tile.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, dir.getOpposite())) {
                    BaseTeslaContainer cont = (BaseTeslaContainer) tile.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, dir.getOpposite());
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
        if(container.getStoredPower() > 0)
            if(pushEnergy()) {
                sendUpdate = true;
            }
        if(!world.isRemote) {
            if(sendUpdate) {
                this.markDirty();
                this.world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
            }
        }
    }
    
    @Override
    public void markDirty() {
        super.markDirty();
        PacketHandler.INSTANCE.sendToAllAround(new MessageAccumulator(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
        this.world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setTag("TeslaContainer", this.container.serializeNBT());
        return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
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
        if(capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return (T) this.container;
        
        return super.getCapability(capability, facing);
    }
    
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return true;
        return super.hasCapability(capability, facing);
    }
}