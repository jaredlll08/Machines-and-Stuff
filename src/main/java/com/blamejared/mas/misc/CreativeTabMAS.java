package com.blamejared.mas.misc;

import com.blamejared.mas.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CreativeTabMAS extends CreativeTabs {
    
    public CreativeTabMAS() {
        super(Reference.MODID);
    }
    
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Blocks.FURNACE);
    }
}
