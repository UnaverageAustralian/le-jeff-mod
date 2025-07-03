package net.unavgaustralian.jeff;

import net.fabricmc.api.ModInitializer;

import net.unavgaustralian.jeff.block.ModBlocks;
import net.unavgaustralian.jeff.events.Events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeJeff implements ModInitializer {
	public static final String MOD_ID = "le-jeff";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		Events.register();
	}
}