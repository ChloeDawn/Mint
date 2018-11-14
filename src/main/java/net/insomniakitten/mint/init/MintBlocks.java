package net.insomniakitten.mint.init;

import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import net.insomniakitten.mint.Mint;
import net.insomniakitten.mint.block.GrassSlabBlock;
import net.insomniakitten.mint.block.GrassStairsBlock;
import net.insomniakitten.mint.block.IcePillarBlock;
import net.insomniakitten.mint.block.IceSlabBlock;
import net.insomniakitten.mint.block.IceStairsBlock;
import net.insomniakitten.mint.block.RotatedSlabBlock;
import net.insomniakitten.mint.block.RotatedStairsBlock;
import net.insomniakitten.mint.block.SandLayerBlock;
import net.insomniakitten.mint.block.SimpleSlabBlock;
import net.insomniakitten.mint.block.SimpleStairsBlock;
import net.insomniakitten.mint.block.TranslucentSlabBlock;
import net.insomniakitten.mint.block.TranslucentStairsBlock;
import net.insomniakitten.mint.util.state.RegistrationState;
import net.insomniakitten.pylon.annotation.rift.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import org.apache.logging.log4j.Logger;
import org.dimdev.rift.listener.BlockAdder;

import javax.annotation.Nullable;
import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Listener class for Mint's block registration and lookups.
 * Handles registration of Mint's blocks, and allows for retrieving
 * the instance or name of registered Mint blocks
 *
 * @author InsomniaKitten
 */
@Listener(priority = 1)
public final class MintBlocks implements BlockAdder {
    private static final MintBlocks INSTANCE = new MintBlocks();

    private static final Logger LOGGER = Mint.getLogger("blocks");

    static {
        Mint.setInstanceForLoader(MintBlocks.class, MintBlocks.INSTANCE);
    }

    private volatile RegistrationState state = RegistrationState.initial();

    private MintBlocks() {}

    /**
     * Retrieves an {@link Block} for the given name from the block registry
     * The given name is wrapped as a {@link ResourceLocation} with Mint's namespace,
     * and passed to {@link RegistryNamespacedDefaultedByKey#get(Object)}.
     * If blocks have not yet been registered, an exception will be thrown
     *
     * @param name The name of the block to be retrieved
     * @return The block, or {@link Blocks#AIR} if not found
     */
    public static Block byName(final String name) {
        if (MintBlocks.INSTANCE.state.isUnregistered()) {
            throw new UnsupportedOperationException("Blocks not registered");
        }

        if (MintBlocks.INSTANCE.state.isRegistering()) {
            throw new ConcurrentModificationException("Blocks still registering");
        }

        final ResourceLocation key = Mint.withNamespace(name);
        @Nullable final Block block = Block.REGISTRY.get(key);

        return Objects.requireNonNull(block, "Block '" + key + "'");
    }

    /**
     * Retrieves the name of the given {@link Block} from the block registry
     * If blocks have not yet been registered, an exception will be thrown
     *
     * @param block The block to retrieve the name of
     * @return The name of the block
     */
    public static ResourceLocation getName(final Block block) {
        if (MintBlocks.INSTANCE.state.isUnregistered()) {
            throw new UnsupportedOperationException("Blocks not registered");
        }

        if (MintBlocks.INSTANCE.state.isRegistering()) {
            throw new ConcurrentModificationException("Blocks still registering");
        }

        return Objects.requireNonNull(Block.REGISTRY.getKey(block), "name");
    }

    /**
     * Registers all of Mint's blocks to the block registry. If this is called whilst already
     * executing, or if registration has been completed, an exception will be thrown.
     */
    @Override
    public void registerBlocks() {
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

        this.registerBlock("white_stained_glass_stairs", new TranslucentStairsBlock(Blocks.WHITE_STAINED_GLASS));
        this.registerBlock("orange_stained_glass_stairs", new TranslucentStairsBlock(Blocks.ORANGE_STAINED_GLASS));
        this.registerBlock("magenta_stained_glass_stairs", new TranslucentStairsBlock(Blocks.MAGENTA_STAINED_GLASS));
        this.registerBlock("light_blue_stained_glass_stairs", new TranslucentStairsBlock(Blocks.LIGHT_BLUE_STAINED_GLASS));
        this.registerBlock("yellow_stained_glass_stairs", new TranslucentStairsBlock(Blocks.YELLOW_STAINED_GLASS));
        this.registerBlock("lime_stained_glass_stairs", new TranslucentStairsBlock(Blocks.LIME_STAINED_GLASS));
        this.registerBlock("pink_stained_glass_stairs", new TranslucentStairsBlock(Blocks.PINK_STAINED_GLASS));
        this.registerBlock("gray_stained_glass_stairs", new TranslucentStairsBlock(Blocks.GRAY_STAINED_GLASS));
        this.registerBlock("light_gray_stained_glass_stairs", new TranslucentStairsBlock(Blocks.LIGHT_GRAY_STAINED_GLASS));
        this.registerBlock("cyan_stained_glass_stairs", new TranslucentStairsBlock(Blocks.CYAN_STAINED_GLASS));
        this.registerBlock("purple_stained_glass_stairs", new TranslucentStairsBlock(Blocks.PURPLE_STAINED_GLASS));
        this.registerBlock("blue_stained_glass_stairs", new TranslucentStairsBlock(Blocks.BLUE_STAINED_GLASS));
        this.registerBlock("brown_stained_glass_stairs", new TranslucentStairsBlock(Blocks.BROWN_STAINED_GLASS));
        this.registerBlock("green_stained_glass_stairs", new TranslucentStairsBlock(Blocks.GREEN_STAINED_GLASS));
        this.registerBlock("red_stained_glass_stairs", new TranslucentStairsBlock(Blocks.RED_STAINED_GLASS));
        this.registerBlock("black_stained_glass_stairs", new TranslucentStairsBlock(Blocks.BLACK_STAINED_GLASS));

        this.registerBlock("white_stained_glass_slab", new TranslucentSlabBlock(Blocks.WHITE_STAINED_GLASS));
        this.registerBlock("orange_stained_glass_slab", new TranslucentSlabBlock(Blocks.ORANGE_STAINED_GLASS));
        this.registerBlock("magenta_stained_glass_slab", new TranslucentSlabBlock(Blocks.MAGENTA_STAINED_GLASS));
        this.registerBlock("light_blue_stained_glass_slab", new TranslucentSlabBlock(Blocks.LIGHT_BLUE_STAINED_GLASS));
        this.registerBlock("yellow_stained_glass_slab", new TranslucentSlabBlock(Blocks.YELLOW_STAINED_GLASS));
        this.registerBlock("lime_stained_glass_slab", new TranslucentSlabBlock(Blocks.LIME_STAINED_GLASS));
        this.registerBlock("pink_stained_glass_slab", new TranslucentSlabBlock(Blocks.PINK_STAINED_GLASS));
        this.registerBlock("gray_stained_glass_slab", new TranslucentSlabBlock(Blocks.GRAY_STAINED_GLASS));
        this.registerBlock("light_gray_stained_glass_slab", new TranslucentSlabBlock(Blocks.LIGHT_GRAY_STAINED_GLASS));
        this.registerBlock("cyan_stained_glass_slab", new TranslucentSlabBlock(Blocks.CYAN_STAINED_GLASS));
        this.registerBlock("purple_stained_glass_slab", new TranslucentSlabBlock(Blocks.PURPLE_STAINED_GLASS));
        this.registerBlock("blue_stained_glass_slab", new TranslucentSlabBlock(Blocks.BLUE_STAINED_GLASS));
        this.registerBlock("brown_stained_glass_slab", new TranslucentSlabBlock(Blocks.BROWN_STAINED_GLASS));
        this.registerBlock("green_stained_glass_slab", new TranslucentSlabBlock(Blocks.GREEN_STAINED_GLASS));
        this.registerBlock("red_stained_glass_slab", new TranslucentSlabBlock(Blocks.RED_STAINED_GLASS));
        this.registerBlock("black_stained_glass_slab", new TranslucentSlabBlock(Blocks.BLACK_STAINED_GLASS));

        this.registerBlock("oak_log_stairs", new RotatedStairsBlock(Blocks.OAK_LOG));
        this.registerBlock("spruce_log_stairs", new RotatedStairsBlock(Blocks.SPRUCE_LOG));
        this.registerBlock("birch_log_stairs", new RotatedStairsBlock(Blocks.BIRCH_LOG));
        this.registerBlock("jungle_log_stairs", new RotatedStairsBlock(Blocks.JUNGLE_LOG));
        this.registerBlock("acacia_log_stairs", new RotatedStairsBlock(Blocks.ACACIA_LOG));
        this.registerBlock("dark_oak_log_stairs", new RotatedStairsBlock(Blocks.DARK_OAK_LOG));

        this.registerBlock("oak_wood_stairs", new RotatedStairsBlock(Blocks.OAK_LOG));
        this.registerBlock("spruce_wood_stairs", new RotatedStairsBlock(Blocks.SPRUCE_LOG));
        this.registerBlock("birch_wood_stairs", new RotatedStairsBlock(Blocks.BIRCH_LOG));
        this.registerBlock("jungle_wood_stairs", new RotatedStairsBlock(Blocks.JUNGLE_LOG));
        this.registerBlock("acacia_wood_stairs", new RotatedStairsBlock(Blocks.ACACIA_LOG));
        this.registerBlock("dark_oak_wood_stairs", new RotatedStairsBlock(Blocks.DARK_OAK_LOG));

        this.registerBlock("stripped_oak_log_stairs", new RotatedStairsBlock(Blocks.STRIPPED_OAK_LOG));
        this.registerBlock("stripped_spruce_log_stairs", new RotatedStairsBlock(Blocks.STRIPPED_SPRUCE_LOG));
        this.registerBlock("stripped_birch_log_stairs", new RotatedStairsBlock(Blocks.STRIPPED_BIRCH_LOG));
        this.registerBlock("stripped_jungle_log_stairs", new RotatedStairsBlock(Blocks.STRIPPED_JUNGLE_LOG));
        this.registerBlock("stripped_acacia_log_stairs", new RotatedStairsBlock(Blocks.STRIPPED_ACACIA_LOG));
        this.registerBlock("stripped_dark_oak_log_stairs", new RotatedStairsBlock(Blocks.STRIPPED_DARK_OAK_LOG));

        this.registerBlock("stripped_oak_wood_stairs", new RotatedStairsBlock(Blocks.STRIPPED_OAK_WOOD));
        this.registerBlock("stripped_spruce_wood_stairs", new RotatedStairsBlock(Blocks.STRIPPED_SPRUCE_WOOD));
        this.registerBlock("stripped_birch_wood_stairs", new RotatedStairsBlock(Blocks.STRIPPED_BIRCH_WOOD));
        this.registerBlock("stripped_jungle_wood_stairs", new RotatedStairsBlock(Blocks.STRIPPED_JUNGLE_WOOD));
        this.registerBlock("stripped_acacia_wood_stairs", new RotatedStairsBlock(Blocks.STRIPPED_ACACIA_WOOD));
        this.registerBlock("stripped_dark_oak_wood_stairs", new RotatedStairsBlock(Blocks.STRIPPED_DARK_OAK_WOOD));

        this.registerBlock("oak_log_slab", new RotatedSlabBlock(Blocks.OAK_LOG));
        this.registerBlock("spruce_log_slab", new RotatedSlabBlock(Blocks.SPRUCE_LOG));
        this.registerBlock("birch_log_slab", new RotatedSlabBlock(Blocks.BIRCH_LOG));
        this.registerBlock("jungle_log_slab", new RotatedSlabBlock(Blocks.JUNGLE_LOG));
        this.registerBlock("acacia_log_slab", new RotatedSlabBlock(Blocks.ACACIA_LOG));
        this.registerBlock("dark_oak_log_slab", new RotatedSlabBlock(Blocks.DARK_OAK_LOG));

        this.registerBlock("oak_wood_slab", new RotatedSlabBlock(Blocks.OAK_LOG));
        this.registerBlock("spruce_wood_slab", new RotatedSlabBlock(Blocks.SPRUCE_LOG));
        this.registerBlock("birch_wood_slab", new RotatedSlabBlock(Blocks.BIRCH_LOG));
        this.registerBlock("jungle_wood_slab", new RotatedSlabBlock(Blocks.JUNGLE_LOG));
        this.registerBlock("acacia_wood_slab", new RotatedSlabBlock(Blocks.ACACIA_LOG));
        this.registerBlock("dark_oak_wood_slab", new RotatedSlabBlock(Blocks.DARK_OAK_LOG));

        this.registerBlock("stripped_oak_log_slab", new RotatedSlabBlock(Blocks.OAK_LOG));
        this.registerBlock("stripped_spruce_log_slab", new RotatedSlabBlock(Blocks.SPRUCE_LOG));
        this.registerBlock("stripped_birch_log_slab", new RotatedSlabBlock(Blocks.BIRCH_LOG));
        this.registerBlock("stripped_jungle_log_slab", new RotatedSlabBlock(Blocks.JUNGLE_LOG));
        this.registerBlock("stripped_acacia_log_slab", new RotatedSlabBlock(Blocks.ACACIA_LOG));
        this.registerBlock("stripped_dark_oak_log_slab", new RotatedSlabBlock(Blocks.DARK_OAK_LOG));

        this.registerBlock("stripped_oak_wood_slab", new RotatedSlabBlock(Blocks.OAK_LOG));
        this.registerBlock("stripped_spruce_wood_slab", new RotatedSlabBlock(Blocks.SPRUCE_LOG));
        this.registerBlock("stripped_birch_wood_slab", new RotatedSlabBlock(Blocks.BIRCH_LOG));
        this.registerBlock("stripped_jungle_wood_slab", new RotatedSlabBlock(Blocks.JUNGLE_LOG));
        this.registerBlock("stripped_acacia_wood_slab", new RotatedSlabBlock(Blocks.ACACIA_LOG));
        this.registerBlock("stripped_dark_oak_wood_slab", new RotatedSlabBlock(Blocks.DARK_OAK_LOG));

        this.registerBlock("ice_pillar", new IcePillarBlock(Blocks.ICE));
        this.registerBlock("ice_stairs", new IceStairsBlock(Blocks.ICE));
        this.registerBlock("ice_slab", new IceSlabBlock(Blocks.ICE));

        final Block iceBricks = new BlockIce(Block.Builder.from(Blocks.ICE));

        this.registerBlock("ice_bricks", iceBricks);
        this.registerBlock("ice_brick_stairs", new IceStairsBlock(iceBricks));
        this.registerBlock("ice_brick_slab", new IceSlabBlock(iceBricks));

        this.registerBlock("red_nether_brick_stairs", new SimpleStairsBlock(Blocks.RED_NETHER_BRICKS));
        this.registerBlock("red_nether_brick_slab", new SimpleSlabBlock(Blocks.RED_NETHER_BRICKS));

        this.registerBlock("dirt_stairs", new SimpleStairsBlock(Blocks.DIRT));
        this.registerBlock("dirt_slab", new SimpleSlabBlock(Blocks.DIRT));

        this.registerBlock("grass_block_stairs", new GrassStairsBlock());
        this.registerBlock("grass_block_slab", new GrassSlabBlock());

        this.registerBlock("sand_layer", new SandLayerBlock(Blocks.SAND));
        this.registerBlock("red_sand_layer", new SandLayerBlock(Blocks.RED_SAND));

        final Stopwatch stopwatchResult = stopwatch.stop();
        final long elapsed = stopwatchResult.elapsed(TimeUnit.MILLISECONDS);

        MintBlocks.LOGGER.info("Block registration completed in {}ms", elapsed);

        this.state = RegistrationState.REGISTERED;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("state", this.state).toString();
    }

    /**
     * Registers the given block with the given name to the block registry
     * The name is wrapped as a {@link ResourceLocation} with Mint's namespace
     *
     * @param name The name of the block being registered
     * @param block The block to be registered
     */
    private void registerBlock(final String name, final Block block) {
        final ResourceLocation key = Mint.withNamespace(name);

        MintBlocks.LOGGER.debug("Registering block '{}'", key);
        Block.register(key, block);
    }
}
