package com.blamejared.mas.tileentities.generators;

import com.blamejared.mas.api.Registry;
import com.blamejared.mas.api.generators.GeneratorBase;
import net.minecraft.item.ItemStack;

public class TileEntityGeneratorTrash extends GeneratorBase {
    
    public TileEntityGeneratorTrash() {
        super(20000, 1);
    }
    
    @Override
    public int getEnergyGenerated() {
        return 10;
    }
    
    @Override
    public int getGenerationTime(ItemStack stack) {
        return 40;
    }
    
    public boolean canGenerateEnergy(ItemStack stack) {
        return this.container.getStoredPower() < this.container.getCapacity() && Registry.TrashGenerator.canTrash(stack);
    }
}
