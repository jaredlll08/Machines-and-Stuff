package com.blamejared.mas.reference;

import com.blamejared.mas.misc.CreativeTabMAS;
import net.minecraft.launchwrapper.Launch;

public class Reference {
    
    public static final String MODID = "mas";
    public static final String NAME = "Machines-And-Stuff";
    public static final String VERSION = "1.0.0";
    public static final String DEPENDENCIES = "";
    
    public static final CreativeTabMAS TAB = new CreativeTabMAS();
    public static final boolean DEVENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
}
