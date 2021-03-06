package com.blamejared.mas.blocks.machines;

import com.blamejared.mas.MAS;
import com.blamejared.mas.client.gui.base.*;
import com.blamejared.mas.client.gui.furnace.*;
import com.blamejared.mas.tileentities.machine.TileEntityMachineFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluxfurnace extends BlockMachine implements IHasGui {
    
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMachineFurnace();
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            playerIn.openGui(MAS.INSTANCE, GuiCarrier.BLOCK.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
    
    @Override
    public Gui getClientGuiElement(int id, EntityPlayer player, World world, BlockPos blockPos) {
        return new GUIFurnace(player.inventory, (TileEntityMachineFurnace) world.getTileEntity(blockPos));
    }
    
    @Override
    public Container getServerGuiElement(int id, EntityPlayer player, World world, BlockPos blockPos) {
        return new ContainerFurnace(player.inventory, (TileEntityMachineFurnace) world.getTileEntity(blockPos));
    }
}
