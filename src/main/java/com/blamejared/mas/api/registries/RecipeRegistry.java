package com.blamejared.mas.api.registries;

import com.blamejared.mas.api.recipes.machines.RecipeMachineBase;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * Created by Jared on 5/13/2016.
 */
public class RecipeRegistry {
    
    private static HashMap<String, RecipeMachineBase> crusherRecipes = new HashMap<String, RecipeMachineBase>();
    private static HashMap<String, RecipeMachineBase> furnaceRecipes = new HashMap<String, RecipeMachineBase>();
    private static HashMap<String, RecipeMachineBase> sawmillRecipes = new HashMap<String, RecipeMachineBase>();
    
    
    public static void registerCrusherRecipe(String seedID, RecipeMachineBase recipe) {
        if(!crusherRecipes.containsKey(seedID)) {
            crusherRecipes.put(seedID, recipe);
        }
    }
    
    public static void registerCrusherRecipe(RecipeMachineBase recipe) {
        String id = String.format("%s;%s:%s;%s", recipe.getInput().getUnlocalizedName(), recipe.getInput().getItemDamage(), recipe.getOutput().getUnlocalizedName(), recipe.getOutput().getItemDamage());
        if(!crusherRecipes.containsKey(id)) {
            crusherRecipes.put(id, recipe);
        }
    }
    
    public static RecipeMachineBase getCrusherRecipeByID(String seedID) {
        if(crusherRecipes.containsKey(seedID)) {
            return (RecipeMachineBase) crusherRecipes.get(seedID);
        }
        return null;
    }
    
    public static HashMap<String, RecipeMachineBase> getAllCrusherRecipes() {
        return crusherRecipes;
    }
    
    public static boolean isCrusherInput(ItemStack stack) {
        for(Map.Entry<String, RecipeMachineBase> ent : getAllCrusherRecipes().entrySet()) {
            if(ent.getValue().getInput().isItemEqual(stack)) {
                return true;
            }
        }
        return false;
    }
    
    public static void registerFurnaceRecipe(String seedID, RecipeMachineBase recipe) {
        if(!furnaceRecipes.containsKey(seedID)) {
            furnaceRecipes.put(seedID, recipe);
        }
    }
    
    public static void registerFurnaceRecipe(RecipeMachineBase recipe) {
        String id = String.format("%s;%s:%s;%s", recipe.getInput().getUnlocalizedName(), recipe.getInput().getItemDamage(), recipe.getOutput().getUnlocalizedName(), recipe.getOutput().getItemDamage());
        if(!furnaceRecipes.containsKey(id)) {
            furnaceRecipes.put(id, recipe);
        }
    }
    
    public static RecipeMachineBase getFurnaceRecipeByID(String seedID) {
        if(furnaceRecipes.containsKey(seedID)) {
            return (RecipeMachineBase) furnaceRecipes.get(seedID);
        }
        return null;
    }
    
    public static HashMap<String, RecipeMachineBase> getAllFurnaceRecipes() {
        return furnaceRecipes;
    }
    
    public static boolean isFurnaceInput(ItemStack stack) {
        if(stack != null && stack.getItem() != null) {
            for(Map.Entry<String, RecipeMachineBase> ent : getAllFurnaceRecipes().entrySet()) {
                if(compareStacks(ent.getValue().getInput(), stack))
                    return true;
            }
        }
        
        return false;
    }
    
    
    public static void registerSawmillRecipe(String seedID, RecipeMachineBase recipe) {
        if(!sawmillRecipes.containsKey(seedID)) {
            sawmillRecipes.put(seedID, recipe);
        }
    }
    
    public static void registerSawmillRecipe(RecipeMachineBase recipe) {
        String id = String.format("%s;%s:%s;%s", recipe.getInput().getUnlocalizedName(), recipe.getInput().getItemDamage(), recipe.getOutput().getUnlocalizedName(), recipe.getOutput().getItemDamage());
        if(!sawmillRecipes.containsKey(id)) {
            sawmillRecipes.put(id, recipe);
        }
    }
    
    public static RecipeMachineBase getSawmillRecipeByID(String seedID) {
        if(sawmillRecipes.containsKey(seedID)) {
            return (RecipeMachineBase) sawmillRecipes.get(seedID);
        }
        return null;
    }
    
    public static HashMap<String, RecipeMachineBase> getAllSawmillRecipes() {
        return sawmillRecipes;
    }
    
    public static boolean isSawmillInput(ItemStack stack) {
        for(Map.Entry<String, RecipeMachineBase> ent : getAllSawmillRecipes().entrySet()) {
            if(ent.getValue().getInput().isItemEqual(stack)) {
                return true;
            }
        }
        return false;
    }
    
    
    public static boolean compareStacks(ItemStack input, ItemStack output) {
        return output.getItem() == input.getItem() && (input.getMetadata() == 32767 || output.getMetadata() == input.getMetadata());
    }
    
}
