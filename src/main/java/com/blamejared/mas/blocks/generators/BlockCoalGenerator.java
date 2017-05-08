package com.blamejared.mas.blocks.generators;

import com.blamejared.mas.blocks.generators.base.BlockBaseGenerator;
import com.blamejared.mas.client.gui.base.IHasGui;
import com.blamejared.mas.client.gui.coalGenerator.*;
import com.blamejared.mas.tileentities.generators.TileEntityCoalGenerator;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCoalGenerator extends BlockBaseGenerator implements IHasGui {


    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityCoalGenerator();
    }

    @Override
    public Gui getClientGuiElement(int id, EntityPlayer player, World world, BlockPos blockPos) {
        return new GUICoalGenerator(player.inventory, (TileEntityCoalGenerator) world.getTileEntity(blockPos));
    }

    @Override
    public Container getServerGuiElement(int id, EntityPlayer player, World world, BlockPos blockPos) {
        return new ContainerCoalGenerator(player.inventory, (TileEntityCoalGenerator) world.getTileEntity(blockPos));
    }
}
