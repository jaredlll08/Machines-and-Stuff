package com.blamejared.mas.events;

import com.blamejared.mas.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

public class ClientEvents {
    
    
    @SubscribeEvent
    public void renderGUI(RenderGameOverlayEvent.Text e) {
        if(Reference.DEVENV) {
            if((e.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) || (e.getType() == RenderGameOverlayEvent.ElementType.TEXT)) {
        
                Minecraft.getMinecraft().fontRendererObj.drawString("FPS: " + Minecraft.getDebugFPS(), 50, 50, 0xFF55FF);
                if(FMLCommonHandler.instance().getMinecraftServerInstance() != null)
                    Minecraft.getMinecraft().fontRendererObj.drawString(getTPS(FMLCommonHandler.instance().getMinecraftServerInstance(), Minecraft.getMinecraft().player), 50, 70, 0xFF55FF);
        
            }
        }
    }
    
    private String getTPS(MinecraftServer server, EntityPlayerSP player) {
        int dim = player.world.provider.getDimension();
        boolean summary = true;
        if (summary) {
            for (Integer dimId : DimensionManager.getIDs()) {
                double worldTickTime = mean(server.worldTickTimes.get(dimId)) * 1.0E-6D;
                double worldTPS = Math.min(1000.0 / worldTickTime, 20);
            }
            double meanTickTime = mean(server.tickTimeArray) * 1.0E-6D;
            double meanTPS = Math.min(1000.0 / meanTickTime, 20);
            return "MSPT: " + timeFormatter.format(meanTickTime) + " : TPS: " + timeFormatter.format(meanTPS);
        }
        return "";
    }
    
    private static final DecimalFormat timeFormatter = new DecimalFormat("########0.000");
    
    private static long mean(long[] values) {
        long sum = 0;
        for (long v : values) {
            sum += v;
        }
        
        return sum / values.length;
    }
}
