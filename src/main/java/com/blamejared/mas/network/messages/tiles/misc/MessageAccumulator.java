package com.blamejared.mas.network.messages.tiles.misc;

import com.blamejared.mas.tileentities.misc.energy.TileEntityAccumulator;
import io.netty.buffer.ByteBuf;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageAccumulator implements IMessage, IMessageHandler<MessageAccumulator, IMessage> {
    
    private int x;
    private int y;
    private int z;
    private long energy;
    
    public MessageAccumulator() {
    
    }
    
    public MessageAccumulator(TileEntity tile) {
        this((TileEntityAccumulator) tile);
    }
    
    public MessageAccumulator(TileEntityAccumulator tile) {
        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
        this.energy = tile.container.getStoredPower();
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.energy = buf.readLong();
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        
        buf.writeLong(energy);
    }
    
    @Override
    public IMessage onMessage(MessageAccumulator message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
        return null;
        
    }
    
    private void handle(MessageAccumulator message, MessageContext ctx) {
        if(FMLClientHandler.instance().getClient().world != null) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
            if(tileEntity instanceof TileEntityAccumulator) {
                long cap = ((TileEntityAccumulator) tileEntity).container.getCapacity();
                long input = ((TileEntityAccumulator) tileEntity).container.getInputRate();
                long output = ((TileEntityAccumulator) tileEntity).container.getOutputRate();
                ((TileEntityAccumulator) tileEntity).container = new BaseTeslaContainer(message.energy, cap, input, output);
                tileEntity.getWorld().markBlockRangeForRenderUpdate(tileEntity.getPos().north().east(), tileEntity.getPos().south().west());
            }
        }
    }
    
}
