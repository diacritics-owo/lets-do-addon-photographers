package diacritics.owo;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LetsDoAddonPhotographers implements ModInitializer {
	public static final String MOD_ID = "lets-do-photographers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("hello from let's do: photographers!");
	}
}
