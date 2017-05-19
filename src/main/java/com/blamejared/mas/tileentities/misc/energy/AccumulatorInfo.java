package com.blamejared.mas.tileentities.misc.energy;

import com.blamejared.mas.api.accumulators.EnumAccumulator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class AccumulatorInfo {
    
    private final EnumAccumulator type;
    private final EnumFacing facing;
    private boolean enabled;
    
    public AccumulatorInfo(EnumAccumulator type, EnumFacing facing, boolean enabled) {
        this.type = type;
        this.facing = facing;
        this.enabled = enabled;
    }
    
    public EnumAccumulator getType() {
        return type;
    }
    
    public EnumFacing getFacing() {
        return facing;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public static AccumulatorInfo readFromNBT(NBTTagCompound tag) {
        return new AccumulatorInfo(EnumAccumulator.values()[tag.getInteger("type")], EnumFacing.values()[tag.getInteger("facing")], tag.getBoolean("enabled"));
    }
    
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("type", type.ordinal());
        tag.setInteger("facing", facing.ordinal());
        tag.setBoolean("enabled", enabled);
        return tag;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccumulatorInfo{");
        sb.append("type=").append(type);
        sb.append(", facing=").append(facing);
        sb.append(", enabled=").append(enabled);
        sb.append('}');
        return sb.toString();
    }
}
