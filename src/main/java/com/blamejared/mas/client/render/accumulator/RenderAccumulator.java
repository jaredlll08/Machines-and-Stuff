package com.blamejared.mas.client.render.accumulator;

import com.blamejared.mas.tileentities.misc.energy.TileEntityAccumulator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

public class RenderAccumulator extends TileEntitySpecialRenderer<TileEntityAccumulator> {
    
    
    @Override
    public void renderTileEntityAt(TileEntityAccumulator te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GL11.glTranslated(x, y, z);
        GlStateManager.disableLighting();
        GlStateManager.disableLighting();
        renderBlockModel(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
        GlStateManager.enableLighting();
        GL11.glTranslated(-x, -y, -z);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
    
    public static void renderBlockModel(World world, BlockPos pos, IBlockState state) {
        VertexBuffer wr = Tessellator.getInstance().getBuffer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        wr.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockModelShapes modelShapes = blockrendererdispatcher.getBlockModelShapes();
        IBakedModel ibakedmodel = modelShapes.getModelForState(state);
        final IBlockAccess worldWrapper = world;
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        
        for(BlockRenderLayer layer : BlockRenderLayer.values()) {
            if(state.getBlock().canRenderInLayer(state, layer)) {
                ForgeHooksClient.setRenderLayer(layer);
                blockrendererdispatcher.getBlockModelRenderer().renderModel(worldWrapper, ibakedmodel, state, pos, wr, true);
            }
        }
        ForgeHooksClient.setRenderLayer(null);
        wr.setTranslation(0, 0, 0);
        Tessellator.getInstance().draw();
    }
}
