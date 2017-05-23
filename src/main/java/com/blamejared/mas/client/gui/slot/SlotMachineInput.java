package com.blamejared.mas.client.gui.slot;

import com.blamejared.mas.tileentities.machine.TileEntityMachineBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class SlotMachineInput extends SlotItemHandler {
    
    private TileEntityMachineBase tile;
    
    public SlotMachineInput(TileEntityMachineBase tile, int index, int xPosition, int yPosition) {
        super(tile.itemStackHandler, index, xPosition, yPosition);
        this.tile = tile;
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        return super.isItemValid(stack);
    }
}
