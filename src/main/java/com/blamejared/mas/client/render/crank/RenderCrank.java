package com.blamejared.mas.client.render.crank;

import com.blamejared.mas.reference.Reference;
import com.blamejared.mas.tileentities.misc.TileEntityCrank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCrank extends TileEntitySpecialRenderer<TileEntityCrank> {
    
    ModelCrank model = new ModelCrank();
    private Minecraft mc = Minecraft.getMinecraft();
    
    public RenderCrank() {
    }
    
    private ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/models/crank.png");
    
    @Override
    public void renderTileEntityAt(TileEntityCrank te, double x, double y, double z, float partialTicks, int destroyStage) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.50f, (float) z + 0.5f);
        GL11.glRotatef(180f, 1f, 0f, 0f);
        GL11.glScaled(1, 1, 1);
        
        mc.renderEngine.bindTexture(texture);
        model.render(0.0625f);
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.50f, (float) z + 0.5f);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
    
}
