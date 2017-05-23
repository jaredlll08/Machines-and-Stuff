package com.blamejared.mas.data;

import com.blamejared.mas.api.recipes.machines.RecipeMachineBase;
import com.blamejared.mas.api.registries.RecipeRegistry;
import com.blamejared.mas.blocks.MBlocks;
import com.blamejared.mas.items.MItems;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.*;

import java.util.Map;

public class RecipeData {
    
    public static void init() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MBlocks.GENERATOR_TRASH), "ifi", "fsf", "ifi", 'f', new ItemStack(Blocks.FURNACE), 's', new ItemStack(MBlocks.STONE_REINFORCED), 'i', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MBlocks.GENERATOR_COAL), "ifi", "fcf", "ifi", 'f', new ItemStack(Blocks.FURNACE), 'c', new ItemStack(Items.COAL), 'i', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MBlocks.CRANK), " ww", "twt", "sss", 'w', "plankWood", 's', new ItemStack(MBlocks.STONE_REINFORCED), 't', new ItemStack(Blocks.STONE_SLAB)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MBlocks.STONE_REINFORCED), "sis", "isi", "sis", 'i', "ingotIron", 's', "stone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MBlocks.ACCUMULATOR_STONE_REINFORCED), "sis", "iri", "sis", 'i', "ingotIron", 's', new ItemStack(MBlocks.STONE_REINFORCED), 'r', "blockRedstone"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MBlocks.ACCUMULATOR_IRON), "sis", "iri", "sis", 'i', "blockIron", 'r', new ItemStack(MBlocks.ACCUMULATOR_STONE_REINFORCED), 's', new ItemStack(MBlocks.STONE_REINFORCED)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MItems.WRENCH), "s s", "sss", " s ", 's', new ItemStack(MBlocks.STONE_REINFORCED)));
    
        registerFurnaceRecipes();
    }
    
    private static void registerFurnaceRecipes() {
        for(Map.Entry<ItemStack, ItemStack> ent : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
            RecipeRegistry.registerFurnaceRecipe(new RecipeMachineBase(ent.getKey().copy(), ent.getValue().copy(), ent.getKey().getCount(), ent.getValue().getCount()));
        }
    }
    
    private static void registerCrusherRecipes(){
        //TODO add recipes
    }
    
}
