package com.blamejared.mas.blocks.misc;

import com.blamejared.mas.MAS;
import com.blamejared.mas.api.accumulators.EnumAccumulator;
import com.blamejared.mas.client.gui.accumulator.*;
import com.blamejared.mas.client.gui.base.IHasGui;
import com.blamejared.mas.client.gui.generatorcoal.*;
import com.blamejared.mas.tileentities.generators.TileEntityGeneratorCoal;
import com.blamejared.mas.tileentities.misc.energy.TileEntityAccumulator;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

import javax.annotation.Nullable;

public class BlockAccumulator extends Block implements ITileEntityProvider, IHasGui {
    
    EnumAccumulator enumAccumulator;
    
    public BlockAccumulator(EnumAccumulator enumAccumulator) {
        super(Material.IRON);
        this.enumAccumulator = enumAccumulator;
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAccumulator(enumAccumulator);
    }
    
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            playerIn.openGui(MAS.INSTANCE, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
    @Override
    public Gui getClientGuiElement(int id, EntityPlayer player, World world, BlockPos blockPos) {
        return new GuiAccumulator(player.inventory, (TileEntityAccumulator) world.getTileEntity(blockPos));
    }
    
    @Override
    public Container getServerGuiElement(int id, EntityPlayer player, World world, BlockPos blockPos) {
        return new ContainerAccumulator(player.inventory, (TileEntityAccumulator) world.getTileEntity(blockPos));
    }
    
}
