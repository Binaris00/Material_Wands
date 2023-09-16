package binaris.material_wands;

import binaris.material_wands.registry.MaterialWands_Items;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaterialWands implements ModInitializer {
	public static final String MOD_ID = "material_wands";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MaterialWands_Items.registerAllItems();
		LOGGER.info("This is a welcome message :O!");
	}
}