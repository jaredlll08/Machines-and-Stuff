package com.blamejared.mas.data;

import com.blamejared.mas.api.Registry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;
import net.minecraftforge.oredict.OreDictionary;


public class GeneratorData {
    
    public static void init() {
        registerBasicCoalGeneratorItems();
        registerTrashGeneratorItems();
    }
    
    
    private static void registerTrashGeneratorItems() {
    }
    
    private static void registerBasicCoalGeneratorItems() {
        //		Registry.BasicCoalGenerator.basicCoalGenerator.add(new MutablePair<ItemStack, Integer>(new ItemStack(Items.coal), 600));
        Registry.BasicCoalGenerator.addBasicCoalGeneratorItem(new ItemStack(Items.COAL), 600);
        Registry.BasicCoalGenerator.addBasicCoalGeneratorItem(new ItemStack(Items.COAL, 1, 1), 600);
        for(ItemStack stack : OreDictionary.getOres("blockCoal")) {
            Registry.BasicCoalGenerator.addBasicCoalGeneratorItem(stack, 5400);
        }
    }
}
