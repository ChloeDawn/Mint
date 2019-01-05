package io.github.insomniakitten.mint.common.init;

import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import io.github.insomniakitten.mint.common.Mint;
import io.github.insomniakitten.mint.common.state.RegistrationState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultMappedRegistry;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Handles registration of Mint's items, and allows for retrieving
 * the instance or name of registered Mint items
 *
 * @author InsomniaKitten
 */
final class MintItems {
  static final MintItems INSTANCE = new MintItems();

  private static final Logger LOGGER = Mint.getLogger("items");

  private volatile RegistrationState state = RegistrationState.initial();

  private MintItems() {}

  /**
   * Retrieves an {@link Item} for the given name from the item registry
   * The given name is wrapped as a {@link Identifier} with Mint's namespace,
   * and passed to {@link DefaultMappedRegistry#get(Identifier)}.
   * If items have not yet been registered, an exception will be thrown
   *
   * @param name The name of the item to be retrieved
   * @return The item, or {@link Items#AIR} if not found
   */
  Item getItem(final String name) {
    if (this.state.isUnregistered()) {
      throw new UnsupportedOperationException("Items not registered");
    }

    if (this.state.isRegistering()) {
      throw new ConcurrentModificationException("Items still registering");
    }

    final Identifier key = Mint.withNamespace(name);
    @Nullable final Item item = Registry.ITEM.get(key);

    return Objects.requireNonNull(item, "Item '" + key + "'");
  }

  /**
   * Retrieves the name of the given {@link Item} from the item registry
   * If items have not yet been registered, an exception will be thrown
   *
   * @param item The item to retrieve the name of
   * @return The name of the item
   */
  Identifier getName(final Item item) {
    if (this.state.isUnregistered()) {
      throw new UnsupportedOperationException("Items not registered");
    }

    if (this.state.isRegistering()) {
      throw new ConcurrentModificationException("Items still registering");
    }

    return Objects.requireNonNull(Registry.ITEM.getId(item), "name");
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("state", this.state).toString();
  }

  /**
   * Registers all of Mint's items to the item registry. If this is called whilst already
   * executing, or if registration has been completed, an exception will be thrown.
   */
  void registerItems() {
    if (this.state.isRegistered()) {
      throw new UnsupportedOperationException("Items already registered");
    }

    if (this.state.isRegistering()) {
      throw new ConcurrentModificationException("Items already registering");
    }

    MintItems.LOGGER.info("Beginning item registration");

    this.state = RegistrationState.REGISTERING;

    final Stopwatch stopwatch = Stopwatch.createStarted();

    this.registerBlockItem("terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("white_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("orange_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("magenta_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_blue_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("yellow_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("lime_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("pink_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("gray_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_gray_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("cyan_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("purple_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("blue_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("brown_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("green_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("red_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("black_terracotta_stairs", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("white_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("orange_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("magenta_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_blue_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("yellow_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("lime_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("pink_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("gray_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_gray_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("cyan_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("purple_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("blue_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("brown_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("green_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("red_terracotta_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("black_terracotta_slab", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("white_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("orange_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("magenta_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_blue_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("yellow_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("lime_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("pink_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("gray_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_gray_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("cyan_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("purple_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("blue_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("brown_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("green_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("red_concrete_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("black_concrete_stairs", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("white_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("orange_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("magenta_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_blue_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("yellow_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("lime_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("pink_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("gray_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_gray_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("cyan_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("purple_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("blue_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("brown_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("green_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("red_concrete_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("black_concrete_slab", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("white_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("orange_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("magenta_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_blue_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("yellow_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("lime_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("pink_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("gray_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_gray_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("cyan_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("purple_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("blue_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("brown_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("green_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("red_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("black_stained_glass_stairs", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("white_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("orange_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("magenta_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_blue_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("yellow_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("lime_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("pink_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("gray_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("light_gray_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("cyan_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("purple_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("blue_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("brown_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("green_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("red_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("black_stained_glass_slab", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("oak_log_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("spruce_log_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("birch_log_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("jungle_log_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("acacia_log_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("dark_oak_log_stairs", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("oak_wood_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("spruce_wood_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("birch_wood_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("jungle_wood_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("acacia_wood_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("dark_oak_wood_stairs", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("stripped_oak_log_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_spruce_log_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_birch_log_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_jungle_log_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_acacia_log_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_dark_oak_log_stairs", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("stripped_oak_wood_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_spruce_wood_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_birch_wood_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_jungle_wood_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_acacia_wood_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_dark_oak_wood_stairs", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("oak_log_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("spruce_log_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("birch_log_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("jungle_log_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("acacia_log_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("dark_oak_log_slab", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("oak_wood_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("spruce_wood_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("birch_wood_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("jungle_wood_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("acacia_wood_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("dark_oak_wood_slab", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("stripped_oak_log_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_spruce_log_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_birch_log_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_jungle_log_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_acacia_log_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_dark_oak_log_slab", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("stripped_oak_wood_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_spruce_wood_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_birch_wood_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_jungle_wood_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_acacia_wood_slab", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("stripped_dark_oak_wood_slab", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("ice_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("ice_slab", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("dirt_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("dirt_slab", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("grass_block_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("grass_block_slab", ItemGroup.BUILDING_BLOCKS);

    this.registerBlockItem("sand_layer", ItemGroup.DECORATIONS);
    this.registerBlockItem("red_sand_layer", ItemGroup.DECORATIONS);

    this.registerBlockItem("bookshelf_stairs", ItemGroup.BUILDING_BLOCKS);
    this.registerBlockItem("bookshelf_slab", ItemGroup.BUILDING_BLOCKS);

    final Stopwatch stopwatchResult = stopwatch.stop();
    final long elapsed = stopwatchResult.elapsed(TimeUnit.MILLISECONDS);

    this.state = RegistrationState.REGISTERED;

    MintItems.LOGGER.info("Item registration completed in {}ms", elapsed);
  }

  /**
   * Registers the given item with the given name to the item registry
   * The name is wrapped as a {@link Identifier} with Mint's namespace
   *
   * @param name The name of the item being registered
   * @param item The item to be registered
   */
  private void registerItem(final String name, final Item item) {
    final Identifier key = Mint.withNamespace(name);

    MintItems.LOGGER.debug("Registering item '{}'", key);
    Registry.ITEM.register(key, item);
  }

  /**
   * Registers an {@link BlockItem} with the given name to the item registry
   * The item is inferred from the given name, wrapped as a {@link Identifier}
   * and passed to {@link MintBlocks#getBlock(String)} to retrieve the related block
   *
   * @param name The name of the block item being registered
   * @param group The group that the block item should be assigned to
   */
  private void registerBlockItem(final String name, final ItemGroup group) {
    final Identifier key = Mint.withNamespace(name);
    final Item.Settings settings = new Item.Settings().itemGroup(group);
    final BlockItem item = new BlockItem(MintBootstrap.getBlock(name), settings);

    MintItems.LOGGER.debug("Registering block item '{}'", key);
    Registry.ITEM.register(key, item);
  }
}
