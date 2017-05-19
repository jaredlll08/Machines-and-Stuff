package com.blamejared.mas.blocks.misc;

import com.blamejared.mas.MAS;
import com.blamejared.mas.api.accumulators.EnumAccumulator;
import com.blamejared.mas.client.gui.accumulator.*;
import com.blamejared.mas.client.gui.base.IHasGui;
import com.blamejared.mas.items.MItems;
import com.blamejared.mas.tileentities.misc.energy.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import org.lwjgl.input.Keyboard;

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
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.SOLID;
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
            if(playerIn.inventory.getCurrentItem().getItem() == MItems.WRENCH) {
                TileEntityAccumulator tile = (TileEntityAccumulator) worldIn.getTileEntity(pos);
                EnumFacing face = facing;
                if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
                    face = face.getOpposite();
                }
                AccumulatorInfo info = tile.getInfoForFace(face);
                tile.getInfoForFace(face).setEnabled(!info.isEnabled());
                tile.markDirty();
                return true;
            } else
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
