package com.blamejared.mas.tileentities.machine;

import com.blamejared.mas.api.recipes.machines.RecipeMachineBase;
import com.blamejared.mas.api.registries.RecipeRegistry;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

/**
 * Created by Jared on 5/25/2016.
 */
public class TileEntityMachineFurnace extends TileEntityMachineBase<RecipeMachineBase> {
    
    public TileEntityMachineFurnace() {
        super(32000, 2);
    }
    
    @Override
    public int getEnergyUsed() {
        return 250;
    }
    
    @Override
    public RecipeMachineBase getRecipe(String index) {
        return RecipeRegistry.getFurnaceRecipeByID(index);
    }
    
    @Override
    public HashMap<String, RecipeMachineBase> getRecipes() {
        return RecipeRegistry.getAllFurnaceRecipes();
    }
    
    @Override
    public boolean isValidInput(ItemStack stack) {
        return RecipeRegistry.isFurnaceInput(stack);
    }
    
}
