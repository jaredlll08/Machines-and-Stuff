package com.blamejared.mas.client.gui.crusher;

import com.blamejared.mas.api.registries.RecipeRegistry;
import com.blamejared.mas.client.gui.base.GuiBase;
import com.blamejared.mas.reference.Reference;
import com.blamejared.mas.tileentities.machine.TileEntityMachineCrusher;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUICrusher extends GuiBase {
    
    private TileEntityMachineCrusher tile;
    
    public GUICrusher(InventoryPlayer invPlayer, TileEntityMachineCrusher tile2) {
        super(new ContainerCrusher(invPlayer, tile2), tile2, invPlayer.player, "tile.machine_crusher.name");
        this.tile = tile2;
        setTitle(true);
        setOutlinePredicate(itemStack -> RecipeRegistry.isCrusherInput(itemStack));
    }
    
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/machinecrusher.png");
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);
        drawMachineProgress(tile.itemCycleTime, tile.needCycleTime);
        drawPowerBar(tile.container, mx, my);
    }
    
}
