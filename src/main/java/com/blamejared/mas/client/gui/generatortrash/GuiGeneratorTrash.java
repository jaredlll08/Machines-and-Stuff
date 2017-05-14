package com.blamejared.mas.client.gui.generatortrash;

import com.blamejared.mas.api.Registry;
import com.blamejared.mas.client.gui.base.GuiBase;
import com.blamejared.mas.reference.Reference;
import com.blamejared.mas.tileentities.generators.TileEntityGeneratorTrash;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiGeneratorTrash extends GuiBase {
    
    private TileEntityGeneratorTrash tile;
    
    public GuiGeneratorTrash(InventoryPlayer invPlayer, TileEntityGeneratorTrash tile2) {
        super(new ContainerGeneratorTrash(invPlayer, tile2), tile2, invPlayer.player, "tile.generator_trash.name");
        this.tile = tile2;
        setTitle(true);
        setOutlinePredicate(Registry.TrashGenerator::canTrash);
    }
    
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/generator_trash.png");
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);
        this.drawGeneratorProgress(83, 40, tile);
        this.drawPowerBar(tile.container, mx, my);
    }
}
