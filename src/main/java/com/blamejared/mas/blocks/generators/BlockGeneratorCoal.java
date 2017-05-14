package com.blamejared.mas.blocks.generators;

import com.blamejared.mas.blocks.generators.base.BlockBaseGenerator;
import com.blamejared.mas.client.gui.base.IHasGui;
import com.blamejared.mas.client.gui.generatorcoal.*;
import com.blamejared.mas.tileentities.generators.TileEntityGeneratorCoal;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGeneratorCoal extends BlockBaseGenerator implements IHasGui {
    
    public BlockGeneratorCoal() {
        super(TileEntityGeneratorCoal.class);
    }
    
    @Override
    public Gui getClientGuiElement(int id, EntityPlayer player, World world, BlockPos blockPos) {
        return new GuiGeneratorCoal(player.inventory, (TileEntityGeneratorCoal) world.getTileEntity(blockPos));
    }

    @Override
    public Container getServerGuiElement(int id, EntityPlayer player, World world, BlockPos blockPos) {
        return new ContainerGeneratorCoal(player.inventory, (TileEntityGeneratorCoal) world.getTileEntity(blockPos));
    }
}
