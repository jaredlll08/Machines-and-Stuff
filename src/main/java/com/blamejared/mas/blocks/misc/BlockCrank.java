package com.blamejared.mas.blocks.misc;

import com.blamejared.mas.blocks.MBlocks;
import com.blamejared.mas.network.PacketHandler;
import com.blamejared.mas.network.messages.tiles.misc.MessageCrank;
import com.blamejared.mas.tileentities.misc.TileEntityCrank;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_CONSUMER;
import static net.darkhax.tesla.capability.TeslaCapabilities.CAPABILITY_HOLDER;

public class BlockCrank extends Block implements ITileEntityProvider {
    
    public BlockCrank() {
        super(Material.WOOD);
    }
    
    
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos.down());
        if(tile != null) {
            if(tile.hasCapability(CAPABILITY_HOLDER, EnumFacing.UP)|| tile.hasCapability(CAPABILITY_CONSUMER, EnumFacing.UP)) {
                return true;
            }
        }
        return false;
    }
    
    
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return new AxisAlignedBB(pos.getX() + 0.19, pos.getY(), pos.getZ() + 0.19, pos.getX() + 0.81, pos.getY() + 0.5, pos.getZ() + 0.81);
    }
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        TileEntity tile = worldIn.getTileEntity(pos.down());
        if(tile != null) {
            if(tile.hasCapability(CAPABILITY_HOLDER, EnumFacing.UP) || tile.hasCapability(CAPABILITY_CONSUMER, EnumFacing.UP)) {
                return;
            }
        }
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        spawnAsEntity(worldIn, pos, new ItemStack(MBlocks.CRANK));
    }
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) {
            return true;
        }
        TileEntityCrank tile = (TileEntityCrank) worldIn.getTileEntity(pos);
        if(tile.generationTimerDefault <= 100) {
            tile.generationTimerDefault += 5;
            tile.generationTimer += 5;
            PacketHandler.INSTANCE.sendToAllAround(new MessageCrank(tile), new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(), (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), 128d));
        }
        return true;
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
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCrank();
    }
}
