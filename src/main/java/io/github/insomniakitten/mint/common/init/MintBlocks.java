package io.github.insomniakitten.mint.common.init;

import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import io.github.insomniakitten.mint.common.Mint;
import io.github.insomniakitten.mint.common.block.BookshelfSlabBlock;
import io.github.insomniakitten.mint.common.block.BookshelfStairsBlock;
import io.github.insomniakitten.mint.common.block.DirtSlabBlock;
import io.github.insomniakitten.mint.common.block.DirtStairsBlock;
import io.github.insomniakitten.mint.common.block.GrassSlabBlock;
import io.github.insomniakitten.mint.common.block.GrassStairsBlock;
import io.github.insomniakitten.mint.common.block.IceSlabBlock;
import io.github.insomniakitten.mint.common.block.IceStairsBlock;
import io.github.insomniakitten.mint.common.block.LogSlabBlock;
import io.github.insomniakitten.mint.common.block.LogStairsBlock;
import io.github.insomniakitten.mint.common.block.SandLayerBlock;
import io.github.insomniakitten.mint.common.block.SimpleSlabBlock;
import io.github.insomniakitten.mint.common.block.SimpleStairsBlock;
import io.github.insomniakitten.mint.common.block.TransparentSlabBlock;
import io.github.insomniakitten.mint.common.block.TransparentStairsBlock;
import io.github.insomniakitten.mint.common.state.RegistrationState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultMappedRegistry;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Handles registration of Mint's blocks, and allows for retrieving
 * the instance or name of registered Mint blocks
 *
 * @author InsomniaKitten
 */
final class MintBlocks {
  static final MintBlocks INSTANCE = new MintBlocks();

  private static final Logger LOGGER = Mint.getLogger("blocks");

  private volatile RegistrationState state = RegistrationState.initial();

  private MintBlocks() {}

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("state", this.state).toString();
  }

  /**
   * Retrieves an {@link Block} for the given name from the block registry
   * The given name is wrapped as a {@link Identifier} with Mint's namespace,
   * and passed to {@link DefaultMappedRegistry#get(Identifier)}.
   * If blocks have not yet been registered, an exception will be thrown
   *
   * @param name The name of the block to be retrieved
   * @return The block, or {@link Blocks#AIR} if not found
   */
  Block getBlock(final String name) {
    if (this.state.isUnregistered()) {
      throw new UnsupportedOperationException("Blocks not registered");
    }

    if (this.state.isRegistering()) {
      throw new ConcurrentModificationException("Blocks still registering");
    }

    final Identifier key = Mint.withNamespace(name);
    @Nullable final Block block = Registry.BLOCK.get(key);

    return Objects.requireNonNull(block, "Block '" + key + "'");
  }

  /**
   * Retrieves the name of the given {@link Block} from the block registry
   * If blocks have not yet been registered, an exception will be thrown
   *
   * @param block The block to retrieve the name of
   * @return The name of the block
   */
  Identifier getName(final Block block) {
    if (this.state.isUnregistered()) {
      throw new UnsupportedOperationException("Blocks not registered");
    }

    if (this.state.isRegistering()) {
      throw new ConcurrentModificationException("Blocks still registering");
    }

    return Objects.requireNonNull(Registry.BLOCK.getId(block), "name");
  }

  /**
   * Registers all of Mint's blocks to the block registry. If this is called whilst already
   * executing, or if registration has been completed, an exception will be thrown.
   */
  void registerBlocks() {
    if (this.state.isRegistered()) {
      throw new UnsupportedOperationException("Blocks already registered");
    }

    if (this.state.isRegistering()) {
      throw new ConcurrentModificationException("Blocks already registering");
    }

    this.state = RegistrationState.REGISTERING;

    MintBlocks.LOGGER.info("Beginning block registration");

    final Stopwatch stopwatch = Stopwatch.createStarted();

    this.registerBlock("terracotta_stairs", new SimpleStairsBlock(Blocks.TERRACOTTA));
    this.registerBlock("white_terracotta_stairs", new SimpleStairsBlock(Blocks.WHITE_TERRACOTTA));
    this.registerBlock("orange_terracotta_stairs", new SimpleStairsBlock(Blocks.ORANGE_TERRACOTTA));
    this.registerBlock("magenta_terracotta_stairs", new SimpleStairsBlock(Blocks.MAGENTA_TERRACOTTA));
    this.registerBlock("light_blue_terracotta_stairs", new SimpleStairsBlock(Blocks.LIGHT_BLUE_TERRACOTTA));
    this.registerBlock("yellow_terracotta_stairs", new SimpleStairsBlock(Blocks.YELLOW_TERRACOTTA));
    this.registerBlock("lime_terracotta_stairs", new SimpleStairsBlock(Blocks.LIME_TERRACOTTA));
    this.registerBlock("pink_terracotta_stairs", new SimpleStairsBlock(Blocks.PINK_TERRACOTTA));
    this.registerBlock("gray_terracotta_stairs", new SimpleStairsBlock(Blocks.GRAY_TERRACOTTA));
    this.registerBlock("light_gray_terracotta_stairs", new SimpleStairsBlock(Blocks.LIGHT_GRAY_TERRACOTTA));
    this.registerBlock("cyan_terracotta_stairs", new SimpleStairsBlock(Blocks.CYAN_TERRACOTTA));
    this.registerBlock("purple_terracotta_stairs", new SimpleStairsBlock(Blocks.PURPLE_TERRACOTTA));
    this.registerBlock("blue_terracotta_stairs", new SimpleStairsBlock(Blocks.BLUE_TERRACOTTA));
    this.registerBlock("brown_terracotta_stairs", new SimpleStairsBlock(Blocks.BROWN_TERRACOTTA));
    this.registerBlock("green_terracotta_stairs", new SimpleStairsBlock(Blocks.GREEN_TERRACOTTA));
    this.registerBlock("red_terracotta_stairs", new SimpleStairsBlock(Blocks.RED_TERRACOTTA));
    this.registerBlock("black_terracotta_stairs", new SimpleStairsBlock(Blocks.BLACK_TERRACOTTA));

    this.registerBlock("terracotta_slab", new SimpleSlabBlock(Blocks.TERRACOTTA));
    this.registerBlock("white_terracotta_slab", new SimpleSlabBlock(Blocks.WHITE_TERRACOTTA));
    this.registerBlock("orange_terracotta_slab", new SimpleSlabBlock(Blocks.ORANGE_TERRACOTTA));
    this.registerBlock("magenta_terracotta_slab", new SimpleSlabBlock(Blocks.MAGENTA_TERRACOTTA));
    this.registerBlock("light_blue_terracotta_slab", new SimpleSlabBlock(Blocks.LIGHT_BLUE_TERRACOTTA));
    this.registerBlock("yellow_terracotta_slab", new SimpleSlabBlock(Blocks.YELLOW_TERRACOTTA));
    this.registerBlock("lime_terracotta_slab", new SimpleSlabBlock(Blocks.LIME_TERRACOTTA));
    this.registerBlock("pink_terracotta_slab", new SimpleSlabBlock(Blocks.PINK_TERRACOTTA));
    this.registerBlock("gray_terracotta_slab", new SimpleSlabBlock(Blocks.GRAY_TERRACOTTA));
    this.registerBlock("light_gray_terracotta_slab", new SimpleSlabBlock(Blocks.LIGHT_GRAY_TERRACOTTA));
    this.registerBlock("cyan_terracotta_slab", new SimpleSlabBlock(Blocks.CYAN_TERRACOTTA));
    this.registerBlock("purple_terracotta_slab", new SimpleSlabBlock(Blocks.PURPLE_TERRACOTTA));
    this.registerBlock("blue_terracotta_slab", new SimpleSlabBlock(Blocks.BLUE_TERRACOTTA));
    this.registerBlock("brown_terracotta_slab", new SimpleSlabBlock(Blocks.BROWN_TERRACOTTA));
    this.registerBlock("green_terracotta_slab", new SimpleSlabBlock(Blocks.GREEN_TERRACOTTA));
    this.registerBlock("red_terracotta_slab", new SimpleSlabBlock(Blocks.RED_TERRACOTTA));
    this.registerBlock("black_terracotta_slab", new SimpleSlabBlock(Blocks.BLACK_TERRACOTTA));

    this.registerBlock("white_concrete_stairs", new SimpleStairsBlock(Blocks.WHITE_CONCRETE));
    this.registerBlock("orange_concrete_stairs", new SimpleStairsBlock(Blocks.ORANGE_CONCRETE));
    this.registerBlock("magenta_concrete_stairs", new SimpleStairsBlock(Blocks.MAGENTA_CONCRETE));
    this.registerBlock("light_blue_concrete_stairs", new SimpleStairsBlock(Blocks.LIGHT_BLUE_CONCRETE));
    this.registerBlock("yellow_concrete_stairs", new SimpleStairsBlock(Blocks.YELLOW_CONCRETE));
    this.registerBlock("lime_concrete_stairs", new SimpleStairsBlock(Blocks.LIME_CONCRETE));
    this.registerBlock("pink_concrete_stairs", new SimpleStairsBlock(Blocks.PINK_CONCRETE));
    this.registerBlock("gray_concrete_stairs", new SimpleStairsBlock(Blocks.GRAY_CONCRETE));
    this.registerBlock("light_gray_concrete_stairs", new SimpleStairsBlock(Blocks.LIGHT_GRAY_CONCRETE));
    this.registerBlock("cyan_concrete_stairs", new SimpleStairsBlock(Blocks.CYAN_CONCRETE));
    this.registerBlock("purple_concrete_stairs", new SimpleStairsBlock(Blocks.PURPLE_CONCRETE));
    this.registerBlock("blue_concrete_stairs", new SimpleStairsBlock(Blocks.BLUE_CONCRETE));
    this.registerBlock("brown_concrete_stairs", new SimpleStairsBlock(Blocks.BROWN_CONCRETE));
    this.registerBlock("green_concrete_stairs", new SimpleStairsBlock(Blocks.GREEN_CONCRETE));
    this.registerBlock("red_concrete_stairs", new SimpleStairsBlock(Blocks.RED_CONCRETE));
    this.registerBlock("black_concrete_stairs", new SimpleStairsBlock(Blocks.BLACK_CONCRETE));

    this.registerBlock("white_concrete_slab", new SimpleSlabBlock(Blocks.WHITE_CONCRETE));
    this.registerBlock("orange_concrete_slab", new SimpleSlabBlock(Blocks.ORANGE_CONCRETE));
    this.registerBlock("magenta_concrete_slab", new SimpleSlabBlock(Blocks.MAGENTA_CONCRETE));
    this.registerBlock("light_blue_concrete_slab", new SimpleSlabBlock(Blocks.LIGHT_BLUE_CONCRETE));
    this.registerBlock("yellow_concrete_slab", new SimpleSlabBlock(Blocks.YELLOW_CONCRETE));
    this.registerBlock("lime_concrete_slab", new SimpleSlabBlock(Blocks.LIME_CONCRETE));
    this.registerBlock("pink_concrete_slab", new SimpleSlabBlock(Blocks.PINK_CONCRETE));
    this.registerBlock("gray_concrete_slab", new SimpleSlabBlock(Blocks.GRAY_CONCRETE));
    this.registerBlock("light_gray_concrete_slab", new SimpleSlabBlock(Blocks.LIGHT_GRAY_CONCRETE));
    this.registerBlock("cyan_concrete_slab", new SimpleSlabBlock(Blocks.CYAN_CONCRETE));
    this.registerBlock("purple_concrete_slab", new SimpleSlabBlock(Blocks.PURPLE_CONCRETE));
    this.registerBlock("blue_concrete_slab", new SimpleSlabBlock(Blocks.BLUE_CONCRETE));
    this.registerBlock("brown_concrete_slab", new SimpleSlabBlock(Blocks.BROWN_CONCRETE));
    this.registerBlock("green_concrete_slab", new SimpleSlabBlock(Blocks.GREEN_CONCRETE));
    this.registerBlock("red_concrete_slab", new SimpleSlabBlock(Blocks.RED_CONCRETE));
    this.registerBlock("black_concrete_slab", new SimpleSlabBlock(Blocks.BLACK_CONCRETE));

    this.registerBlock("glass_stairs", new TransparentStairsBlock(Blocks.GLASS, BlockRenderLayer.CUTOUT));
    this.registerBlock("white_stained_glass_stairs", new TransparentStairsBlock(Blocks.WHITE_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("orange_stained_glass_stairs", new TransparentStairsBlock(Blocks.ORANGE_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("magenta_stained_glass_stairs", new TransparentStairsBlock(Blocks.MAGENTA_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("light_blue_stained_glass_stairs", new TransparentStairsBlock(Blocks.LIGHT_BLUE_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("yellow_stained_glass_stairs", new TransparentStairsBlock(Blocks.YELLOW_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("lime_stained_glass_stairs", new TransparentStairsBlock(Blocks.LIME_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("pink_stained_glass_stairs", new TransparentStairsBlock(Blocks.PINK_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("gray_stained_glass_stairs", new TransparentStairsBlock(Blocks.GRAY_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("light_gray_stained_glass_stairs", new TransparentStairsBlock(Blocks.LIGHT_GRAY_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("cyan_stained_glass_stairs", new TransparentStairsBlock(Blocks.CYAN_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("purple_stained_glass_stairs", new TransparentStairsBlock(Blocks.PURPLE_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("blue_stained_glass_stairs", new TransparentStairsBlock(Blocks.BLUE_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("brown_stained_glass_stairs", new TransparentStairsBlock(Blocks.BROWN_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("green_stained_glass_stairs", new TransparentStairsBlock(Blocks.GREEN_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("red_stained_glass_stairs", new TransparentStairsBlock(Blocks.RED_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("black_stained_glass_stairs", new TransparentStairsBlock(Blocks.BLACK_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));

    this.registerBlock("glass_slab", new TransparentSlabBlock(Blocks.GLASS, BlockRenderLayer.CUTOUT));
    this.registerBlock("white_stained_glass_slab", new TransparentSlabBlock(Blocks.WHITE_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("orange_stained_glass_slab", new TransparentSlabBlock(Blocks.ORANGE_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("magenta_stained_glass_slab", new TransparentSlabBlock(Blocks.MAGENTA_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("light_blue_stained_glass_slab", new TransparentSlabBlock(Blocks.LIGHT_BLUE_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("yellow_stained_glass_slab", new TransparentSlabBlock(Blocks.YELLOW_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("lime_stained_glass_slab", new TransparentSlabBlock(Blocks.LIME_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("pink_stained_glass_slab", new TransparentSlabBlock(Blocks.PINK_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("gray_stained_glass_slab", new TransparentSlabBlock(Blocks.GRAY_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("light_gray_stained_glass_slab", new TransparentSlabBlock(Blocks.LIGHT_GRAY_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("cyan_stained_glass_slab", new TransparentSlabBlock(Blocks.CYAN_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("purple_stained_glass_slab", new TransparentSlabBlock(Blocks.PURPLE_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("blue_stained_glass_slab", new TransparentSlabBlock(Blocks.BLUE_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("brown_stained_glass_slab", new TransparentSlabBlock(Blocks.BROWN_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("green_stained_glass_slab", new TransparentSlabBlock(Blocks.GREEN_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("red_stained_glass_slab", new TransparentSlabBlock(Blocks.RED_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));
    this.registerBlock("black_stained_glass_slab", new TransparentSlabBlock(Blocks.BLACK_STAINED_GLASS, BlockRenderLayer.TRANSLUCENT));

    this.registerBlock("oak_log_stairs", new LogStairsBlock(Blocks.OAK_LOG));
    this.registerBlock("spruce_log_stairs", new LogStairsBlock(Blocks.SPRUCE_LOG));
    this.registerBlock("birch_log_stairs", new LogStairsBlock(Blocks.BIRCH_LOG));
    this.registerBlock("jungle_log_stairs", new LogStairsBlock(Blocks.JUNGLE_LOG));
    this.registerBlock("acacia_log_stairs", new LogStairsBlock(Blocks.ACACIA_LOG));
    this.registerBlock("dark_oak_log_stairs", new LogStairsBlock(Blocks.DARK_OAK_LOG));

    this.registerBlock("oak_wood_stairs", new LogStairsBlock(Blocks.OAK_LOG));
    this.registerBlock("spruce_wood_stairs", new LogStairsBlock(Blocks.SPRUCE_LOG));
    this.registerBlock("birch_wood_stairs", new LogStairsBlock(Blocks.BIRCH_LOG));
    this.registerBlock("jungle_wood_stairs", new LogStairsBlock(Blocks.JUNGLE_LOG));
    this.registerBlock("acacia_wood_stairs", new LogStairsBlock(Blocks.ACACIA_LOG));
    this.registerBlock("dark_oak_wood_stairs", new LogStairsBlock(Blocks.DARK_OAK_LOG));

    this.registerBlock("stripped_oak_log_stairs", new LogStairsBlock(Blocks.STRIPPED_OAK_LOG));
    this.registerBlock("stripped_spruce_log_stairs", new LogStairsBlock(Blocks.STRIPPED_SPRUCE_LOG));
    this.registerBlock("stripped_birch_log_stairs", new LogStairsBlock(Blocks.STRIPPED_BIRCH_LOG));
    this.registerBlock("stripped_jungle_log_stairs", new LogStairsBlock(Blocks.STRIPPED_JUNGLE_LOG));
    this.registerBlock("stripped_acacia_log_stairs", new LogStairsBlock(Blocks.STRIPPED_ACACIA_LOG));
    this.registerBlock("stripped_dark_oak_log_stairs", new LogStairsBlock(Blocks.STRIPPED_DARK_OAK_LOG));

    this.registerBlock("stripped_oak_wood_stairs", new LogStairsBlock(Blocks.STRIPPED_OAK_WOOD));
    this.registerBlock("stripped_spruce_wood_stairs", new LogStairsBlock(Blocks.STRIPPED_SPRUCE_WOOD));
    this.registerBlock("stripped_birch_wood_stairs", new LogStairsBlock(Blocks.STRIPPED_BIRCH_WOOD));
    this.registerBlock("stripped_jungle_wood_stairs", new LogStairsBlock(Blocks.STRIPPED_JUNGLE_WOOD));
    this.registerBlock("stripped_acacia_wood_stairs", new LogStairsBlock(Blocks.STRIPPED_ACACIA_WOOD));
    this.registerBlock("stripped_dark_oak_wood_stairs", new LogStairsBlock(Blocks.STRIPPED_DARK_OAK_WOOD));

    this.registerBlock("oak_log_slab", new LogSlabBlock(Blocks.OAK_LOG));
    this.registerBlock("spruce_log_slab", new LogSlabBlock(Blocks.SPRUCE_LOG));
    this.registerBlock("birch_log_slab", new LogSlabBlock(Blocks.BIRCH_LOG));
    this.registerBlock("jungle_log_slab", new LogSlabBlock(Blocks.JUNGLE_LOG));
    this.registerBlock("acacia_log_slab", new LogSlabBlock(Blocks.ACACIA_LOG));
    this.registerBlock("dark_oak_log_slab", new LogSlabBlock(Blocks.DARK_OAK_LOG));

    this.registerBlock("oak_wood_slab", new LogSlabBlock(Blocks.OAK_LOG));
    this.registerBlock("spruce_wood_slab", new LogSlabBlock(Blocks.SPRUCE_LOG));
    this.registerBlock("birch_wood_slab", new LogSlabBlock(Blocks.BIRCH_LOG));
    this.registerBlock("jungle_wood_slab", new LogSlabBlock(Blocks.JUNGLE_LOG));
    this.registerBlock("acacia_wood_slab", new LogSlabBlock(Blocks.ACACIA_LOG));
    this.registerBlock("dark_oak_wood_slab", new LogSlabBlock(Blocks.DARK_OAK_LOG));

    this.registerBlock("stripped_oak_log_slab", new LogSlabBlock(Blocks.OAK_LOG));
    this.registerBlock("stripped_spruce_log_slab", new LogSlabBlock(Blocks.SPRUCE_LOG));
    this.registerBlock("stripped_birch_log_slab", new LogSlabBlock(Blocks.BIRCH_LOG));
    this.registerBlock("stripped_jungle_log_slab", new LogSlabBlock(Blocks.JUNGLE_LOG));
    this.registerBlock("stripped_acacia_log_slab", new LogSlabBlock(Blocks.ACACIA_LOG));
    this.registerBlock("stripped_dark_oak_log_slab", new LogSlabBlock(Blocks.DARK_OAK_LOG));

    this.registerBlock("stripped_oak_wood_slab", new LogSlabBlock(Blocks.OAK_LOG));
    this.registerBlock("stripped_spruce_wood_slab", new LogSlabBlock(Blocks.SPRUCE_LOG));
    this.registerBlock("stripped_birch_wood_slab", new LogSlabBlock(Blocks.BIRCH_LOG));
    this.registerBlock("stripped_jungle_wood_slab", new LogSlabBlock(Blocks.JUNGLE_LOG));
    this.registerBlock("stripped_acacia_wood_slab", new LogSlabBlock(Blocks.ACACIA_LOG));
    this.registerBlock("stripped_dark_oak_wood_slab", new LogSlabBlock(Blocks.DARK_OAK_LOG));

    this.registerBlock("ice_stairs", new IceStairsBlock(Blocks.ICE));
    this.registerBlock("ice_slab", new IceSlabBlock(Blocks.ICE));

    this.registerBlock("packed_ice_stairs", new SimpleStairsBlock(Blocks.PACKED_ICE));
    this.registerBlock("packed_ice_slab", new SimpleSlabBlock(Blocks.PACKED_ICE));

    this.registerBlock("dirt_stairs", new DirtStairsBlock());
    this.registerBlock("dirt_slab", new DirtSlabBlock());

    this.registerBlock("grass_block_stairs", new GrassStairsBlock());
    this.registerBlock("grass_block_slab", new GrassSlabBlock());

    this.registerBlock("sand_layer", new SandLayerBlock(Blocks.SAND));
    this.registerBlock("red_sand_layer", new SandLayerBlock(Blocks.RED_SAND));

    this.registerBlock("bookshelf_stairs", new BookshelfStairsBlock());
    this.registerBlock("bookshelf_slab", new BookshelfSlabBlock());

    final Stopwatch stopwatchResult = stopwatch.stop();
    final long elapsed = stopwatchResult.elapsed(TimeUnit.MILLISECONDS);

    MintBlocks.LOGGER.info("Block registration completed in {}ms", elapsed);

    this.state = RegistrationState.REGISTERED;
  }

  /**
   * Registers the given block with the given name to the block registry
   * The name is wrapped as a {@link Identifier} with Mint's namespace
   *
   * @param name The name of the block being registered
   * @param block The block to be registered
   */
  private void registerBlock(final String name, final Block block) {
    final Identifier key = Mint.withNamespace(name);

    MintBlocks.LOGGER.debug("Registering block '{}'", key);
    Registry.BLOCK.register(key, block);
  }
}
