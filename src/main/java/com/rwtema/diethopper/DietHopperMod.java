package com.rwtema.diethopper;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod(modid = DietHopperMod.MODID, version = DietHopperMod.VERSION, acceptedMinecraftVersions = "[1.10.2,)")
public class DietHopperMod {
	static final String MODID = "diethopper";
	static final String VERSION = "1.0";

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		Block blockDietHopper = new BlockDietHopper();
		ForgeRegistries.BLOCKS.register(blockDietHopper);
	}
}
