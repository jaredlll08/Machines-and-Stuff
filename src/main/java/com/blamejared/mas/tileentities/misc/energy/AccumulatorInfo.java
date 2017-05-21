package com.blamejared.mas.tileentities.misc.energy;

import com.blamejared.mas.api.accumulators.EnumAccumulator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class AccumulatorInfo {
    
    private final EnumAccumulator type;
    private final EnumFacing facing;
    private AccumulatorIOInfo ioInfo;
    
    public AccumulatorInfo(EnumAccumulator type, EnumFacing facing, AccumulatorIOInfo ioInfo) {
        this.type = type;
        this.facing = facing;
        this.ioInfo = ioInfo;
    }
    
    public EnumAccumulator getType() {
        return type;
    }
    
    public EnumFacing getFacing() {
        return facing;
    }
    
    public AccumulatorIOInfo getIoInfo() {
        return ioInfo;
    }
    
    public void setIoInfo(AccumulatorIOInfo ioInfo) {
        this.ioInfo = ioInfo;
    }
    
    public static AccumulatorInfo readFromNBT(NBTTagCompound tag) {
        return new AccumulatorInfo(EnumAccumulator.values()[tag.getInteger("type")], EnumFacing.values()[tag.getInteger("facing")], AccumulatorIOInfo.valueOf(tag.getString("ioInfo")));
    }
    
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("type", type.ordinal());
        tag.setInteger("facing", facing.ordinal());
        tag.setString("ioInfo", ioInfo.name());
        return tag;
    }
    
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccumulatorInfo{");
        sb.append("type=").append(type);
        sb.append(", facing=").append(facing);
        sb.append(", ioInfo=").append(ioInfo);
        sb.append('}');
        return sb.toString();
    }
    
    public enum AccumulatorIOInfo {
        INPUT, OUTPUT, DISABLED;
    
        public AccumulatorIOInfo getNext() {
            return values()[(ordinal() + 1) % values().length];
        }
        
    }
}
