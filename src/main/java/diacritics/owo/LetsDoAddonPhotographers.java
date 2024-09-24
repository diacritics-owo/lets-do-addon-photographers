package diacritics.owo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ImmutableSet;

public class LetsDoAddonPhotographers implements ModInitializer {
	public static final String MOD_ID = "lets-do-photographers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final RegistryKey<PointOfInterestType> CAMERA_KEY =
			RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, identifier("photographer"));
	public static final RegistryKey<VillagerProfession> PHOTOGRAPHER_KEY =
			RegistryKey.of(RegistryKeys.VILLAGER_PROFESSION, identifier("photographer"));

	@SuppressWarnings("rawtypes")
	@Override
	public void onInitialize() {
		LOGGER.info("hello from let's do: photographers!");

		// very very bad solution but i'm having issues with meadow mixins so
		try {
			Class objectRegistry = Class.forName("net.satisfy.meadow.registry.ObjectRegistry");
			Class soundRegistry = Class.forName("net.satisfy.meadow.registry.SoundRegistry");

			Block cameraBlock = get(objectRegistry, "CAMERA");
			SoundEvent cameraSound = get(soundRegistry, "CLICK_CAMERA");

			PointOfInterestType camera =
					PointOfInterestHelper.register(CAMERA_KEY.getValue(), 1, 1, cameraBlock);

			VillagerProfession photographer =
					Registry.register(Registries.VILLAGER_PROFESSION, PHOTOGRAPHER_KEY,
							new VillagerProfession(PHOTOGRAPHER_KEY.getValue().getPath(), (entry) -> true,
									(entry) -> entry.matchesKey(CAMERA_KEY), ImmutableSet.of(), ImmutableSet.of(),
									cameraSound));

			// if everything above has gone well, these should both succeed
			assert Registries.POINT_OF_INTEREST_TYPE.getEntry(camera).hasKeyAndValue();
			assert Registries.VILLAGER_PROFESSION.getEntry(photographer).hasKeyAndValue();
		} catch (Exception error) {
			LOGGER.error("failed to initialise! do you have let's do: meadow installed?", error);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<?> _class, String field) throws IllegalAccessException,
			NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
		Object registrySupplier = _class.getField(field).get(null);
		Method getter = registrySupplier.getClass().getMethod("get");
		getter.setAccessible(true);
		return (T) getter.invoke(registrySupplier);
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
