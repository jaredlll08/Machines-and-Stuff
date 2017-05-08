package com.blamejared.mas;

import com.blamejared.mas.api.Registry;
import com.blamejared.mas.blocks.MBlocks;
import com.blamejared.mas.data.*;
import com.blamejared.mas.items.MItems;
import com.blamejared.mas.network.PacketHandler;
import com.blamejared.mas.proxies.CommonProxy;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;

import static com.blamejared.mas.reference.Reference.*;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class MAS {
    
    @Mod.Instance(MODID)
    public static MAS INSTANCE;
    
    @SidedProxy(clientSide = "com.blamejared.mas.proxies.ClientProxy", serverSide = "com.blamejared.mas.proxies.CommonProxy")
    public static CommonProxy PROXY;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        PROXY.registerRenderersPre();
        MBlocks.preInit();
        MItems.preInit();
        PROXY.registerEvents();
        PacketHandler.preInit();
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        PROXY.registerRenderers();
        PROXY.registerGuis();
        GeneratorData.init();
        RecipeData.init();
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    
    }
    
}
