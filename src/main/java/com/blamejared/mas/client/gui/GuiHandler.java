package com.blamejared.mas.client.gui;

import com.blamejared.mas.MAS;
import com.blamejared.mas.client.gui.base.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.*;

/**
 * Created by Jared.
 */
public class GuiHandler implements IGuiHandler {
    
    public GuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(MAS.INSTANCE, this);
    }
    
    
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        IHasGui openableGUI = this.getHasGUI(id, player, world, blockPos);
        return openableGUI != null ? openableGUI.getServerGuiElement(id, player, world, blockPos) : null;
    }
    
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, final int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        IHasGui openableGUI = this.getHasGUI(id, player, world, blockPos);
        return openableGUI != null ? openableGUI.getClientGuiElement(id, player, world, blockPos) : null;
    }
    
    private IHasGui getHasGUI(int id, EntityPlayer player, World world, BlockPos blockPos) {
        return GuiCarrier.values()[id].getHasGUI(player, world, blockPos);
    }
}
