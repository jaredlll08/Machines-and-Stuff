package com.blamejared.mas.client.gui.base;

import com.blamejared.mas.api.generators.GeneratorBase;
import com.blamejared.mas.tileentities.machine.TileEntityMachineBase;
import com.blamejared.mas.util.RenderUtils;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.function.Predicate;

public class GuiBase extends GuiContainer {
    
    Container container;
    TileEntity tile;
    //Not used yet
    //ResourceLocation overlays;
    EntityPlayer player;
    String name;
    
    //Should show title of machine
    private boolean title;
    //Should outline items
    private Predicate<ItemStack> shouldOutline = itemStack -> false;
    
    public GuiBase(Container container, TileEntity tile, EntityPlayer player, String name) {
        super(container);
        this.container = container;
        this.tile = tile;
        this.name = I18n.translateToLocal(name);
        this.player = player;
    }
    
    @Override
    public void initGui() {
        this.xSize = 176;
        this.ySize = 166;
        super.initGui();
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my) {
        if(title)
            this.fontRendererObj.drawString(this.name, this.xSize / 2 - this.fontRendererObj.getStringWidth(this.name) / 2, 6, 0xa0a0a0);
        
        GlStateManager.pushAttrib();
        GlStateManager.color(1, 1, 1, 1);
        
        Minecraft.getMinecraft().renderEngine.bindTexture(getTexture());
        GlStateManager.popAttrib();
    
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && getShouldOutline() != null)
            drawOutlines();
    }
    
    protected void drawOutlines() {
        GL11.glPushMatrix();
        GlStateManager.pushAttrib();
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        for(int x = 0; x < 9; x++) {
            ItemStack stack = player.inventory.getStackInSlot(x);
            if(stack != ItemStack.EMPTY) {
                if(getShouldOutline().test(stack)) {
                    RenderUtils.drawRectNoFade(8 + 18 * x, 142, 280, 16, 16, 0f, 0.8f, 0, 2f, 1);
                } else
                    RenderUtils.drawRectNoFade(8 + 18 * x, 142, 280, 16, 16, 0.8f, 0, 0, 2f, 1);
            }
        }
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                ItemStack stack = player.inventory.getStackInSlot(x + y * 9 + 9);
                if(stack != ItemStack.EMPTY) {
                    if(getShouldOutline().test(stack)) {
                        RenderUtils.drawRectNoFade(8 + 18 * x, 84 + (y * 18), 280, 16, 16, 0f, 0.8f, 0f, 2f, 1);
                    } else
                        RenderUtils.drawRectNoFade(8 + 18 * x, 84 + (y * 18), 280, 16, 16, 0.8f, 0, 0, 2f, 1);
                }
            }
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GlStateManager.popAttrib();
        GL11.glPopMatrix();
    }
    
    //Override this
    public ResourceLocation getTexture() {
        return null;
    }
    
    //Progress of machine recipe
    public void drawMachineProgress(TileEntityMachineBase tile) {
        drawMachineProgress(tile.itemCycleTime, tile.needCycleTime);
    }
    
    //Progress of machine recipe
    public void drawMachineProgress(int itemCycleTime, int needCycleTime) {
        int progressWidth = (int) (((float) itemCycleTime / needCycleTime) * 33);
        drawTexturedModalRect(72, 36, 6, 168, progressWidth, 13);
    }
    
    //How full is power bar
    public void drawPowerBar(BaseTeslaContainer container) {
        GlStateManager.pushAttrib();
        int barWidth = (int) (((float) container.getStoredPower() / container.getCapacity()) * 88);
        drawTexturedModalRect(44, 60, 44, 166, barWidth, 19);
        GlStateManager.popAttrib();
    }
    
    //Generator progress
    public void drawGeneratorProgress(int x, int y, GeneratorBase tile) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int barHeight = (int) (((float) tile.generationTimer / tile.generationTimerDefault) * 16);
        drawTexturedModalRect(x, y + barHeight, 176, barHeight, 16, barHeight + 16);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.pushAttrib();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(getTexture());
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        GlStateManager.popAttrib();
    }
    
    
    public void setOutlinePredicate(Predicate<ItemStack> predicate) {
        this.shouldOutline = predicate;
    }
    
    public Predicate<ItemStack> getShouldOutline() {
        return shouldOutline;
    }
    
    public boolean isTitle() {
        return title;
    }
    
    public void setTitle(boolean title) {
        this.title = title;
    }
}
