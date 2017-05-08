package com.blamejared.mas.client.gui.base;

import com.blamejared.mas.MAS;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum GuiCarrier {
    ENTITY {
        @Override
        public IHasGui getHasGUI(EntityPlayer player, World world, BlockPos blockPos) {
            IHasGui hasGui = null;
            Entity entity = world.getEntityByID(blockPos.getX());
            if(entity instanceof IHasGui) {
                hasGui = (IHasGui) entity;
            }
            return hasGui;
        }
        
        public void openGui(Entity entity, EntityPlayer player, World world) {
            player.openGui(MAS.INSTANCE, ENTITY.ordinal(), world, entity.getEntityId(), 0, 0);
        }
    }, BLOCK {
        @Override
        public IHasGui getHasGUI(EntityPlayer player, World world, BlockPos blockPos) {
            IHasGui hasGui = null;
            
            Block block = world.getBlockState(blockPos).getBlock();
            if(block instanceof IHasGui) {
                hasGui = (IHasGui) block;
            } else {
                TileEntity tileEntity = world.getTileEntity(blockPos);
                if(tileEntity instanceof IHasGui) {
                    hasGui = (IHasGui) tileEntity;
                }
            }
            
            return hasGui;
        }
        
        public void openGui(EntityPlayer player, World world, BlockPos blockPos) {
            player.openGui(MAS.INSTANCE, BLOCK.ordinal(), world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
    }, ITEMSTACK {
        @Override
        public IHasGui getHasGUI(EntityPlayer player, World world, BlockPos blockPos) {
            IHasGui hasGui = null;
            ItemStack itemStack = player.getActiveItemStack();
            if(itemStack != null && IHasGui.class != null && IHasGui.class.isInstance(itemStack.getItem())) {
                hasGui = (IHasGui) itemStack.getItem();
            }
            return hasGui;
        }
        
        public void openGui(EntityPlayer player, World world, BlockPos blockPos) {
            player.openGui(MAS.INSTANCE, ITEMSTACK.ordinal(), world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
    };
    
    public abstract IHasGui getHasGUI(EntityPlayer player, World world, BlockPos blockPos);
}
