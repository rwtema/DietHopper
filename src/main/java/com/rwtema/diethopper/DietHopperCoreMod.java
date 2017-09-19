package com.rwtema.diethopper;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions(value = {"com.rwtema.hexeresy.", "com.rwtema.hexeresy.HexRenderingCoreMod"})
@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE)
public class DietHopperCoreMod implements IFMLLoadingPlugin {
	public static final Logger logger = LogManager.getLogger("DietHopperCoreMod");


	public DietHopperCoreMod() {
		logger.info("Diet Hopper Core Mod - loaded");
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{DietHopperClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
