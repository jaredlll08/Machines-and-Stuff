package com.blamejared.mas.tileentities.misc.energy;

import com.blamejared.mas.api.accumulators.EnumAccumulator;
import com.blamejared.mas.network.PacketHandler;
import com.blamejared.mas.network.messages.tiles.misc.MessageAccumulator;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.nbt.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.*;

public class TileEntityAccumulator extends TileEntity implements ITickable {
    
    public BaseTeslaContainer container;
    public EnumAccumulator enumAccumulator;
    
    private List<AccumulatorInfo> accumulatorInfos = new ArrayList<>();
    
    public TileEntityAccumulator() {
    }
    
    public TileEntityAccumulator(EnumAccumulator acc) {
        container = new BaseTeslaContainer(acc.getCapacity(), acc.getInput(), acc.getOutput());
        this.enumAccumulator = acc;
        for(EnumFacing facing : EnumFacing.values()) {
            accumulatorInfos.add(new AccumulatorInfo(acc, facing, AccumulatorInfo.AccumulatorIOInfo.INPUT));
        }
    }
    
    protected boolean pushEnergy() {
        boolean pushed = false;
        for(AccumulatorInfo info : accumulatorInfos) {
            if(info.getIoInfo() == AccumulatorInfo.AccumulatorIOInfo.OUTPUT) {
                EnumFacing dir = info.getFacing();
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
            }
        }
    }
    
    @Override
    public void markDirty() {
        super.markDirty();
        PacketHandler.INSTANCE.sendToAllAround(new MessageAccumulator(this), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double) this.getPos().getX(), (double) this.getPos().getY(), (double) this.getPos().getZ(), 128d));
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setTag("TeslaContainer", this.container.serializeNBT());
        NBTTagList list = new NBTTagList();
        for(AccumulatorInfo info : accumulatorInfos) {
            list.appendTag(info.writeToNBT());
        }
        nbt.setTag("AccumulatorInfos", list);
        return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.container = new BaseTeslaContainer(nbt.getCompoundTag("TeslaContainer"));
        NBTTagList list = nbt.getTagList("AccumulatorInfos", Constants.NBT.TAG_COMPOUND);
        this.accumulatorInfos.clear();
        for(int i = 0; i < list.tagCount(); i++) {
            this.accumulatorInfos.add(AccumulatorInfo.readFromNBT(list.getCompoundTagAt(i)));
        }
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
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == TeslaCapabilities.CAPABILITY_PRODUCER && getInfoForFace(facing).getIoInfo() == AccumulatorInfo.AccumulatorIOInfo.OUTPUT) {
            return (T) this.container;
        }
        if(capability == TeslaCapabilities.CAPABILITY_CONSUMER && getInfoForFace(facing).getIoInfo() == AccumulatorInfo.AccumulatorIOInfo.INPUT) {
            return (T) this.container;
        }
        if(capability == TeslaCapabilities.CAPABILITY_HOLDER && getInfoForFace(facing).getIoInfo() != AccumulatorInfo.AccumulatorIOInfo.DISABLED)
            return (T) this.container;
        return super.getCapability(capability, facing);
    }
    
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == TeslaCapabilities.CAPABILITY_PRODUCER && getInfoForFace(facing).getIoInfo() == AccumulatorInfo.AccumulatorIOInfo.OUTPUT) {
            return true;
        }
        if(capability == TeslaCapabilities.CAPABILITY_CONSUMER && getInfoForFace(facing).getIoInfo() == AccumulatorInfo.AccumulatorIOInfo.INPUT) {
            return true;
        }
        if(capability == TeslaCapabilities.CAPABILITY_HOLDER && getInfoForFace(facing).getIoInfo() != AccumulatorInfo.AccumulatorIOInfo.DISABLED)
            return true;
        return super.hasCapability(capability, facing);
    }
    
    public List<AccumulatorInfo> getAccumulatorInfos() {
        return accumulatorInfos;
    }
    
    public void setAccumulatorInfos(List<AccumulatorInfo> accumulatorInfos) {
        this.accumulatorInfos = accumulatorInfos;
    }
    
    public AccumulatorInfo getInfoForFace(EnumFacing face){
        AccumulatorInfo info = null;
        for(AccumulatorInfo accumulatorInfo : getAccumulatorInfos()) {
            if(accumulatorInfo.getFacing() == face){
                info = accumulatorInfo;
            }
        }
        return info;
    }
}
