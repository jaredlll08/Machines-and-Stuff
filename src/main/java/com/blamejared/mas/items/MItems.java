package com.blamejared.mas.items;

import com.blamejared.mas.reference.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.*;
import java.util.*;

import static com.blamejared.mas.reference.Reference.*;

public class MItems {
    
    public static Map<String, Item> renderMap = new HashMap<String, Item>();
    public static Map<Item, int[]> colourMap = new HashMap<>();
    
    public static Item WRENCH = new Item().setFull3D();
    
    public static void preInit() {
        registerItem(WRENCH, "wrench", "wrench");
    }
    
    public static void registerItem(Item item, String name, String key) {
        if(DEVENV && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            writeFile(key, key);
        item.setUnlocalizedName(key).setCreativeTab(TAB);
        renderMap.put(key, item);
        
        GameRegistry.register(item, new ResourceLocation(Reference.MODID + ":" + key));
    }
    
    public static void registerItemColour(Item item, String name, String key, int[] layers) {
        if(DEVENV && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            writeFile(key, key);
        item.setUnlocalizedName(key).setCreativeTab(TAB);
        renderMap.put(key, item);
        colourMap.put(item, layers);
        GameRegistry.register(item, new ResourceLocation(Reference.MODID + ":" + key));
    }
    
    public static void registerItemColour(Item item, String name, String key, String textures[], int[] layers) {
        if(DEVENV && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            for(String tex : textures) {
                writeFile(key, tex);
            }
        }
        
        item.setUnlocalizedName(key).setCreativeTab(TAB);
        renderMap.put(key, item);
        colourMap.put(item, layers);
        GameRegistry.register(item, new ResourceLocation(Reference.MODID + ":" + key));
    }
    
    public static void registerItemMeta(Item item, String name, String key) {
        if(DEVENV && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            writeFile(key, key);
        item.setCreativeTab(TAB);
        renderMap.put(key, item);
        
        GameRegistry.register(item, new ResourceLocation(Reference.MODID + ":" + key));
    }
    
    public static void registerItem(Item item, String name, String key, String texture) {
        if(DEVENV && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            writeFile(key, texture);
        item.setUnlocalizedName(key).setCreativeTab(TAB);
        
        GameRegistry.register(item, new ResourceLocation(Reference.MODID + ":" + key));
    }
    
    public static void writeFile(String key, String texture) {
        try {
            File f = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            if(System.getProperty("user.home").endsWith("Jared")) {
                f = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/item/" + key + ".json");
            }
            if(!f.exists()) {
                f.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseItem.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<>();
                while(scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if(line.contains("%MODID%")) {
                        line = line.replace("%MODID%", Reference.MODID);
                    }
                    if(line.contains("%key%")) {
                        line = line.replace("%key%", key);
                    }
                    if(line.contains("%texture%")) {
                        line = line.replace("%texture%", texture);
                    }
                    content.add(line);
                }
                scan.close();
                FileWriter write = new FileWriter(f);
                for(String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
