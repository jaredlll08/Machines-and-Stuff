package com.blamejared.mas.client.gui.generatorTrash;

import com.blamejared.mas.api.Registry;
import com.blamejared.mas.client.gui.base.GuiBase;
import com.blamejared.mas.reference.Reference;
import com.blamejared.mas.tileentities.generators.TileEntityGeneratorTrash;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiGeneratorTrash extends GuiBase {
    
    private TileEntityGeneratorTrash tile;
    
    public GuiGeneratorTrash(InventoryPlayer invPlayer, TileEntityGeneratorTrash tile2) {
        super(new ContainerGeneratorTrash(invPlayer, tile2), tile2, invPlayer.player, "");
        this.tile = tile2;
        this.outlines = true;
    }
    
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/generator_trash.png");
    }
    
    @Override
    public boolean shouldOutline(ItemStack stack) {
        return Registry.TrashGenerator.canTrash(stack);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);
        
        this.drawGeneratorProgress(81, 36, tile);
        this.drawPowerBar(tile.container);
    }
}
