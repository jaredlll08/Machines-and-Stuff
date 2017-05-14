package com.blamejared.mas.blocks.machines;

import com.blamejared.mas.network.PacketHandler;
import com.blamejared.mas.network.messages.tiles.machines.MessageMachineBase;
import com.blamejared.mas.tileentities.machine.TileEntityMachineBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by Jared on 5/3/2016.
 */
public abstract class BlockMachine extends BlockContainer {
    
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool isActive = PropertyBool.create("active");
    
    protected BlockMachine() {
        super(Material.IRON);
    }
    
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, isActive});
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(isActive, false);
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
    
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        
        if(tileentity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        
        super.breakBlock(worldIn, pos, state);
    }
    
    public void setState(boolean active, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        worldIn.setBlockState(pos, getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(isActive, active), 3);
        worldIn.setBlockState(pos, getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(isActive, active), 3);
        
        if(tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
            PacketHandler.INSTANCE.sendToAllAround(new MessageMachineBase((TileEntityMachineBase) tileentity), new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(), (double) tileentity.getPos().getX(), (double) tileentity.getPos().getY(), (double) tileentity.getPos().getZ(), 128d));
        }
    }
}
