package com.blamejared.mas.data;

import com.blamejared.mas.blocks.MBlocks;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.*;

public class RecipeData {
    
    public static void init() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MBlocks.GENERATOR_TRASH), "ifi", "fsf", "ifi", 'f', new ItemStack(Blocks.FURNACE), 's', "cobblestone", 'i', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MBlocks.GENERATOR_COAL), "ifi", "fcf", "ifi", 'f', new ItemStack(Blocks.FURNACE), 'c', new ItemStack(Items.COAL), 'i', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MBlocks.CRANK), " ww", "twt", "sss", 'w', "plankWood", 's', "stone", 't', new ItemStack(Blocks.STONE_SLAB)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MBlocks.STONE_REINFORCED), "sis", "isi", "sis", 'i', "ingotIron", 's', "stone"));
    }
    
}
