package com.blamejared.mas.proxies;

import com.blamejared.mas.blocks.MBlocks;
import com.blamejared.mas.client.gui.GuiHandler;
import com.blamejared.mas.items.MItems;
import com.blamejared.mas.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

import java.util.Map;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void registerRenderersPre() {
        super.registerRenderersPre();
    }
    
    @Override
    public void registerRenderers() {
        super.registerRenderers();
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        for(Map.Entry<String, Item> ent : MItems.renderMap.entrySet()) {
            renderItem.getItemModelMesher().register(ent.getValue(), 0, new ModelResourceLocation(Reference.MODID + ":" + ent.getKey(), "inventory"));
        }
        
        for(Map.Entry<String, Block> ent : MBlocks.renderMap.entrySet()) {
            renderItem.getItemModelMesher().register(Item.getItemFromBlock(ent.getValue()), 0, new ModelResourceLocation(Reference.MODID + ":" + ent.getKey(), "inventory"));
        }
    }
    
    @Override
    public void registerEvents() {
        super.registerEvents();
    }
    
    @Override
    public void registerGuis() {
        super.registerGuis();
        new GuiHandler();
    }
    
    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }
    
}
