package diacritics.owo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.EmeraldForItems;
import net.minecraft.world.entity.npc.VillagerTrades.ItemsForEmeralds;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ImmutableSet;
import io.github.mortuusars.exposure.Exposure;
import net.fabricmc.loader.api.FabricLoader;

public class LetsDoAddonPhotographers implements ModInitializer {
	public static final String MOD_ID = "lets-do-photographers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ResourceKey<PoiType> CAMERA_KEY =
			ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, identifier("photographer"));
	public static final ResourceKey<VillagerProfession> PHOTOGRAPHER_KEY =
			ResourceKey.create(Registries.VILLAGER_PROFESSION, identifier("photographer"));

	public static final boolean EXPOSURE =
			FabricLoader.getInstance().getModContainer("exposure").isPresent();

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

			PoiType camera = PointOfInterestHelper.register(CAMERA_KEY.location(), 1, 1, cameraBlock);

			VillagerProfession photographer =
					Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, PHOTOGRAPHER_KEY,
							new VillagerProfession(PHOTOGRAPHER_KEY.location().getPath(), (entry) -> true,
									(entry) -> entry.is(CAMERA_KEY), ImmutableSet.of(), ImmutableSet.of(),
									cameraSound));

			// if everything above has gone well, these should both succeed
			assert BuiltInRegistries.POINT_OF_INTEREST_TYPE.wrapAsHolder(camera).isBound();
			assert BuiltInRegistries.VILLAGER_PROFESSION.wrapAsHolder(photographer).isBound();

			TradeOfferHelper.registerVillagerOffers(photographer, 1, (factories) -> {
				factories.add(new EmeraldForItems(Items.PAPER, 24, 16, 2));
				factories.add(new EmeraldForItems(Items.BLACK_DYE, 32, 20, 4));
				factories.add(new EmeraldForItems(Items.WHITE_DYE, 32, 20, 4));
			});

			TradeOfferHelper.registerVillagerOffers(photographer, 2, (factories) -> {
				factories.add(new EmeraldForItems(Items.RED_DYE, 32, 20, 5));
				factories.add(new EmeraldForItems(Items.GREEN_DYE, 32, 20, 5));
				factories.add(new EmeraldForItems(Items.BLUE_DYE, 32, 20, 5));
			});

			TradeOfferHelper.registerVillagerOffers(photographer, 4, (factories) -> {
				factories.add(new ItemsForEmeralds(cameraBlock.asItem(), 4, 1, 7));
			});

			if (EXPOSURE) {
				LOGGER.info("exposure detected - compat will be enabled");

				TradeOfferHelper.registerVillagerOffers(photographer, 2, (factories) -> {
					factories.add(new EmeraldForItems(Exposure.Items.BLACK_AND_WHITE_FILM.get(), 3, 10, 5));
				});

				TradeOfferHelper.registerVillagerOffers(photographer, 3, (factories) -> {
					factories.add(new EmeraldForItems(Exposure.Items.COLOR_FILM.get(), 3, 10, 6));
					factories.add(new EmeraldForItems(Exposure.Items.CHROMATIC_SHEET.get(), 7, 10, 6));
				});

				TradeOfferHelper.registerVillagerOffers(photographer, 5, (factories) -> {
					factories.add(new ItemsForEmeralds(Exposure.Items.CAMERA.get(), 3, 1, 10));
				});
			}
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

	public static ResourceLocation identifier(String path) {
		return ResourceLocation.tryBuild(MOD_ID, path);
	}
}
