package com.blamejared.mas.blocks;

import com.blamejared.mas.blocks.generators.*;
import com.blamejared.mas.blocks.misc.BlockCrank;
import com.blamejared.mas.reference.Reference;
import com.blamejared.mas.tileentities.generators.*;
import com.blamejared.mas.tileentities.misc.TileEntityCrank;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.*;
import java.util.*;

import static com.blamejared.mas.reference.Reference.*;

public class MBlocks {
    
    public static Map<String, Block> renderMap = new HashMap<>();
    
    public static Block GENERATOR_COAL = new BlockGeneratorCoal();
    public static Block GENERATOR_TRASH = new BlockGeneratorTrash();
    public static Block CRANK = new BlockCrank();
    
    
    public static void preInit() {
        registerBlock(GENERATOR_COAL, "generator_coal", TileEntityGeneratorCoal.class);
        registerBlock(GENERATOR_TRASH, "generator_trash", TileEntityGeneratorTrash.class);
        registerBlock(CRANK, "crank", TileEntityCrank.class);
    
    }
    
    private static void registerBlock(Block block, String key) {
        registerBlock(block, key, key, null, TAB);
    }
    
    private static void registerBlock(Block block, String key, String texture) {
        registerBlock(block, key, texture, null, TAB);
    }
    
    private static void registerBlock(Block block, String key, String texture, Class tile) {
        registerBlock(block, key, texture, tile, TAB);
    }
    
    
    private static void registerBlock(Block block, String key, Class tile) {
        registerBlock(block, key, key, tile, TAB);
    }
    
    private static void registerBlock(Block block, String key, Class tile, CreativeTabs tab) {
        registerBlock(block, key, key, tile, TAB);
    }
    
    private static void registerBlock(Block block, String key, String texture, Class tile, CreativeTabs tab) {
        block.setUnlocalizedName(key).setCreativeTab(TAB);
        if(DEVENV && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            writeFile(key, texture);
        renderMap.put(texture, block);
        GameRegistry.register(block, new ResourceLocation(Reference.MODID + ":" + key));
        GameRegistry.register(new ItemBlock(block), new ResourceLocation(Reference.MODID + ":" + key));
        if(tile != null) {
            GameRegistry.registerTileEntity(tile, key);
        }
    }
    
    private static void writeFile(String key, String texture) {
        try {
            File baseBlockState = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            File baseBlockModel = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            File baseItem = new File(System.getProperty("user.home") + "/getFluxed/" + key + ".json");
            if(System.getProperty("user.home").endsWith("Jared")) {
                
                baseBlockState = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/blockstates/" + key + ".json");
                baseBlockModel = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/block/" + key + ".json");
                baseItem = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/item/" + key + ".json");
            }
            if(!baseBlockState.exists()) {
                baseBlockState.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseBlockState.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
                while(scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if(line.contains("%modid%")) {
                        line = line.replace("%modid%", Reference.MODID);
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
                FileWriter write = new FileWriter(baseBlockState);
                for(String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }
            if(!baseBlockModel.exists()) {
                baseBlockModel.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseBlockModel.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
                while(scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if(line.contains("%modid%")) {
                        line = line.replace("%modid%", Reference.MODID);
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
                FileWriter write = new FileWriter(baseBlockModel);
                for(String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }
            
            if(!baseItem.exists()) {
                baseItem.createNewFile();
                File base = new File(System.getProperty("user.home") + "/getFluxed/baseBlockItem.json");
                Scanner scan = new Scanner(base);
                List<String> content = new ArrayList<String>();
                while(scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if(line.contains("%modid%")) {
                        line = line.replace("%modid%", Reference.MODID);
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
                FileWriter write = new FileWriter(baseItem);
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
