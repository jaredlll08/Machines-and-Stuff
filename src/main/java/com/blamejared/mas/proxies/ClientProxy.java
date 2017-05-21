package com.blamejared.mas.proxies;

import com.blamejared.mas.blocks.MBlocks;
import com.blamejared.mas.client.gui.GuiHandler;
import com.blamejared.mas.client.render.accumulator.RenderAccumulator;
import com.blamejared.mas.client.render.crank.RenderCrank;
import com.blamejared.mas.events.ClientEvents;
import com.blamejared.mas.items.MItems;
import com.blamejared.mas.reference.Reference;
import com.blamejared.mas.tileentities.misc.TileEntityCrank;
import com.blamejared.mas.tileentities.misc.energy.TileEntityAccumulator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.awt.*;
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
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrank.class, new RenderCrank());
        ClientRegistry.registerTileEntity(TileEntityCrank.class, "crank", new RenderCrank());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAccumulator.class, new RenderAccumulator());
    
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
            TileEntityAccumulator tile = (TileEntityAccumulator) worldIn.getTileEntity(pos);
            if(tile != null && tile.container != null) {
                long energy = tile.container.getStoredPower();
                long cap = tile.container.getCapacity();
                if(tintIndex >= 0 && tintIndex < 6) {
                    switch(tile.getInfoForFace(EnumFacing.VALUES[tintIndex]).getIoInfo()) {
                        case DISABLED:
                            return 0x3D3C3A;
                        case INPUT:
                            return 0x4863A0;
                        case OUTPUT:
                            return 0xFF8040;
                    }
                }
                if(tintIndex == 6) {
                    if(cap != 0 && energy != 0) {
                        Color col;
                
                        switch(tile.enumAccumulator) {
                            case REINFROCED_STONE:
                                col = new Color(energy / (cap + 0.0f), 0, 0, 1);
                                break;
                            case IRON:
                                col = new Color(0,energy / (cap + 0.0f), 0, 1);
                                break;
                            case STEEL:
                                col = new Color(energy / (cap + 0.0f), energy / (cap + 0.0f), 0, 1);
                                break;
                            case FLUXED:
                                col = new Color(0, 0.8f, energy / (cap + 0.0f), 1);
                                break;
                            default:
                                col = new Color(0);
                                break;
                        }
                        return col.getRGB();
                
                    }
                    return 0;
                }
            }
            return 0;
        }, MBlocks.ACCUMULATOR_STONE_REINFORCED, MBlocks.ACCUMULATOR_IRON);
    }
    
    @Override
    public void registerEvents() {
        super.registerEvents();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
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
