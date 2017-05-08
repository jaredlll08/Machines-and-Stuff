package com.blamejared.mas.blocks.generators.base;

import com.blamejared.mas.MAS;
import com.blamejared.mas.network.PacketHandler;
import com.blamejared.mas.network.messages.tiles.generator.MessageGenerator;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.*;

public abstract class BlockBaseGenerator extends BlockContainer {
    
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool isActive = PropertyBool.create("active");
    
    public BlockBaseGenerator() {
        super(Material.IRON);
    }
    
    public static void setState(boolean active, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        worldIn.setBlockState(pos, iblockstate.getBlock().getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(isActive, active), 3);
        
        if(tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
            PacketHandler.INSTANCE.sendToAllAround(new MessageGenerator(tileentity), new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(), tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), 128d));
        }
    }
    
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, isActive});
    }
    
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        boolean active = false;
        if((meta & 15 >> 2) == 1) {
            active = true;
        }
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(isActive, active);
    }
    
    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | (state.getValue(FACING)).getHorizontalIndex();
        if(state.getValue(isActive)) {
            i = i | 1 << 2;
        } else {
            i = i | 0 << 2;
        }
        return i;
    }
    
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(isActive, false);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            playerIn.openGui(MAS.INSTANCE, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
    
    
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
}
