package com.blamejared.mas.tileentities.machine;

import com.blamejared.mas.api.recipes.machines.RecipeMachineBase;
import com.blamejared.mas.api.registries.RecipeRegistry;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

/**
 * Created by Jared on 5/25/2016.
 */
public class TileEntityMachineCrusher extends TileEntityMachineBase<RecipeMachineBase> {
    
    public TileEntityMachineCrusher() {
        super(32000, 2, 250, 250);
    }
    
    @Override
    public int getEnergyUsed() {
        return 250;
    }
    
    @Override
    public RecipeMachineBase getRecipe(String index) {
        return RecipeRegistry.getCrusherRecipeByID(index);
    }
    
    @Override
    public HashMap<String, RecipeMachineBase> getRecipes() {
        return RecipeRegistry.getAllCrusherRecipes();
    }
    
    @Override
    public boolean isValidInput(ItemStack stack) {
        return RecipeRegistry.isCrusherInput(stack);
    }
    
}
