package diacritics.owo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.TradeOffers.BuyForOneEmeraldFactory;
import net.minecraft.village.TradeOffers.SellItemFactory;
import net.minecraft.world.poi.PointOfInterestType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ImmutableSet;

// TODO: exposure integration
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

			TradeOfferHelper.registerVillagerOffers(photographer, 1, (factories) -> {
				factories.add(new BuyForOneEmeraldFactory(Items.PAPER, 24, 16, 2));
				factories.add(new BuyForOneEmeraldFactory(Items.BLACK_DYE, 32, 10, 4));
				factories.add(new BuyForOneEmeraldFactory(Items.WHITE_DYE, 32, 10, 4));
			});

			TradeOfferHelper.registerVillagerOffers(photographer, 2, (factories) -> {
				factories.add(new BuyForOneEmeraldFactory(Items.RED_DYE, 32, 10, 5));
				factories.add(new BuyForOneEmeraldFactory(Items.GREEN_DYE, 32, 10, 5));
				factories.add(new BuyForOneEmeraldFactory(Items.BLUE_DYE, 32, 10, 5));
			});

			TradeOfferHelper.registerVillagerOffers(photographer, 3, (factories) -> {
				factories.add(new SellItemFactory(cameraBlock.asItem(), 15, 1, 6));
			});

			/* ImmutableMap.of(1,
					new Factory[] {new BuyForOneEmeraldFactory(Blocks.WHITE_WOOL, 18, 16, 2),
							new BuyForOneEmeraldFactory(Blocks.BROWN_WOOL, 18, 16, 2),
							new BuyForOneEmeraldFactory(Blocks.BLACK_WOOL, 18, 16, 2),
							new BuyForOneEmeraldFactory(Blocks.GRAY_WOOL, 18, 16, 2),
							new SellItemFactory(Items.SHEARS, 2, 1, 1)},
					2,
					new Factory[] {new BuyForOneEmeraldFactory(Items.WHITE_DYE, 12, 16, 10),
							new BuyForOneEmeraldFactory(Items.GRAY_DYE, 12, 16, 10),
							new BuyForOneEmeraldFactory(Items.BLACK_DYE, 12, 16, 10),
							new BuyForOneEmeraldFactory(Items.LIGHT_BLUE_DYE, 12, 16, 10),
							new BuyForOneEmeraldFactory(Items.LIME_DYE, 12, 16, 10),
							new SellItemFactory(Blocks.WHITE_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.ORANGE_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.MAGENTA_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.LIGHT_BLUE_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.YELLOW_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.LIME_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.PINK_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.GRAY_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.LIGHT_GRAY_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.CYAN_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.PURPLE_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.BLUE_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.BROWN_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.GREEN_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.RED_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.BLACK_WOOL, 1, 1, 16, 5),
							new SellItemFactory(Blocks.WHITE_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.ORANGE_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.MAGENTA_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.LIGHT_BLUE_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.YELLOW_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.LIME_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.PINK_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.GRAY_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.LIGHT_GRAY_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.CYAN_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.PURPLE_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.BLUE_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.BROWN_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.GREEN_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.RED_CARPET, 1, 4, 16, 5),
							new SellItemFactory(Blocks.BLACK_CARPET, 1, 4, 16, 5)},
					3,
					new Factory[] {new BuyForOneEmeraldFactory(Items.YELLOW_DYE, 12, 16, 20),
							new BuyForOneEmeraldFactory(Items.LIGHT_GRAY_DYE, 12, 16, 20),
							new BuyForOneEmeraldFactory(Items.ORANGE_DYE, 12, 16, 20),
							new BuyForOneEmeraldFactory(Items.RED_DYE, 12, 16, 20),
							new BuyForOneEmeraldFactory(Items.PINK_DYE, 12, 16, 20),
							new SellItemFactory(Blocks.WHITE_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.YELLOW_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.RED_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.BLACK_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.BLUE_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.BROWN_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.CYAN_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.GRAY_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.GREEN_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.LIGHT_BLUE_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.LIGHT_GRAY_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.LIME_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.MAGENTA_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.ORANGE_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.PINK_BED, 3, 1, 12, 10),
							new SellItemFactory(Blocks.PURPLE_BED, 3, 1, 12, 10)},
					4,
					new Factory[] {new BuyForOneEmeraldFactory(Items.BROWN_DYE, 12, 16, 30),
							new BuyForOneEmeraldFactory(Items.PURPLE_DYE, 12, 16, 30),
							new BuyForOneEmeraldFactory(Items.BLUE_DYE, 12, 16, 30),
							new BuyForOneEmeraldFactory(Items.GREEN_DYE, 12, 16, 30),
							new BuyForOneEmeraldFactory(Items.MAGENTA_DYE, 12, 16, 30),
							new BuyForOneEmeraldFactory(Items.CYAN_DYE, 12, 16, 30),
							new SellItemFactory(Items.WHITE_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.BLUE_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.LIGHT_BLUE_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.RED_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.PINK_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.GREEN_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.LIME_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.GRAY_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.BLACK_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.PURPLE_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.MAGENTA_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.CYAN_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.BROWN_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.YELLOW_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.ORANGE_BANNER, 3, 1, 12, 15),
							new SellItemFactory(Items.LIGHT_GRAY_BANNER, 3, 1, 12, 15)},
					5, new Factory[] {new SellItemFactory(Items.PAINTING, 2, 3, 30)}); */
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
