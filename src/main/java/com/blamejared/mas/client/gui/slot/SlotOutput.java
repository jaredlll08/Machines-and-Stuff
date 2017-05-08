package com.blamejared.mas.client.gui.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.*;

/**
 * Created by Jared on 8/13/2016.
 */
public class SlotOutput extends SlotItemHandler {
    
    public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }
}
