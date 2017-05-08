package com.blamejared.mas.client.gui.base;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * Created by Jared on 5/29/2016.
 */
public class ContainerBase extends Container {
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
    
    private void updateSlot(final Slot slot) {
        this.detectAndSendChanges();
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int idx) {
        
        //TODO change to work with isItemValid ==false
        ItemStack itemStack = ItemStack.EMPTY;
        Slot clickSlot = (Slot) this.inventorySlots.get(idx);
        
        //        if (clickSlot instanceof SlotOutput) {
        //            return null;
        //        }
        
        if((clickSlot != null) && (clickSlot.getHasStack())) {
            itemStack = clickSlot.getStack();
            
            if(itemStack == ItemStack.EMPTY) {
                return ItemStack.EMPTY;
            }
            List<Slot> selectedSlots = new ArrayList<Slot>();
            
            if(clickSlot.inventory instanceof InventoryPlayer) {
                for(int x = 0; x < this.inventorySlots.size(); x++) {
                    Slot advSlot = (Slot) this.inventorySlots.get(x);
                    if(advSlot.isItemValid(itemStack)) {
                        selectedSlots.add(advSlot);
                    }
                }
            } else {
                for(int x = 0; x < this.inventorySlots.size(); x++) {
                    Slot advSlot = (Slot) this.inventorySlots.get(x);
                    
                    if((advSlot.inventory instanceof InventoryPlayer)) {
                        if(advSlot.isItemValid(itemStack)) {
                            selectedSlots.add(advSlot);
                        }
                    }
                }
            }
            
            if(itemStack != ItemStack.EMPTY) {
                for(Slot d : selectedSlots) {
                    if((d.isItemValid(itemStack)) && (itemStack != ItemStack.EMPTY)) {
                        if(d.getHasStack()) {
                            ItemStack t = d.getStack();
                            
                            if((itemStack != null) && (itemStack.isItemEqual(t))) {
                                int maxSize = t.getMaxStackSize();
                                
                                if(maxSize > d.getSlotStackLimit()) {
                                    maxSize = d.getSlotStackLimit();
                                }
                                
                                int placeAble = maxSize - t.getCount();
                                
                                if(itemStack.getCount() < placeAble) {
                                    placeAble = itemStack.getCount();
                                }
                                
                                t.grow(placeAble);
                                itemStack.shrink(placeAble);
                                
                                if(itemStack.getCount() <= 0) {
                                    clickSlot.putStack(ItemStack.EMPTY);
                                    d.onSlotChanged();
                                    updateSlot(clickSlot);
                                    updateSlot(d);
                                    return ItemStack.EMPTY;
                                }
                                
                                updateSlot(d);
                            }
                        } else {
                            int maxSize = itemStack.getMaxStackSize();
                            
                            if(maxSize > d.getSlotStackLimit()) {
                                maxSize = d.getSlotStackLimit();
                            }
                            
                            ItemStack tmp = itemStack.copy();
                            
                            if(tmp.getCount() > maxSize) {
                                tmp.setCount(maxSize);
                            }
                            itemStack.shrink(tmp.getCount());
                            //Sets the stack
                            d.putStack(tmp);
                            
                            if(itemStack.getCount() <= 0) {
                                clickSlot.putStack(ItemStack.EMPTY);
                                d.onSlotChanged();
                                updateSlot(clickSlot);
                                updateSlot(d);
                                return ItemStack.EMPTY;
                            }
                            
                            updateSlot(d);
                        }
                    }
                }
            }
            
            clickSlot.putStack(itemStack != ItemStack.EMPTY ? itemStack.copy() : ItemStack.EMPTY);
        }
        updateSlot(clickSlot);
        return ItemStack.EMPTY;
    }
    
    
}
