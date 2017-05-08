package com.blamejared.mas.blocks.generators;

import com.blamejared.mas.blocks.generators.base.BlockBaseGenerator;
import com.blamejared.mas.client.gui.base.IHasGui;
import com.blamejared.mas.client.gui.generatorTrash.*;
import com.blamejared.mas.tileentities.generators.TileEntityGeneratorTrash;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGeneratorTrash extends BlockBaseGenerator implements IHasGui {
    
    public BlockGeneratorTrash() {
        super(TileEntityGeneratorTrash.class);
    }
    
    @Override
    public Gui getClientGuiElement(int id, EntityPlayer player, World world, BlockPos blockPos) {
        return new GuiGeneratorTrash(player.inventory, (TileEntityGeneratorTrash) world.getTileEntity(blockPos));
    }
    
    @Override
    public Container getServerGuiElement(int id, EntityPlayer player, World world, BlockPos blockPos) {
        return new ContainerGeneratorTrash(player.inventory, (TileEntityGeneratorTrash) world.getTileEntity(blockPos));
    }
}
