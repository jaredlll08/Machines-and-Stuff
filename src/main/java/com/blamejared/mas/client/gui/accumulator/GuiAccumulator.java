package com.blamejared.mas.client.gui.accumulator;

import com.blamejared.mas.api.Registry;
import com.blamejared.mas.client.gui.base.GuiBase;
import com.blamejared.mas.reference.Reference;
import com.blamejared.mas.tileentities.misc.energy.TileEntityAccumulator;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class GuiAccumulator extends GuiBase {
    
    private TileEntityAccumulator tile;
    
    public GuiAccumulator(InventoryPlayer invPlayer, TileEntityAccumulator tile2) {
        super(new ContainerAccumulator(invPlayer, tile2), tile2, invPlayer.player, tile2.getBlockType().getLocalizedName());
        this.tile = tile2;
        setTitle(true);
        setOutlinePredicate(Registry.BasicCoalGenerator::containsItemStack);
    }
    
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/accumulator.png");
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);
        this.drawPowerBar(tile.container, mx, my);
    }
}
