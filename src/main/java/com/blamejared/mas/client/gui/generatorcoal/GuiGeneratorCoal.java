package com.blamejared.mas.client.gui.generatorcoal;

import com.blamejared.mas.api.Registry;
import com.blamejared.mas.client.gui.base.GuiBase;
import com.blamejared.mas.reference.Reference;
import com.blamejared.mas.tileentities.generators.TileEntityGeneratorCoal;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiGeneratorCoal extends GuiBase {
    
    private TileEntityGeneratorCoal tile;
    
    public GuiGeneratorCoal(InventoryPlayer invPlayer, TileEntityGeneratorCoal tile2) {
        super(new ContainerGeneratorCoal(invPlayer, tile2), tile2, invPlayer.player, "tile.generator_coal.name");
        this.tile = tile2;
        setTitle(true);
        setOutlinePredicate(Registry.BasicCoalGenerator::containsItemStack);
    }
    
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/generator_coal.png");
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);
        
        this.drawGeneratorProgress(82, 40, tile);
        this.drawPowerBar(tile.container, mx, my);
        
    }
}
