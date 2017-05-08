package com.blamejared.mas.client.gui.coalGenerator;

import com.blamejared.mas.api.Registry;
import com.blamejared.mas.client.gui.base.GuiBase;
import com.blamejared.mas.reference.Reference;
import com.blamejared.mas.tileentities.generators.TileEntityCoalGenerator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.function.Predicate;

public class GUICoalGenerator extends GuiBase {
    
    private TileEntityCoalGenerator tile;
    
    public GUICoalGenerator(InventoryPlayer invPlayer, TileEntityCoalGenerator tile2) {
        super(new ContainerCoalGenerator(invPlayer, tile2), tile2, invPlayer.player, "");
        this.tile = tile2;
        this.outlines = true;
    }
    
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Reference.MODID, "textures/gui/coal_generator.png");
    }
    
    @Override
    public boolean shouldOutline(ItemStack stack) {
        return Registry.BasicCoalGenerator.containsItemStack(stack);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        super.drawGuiContainerForegroundLayer(mx, my);
        
        this.drawGeneratorProgress(82, 38, tile);
        this.drawPowerBar(tile.container);
    }
}
