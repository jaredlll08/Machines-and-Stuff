package com.blamejared.mas.network.messages.tiles.misc;

import com.blamejared.mas.tileentities.misc.TileEntityCrank;
import io.netty.buffer.ByteBuf;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageCrank implements IMessage, IMessageHandler<MessageCrank, IMessage> {
    
    private int x;
    private int y;
    private int z;
    private int generationTimer;
    private int generationTimerDefault;
    private long energy;
    
    public MessageCrank() {
    
    }
    
    public MessageCrank(TileEntity tile) {
        this((TileEntityCrank) tile);
    }
    
    public MessageCrank(TileEntityCrank tile) {
        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
        this.generationTimer = tile.generationTimer;
        this.generationTimerDefault = tile.generationTimerDefault;
        this.energy = tile.container.getStoredPower();
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        
        this.generationTimer = buf.readInt();
        this.generationTimerDefault = buf.readInt();
        this.energy = buf.readLong();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        
        buf.writeInt(generationTimer);
        buf.writeInt(generationTimerDefault);
        buf.writeLong(energy);
    }
    
    @Override
    public IMessage onMessage(MessageCrank message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
        return null;
        
    }
    
    private void handle(MessageCrank message, MessageContext ctx) {
        if(FMLClientHandler.instance().getClient().world != null) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if(tileEntity instanceof TileEntityCrank) {
                ((TileEntityCrank) tileEntity).generationTimer = message.generationTimer;
                ((TileEntityCrank) tileEntity).generationTimerDefault = message.generationTimerDefault;
                long cap = ((TileEntityCrank) tileEntity).container.getCapacity();
                long input = ((TileEntityCrank) tileEntity).container.getInputRate();
                long output = ((TileEntityCrank) tileEntity).container.getOutputRate();
                ((TileEntityCrank) tileEntity).container = new BaseTeslaContainer(message.energy, cap, input, output);
                
            }
        }
    }
    
}
