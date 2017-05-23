package com.blamejared.mas.client.gui.furnace;

import com.blamejared.mas.api.registries.RecipeRegistry;
import com.blamejared.mas.client.gui.base.GuiBase;
import com.blamejared.mas.reference.Reference;
import com.blamejared.mas.tileentities.machine.TileEntityMachineFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIFurnace extends GuiBase {
    
    private TileEntityMachineFurnace tile;
    
    public GUIFurnace(InventoryPlayer invPlayer, TileEntityMachineFurnace tile2) {
        super(new ContainerFurnace(invPlayer, tile2), tile2, invPlayer.player, "tile.machine_furnace.name");
        this.tile = tile2;
        setTitle(true);
        setOutlinePredicate(itemStack -> RecipeRegistry.isFurnaceInput(itemStack));
    }
    
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/machinefurnace.png");
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);
        drawMachineProgress(tile.itemCycleTime, tile.needCycleTime);
        drawPowerBar(tile.container, mx, my);
    }
    
}
