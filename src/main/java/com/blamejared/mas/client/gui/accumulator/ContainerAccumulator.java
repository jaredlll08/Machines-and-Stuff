package com.blamejared.mas.client.gui.accumulator;

import com.blamejared.mas.client.gui.base.ContainerBase;
import com.blamejared.mas.tileentities.misc.energy.TileEntityAccumulator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerAccumulator extends ContainerBase {
    
    public ContainerAccumulator(InventoryPlayer invPlayer, TileEntityAccumulator tile) {
        for(int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(invPlayer, x, 8 + 18 * x, 142));
        }
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 8 + 18 * x, 84 + y * 18));
            }
        }
        
    }
}
