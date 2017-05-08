package com.blamejared.mas.client.gui.coalGenerator;

import com.blamejared.mas.client.gui.base.ContainerBase;
import com.blamejared.mas.client.gui.slot.SlotTileDep;
import com.blamejared.mas.tileentities.generators.TileEntityCoalGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerCoalGenerator extends ContainerBase {
    
    public ContainerCoalGenerator(InventoryPlayer invPlayer, TileEntityCoalGenerator tile) {
        addSlotToContainer(new SlotTileDep(tile.itemStackHandler, 0, 80, 11));
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
