package com.blamejared.mas.tileentities.generators;

import com.blamejared.mas.api.Registry;
import com.blamejared.mas.api.generators.GeneratorBase;
import net.minecraft.item.ItemStack;

public class TileEntityCoalGenerator extends GeneratorBase {
    
    public TileEntityCoalGenerator() {
        super(50000, 1);
    }
    
    @Override
    public int getEnergyGenerated() {
        return 80;
    }
    
    @Override
    public int getGenerationTime(ItemStack stack) {
        return Registry.BasicCoalGenerator.getBurnTime(stack);
    }
    
    public boolean canGenerateEnergy(ItemStack stack) {
        return this.container.getStoredPower() < this.container.getCapacity() && Registry.BasicCoalGenerator.containsItemStack(stack);
    }
}
