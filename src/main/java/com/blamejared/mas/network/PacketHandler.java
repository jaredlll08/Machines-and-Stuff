package com.blamejared.mas.network;


import com.blamejared.mas.network.messages.tiles.generator.MessageGenerator;
import com.blamejared.mas.network.messages.tiles.machines.MessageMachineBase;
import com.blamejared.mas.network.messages.tiles.misc.*;
import com.blamejared.mas.reference.Reference;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    
    public static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Reference.MODID);
    public static int ID = 0;
    
    public static void preInit() {
        INSTANCE.registerMessage(MessageGenerator.class, MessageGenerator.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageCrank.class, MessageCrank.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageAccumulator.class, MessageAccumulator.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageMachineBase.class, MessageMachineBase.class, ID++, Side.CLIENT);
    }
    
}
