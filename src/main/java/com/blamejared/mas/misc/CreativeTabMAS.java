package com.blamejared.mas.misc;

import com.blamejared.mas.items.MItems;
import com.blamejared.mas.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabMAS extends CreativeTabs {
    
    public CreativeTabMAS() {
        super(Reference.MODID);
    }
    
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(MItems.WRENCH);
    }
}
