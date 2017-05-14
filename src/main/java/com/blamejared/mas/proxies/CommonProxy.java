package com.blamejared.mas.proxies;

import com.blamejared.mas.events.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    
    public void registerRenderersPre() {
    
    }
    
    public void registerRenderers() {
    
    }
    
    public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
    }
    
    public void registerGuis() {
    }
    
    public EntityPlayer getClientPlayer() {
        return null;
    }
}
