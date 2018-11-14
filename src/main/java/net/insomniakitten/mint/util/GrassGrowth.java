package net.insomniakitten.mint.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.insomniakitten.mint.init.MintBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import javax.annotation.Nullable;

/**
 * Utility class for handling conversion of dirt
 * blocks/slabs/stairs to their grass equivalent
 * and vice versa, utilizing a {@link BiMap}
 * for optimized conversion lookups
 *
 * @author InsomniaKitten
 * @see GrassGrowth#CONVERSION_MAP
 */
public final class GrassGrowth {
    private static final BiMap<Block, Block> CONVERSION_MAP = ImmutableBiMap.of(
        Blocks.DIRT,                      Blocks.GRASS_BLOCK,
        MintBlocks.byName("dirt_stairs"), MintBlocks.byName("grass_block_stairs"),
        MintBlocks.byName("dirt_slab"),   MintBlocks.byName("grass_block_slab")
    );

    private GrassGrowth() {}

    /**
     * Queries the {@link GrassGrowth#CONVERSION_MAP} to determine the
     * grass equivalent of the given {@link IBlockState}
     *
     * @param state The block state to be converted to its grass equivalent
     * @return The conversion result, or `null` if it cannot be converted
     */
    @Nullable
    public static IBlockState getGrassEquivalent(final IBlockState state) {
        @Nullable final Block block = GrassGrowth.CONVERSION_MAP.get(state.getBlock());

        if (block != null) {
            final IBlockState target = block.getDefaultState();
            return BlockStates.copyTo(state, target);
        }

        return null;
    }

    /**
     * Queries the {@link GrassGrowth#CONVERSION_MAP} to determine the
     * dirt equivalent of the given {@link IBlockState}
     *
     * @param state The block state to be converted to its dirt equivalent
     * @return The conversion result, or `null` if it cannot be converted
     */
    @Nullable
    public static IBlockState getDirtEquivalent(final IBlockState state) {
        @Nullable final Block block = GrassGrowth.CONVERSION_MAP.inverse().get(state.getBlock());

        if (block != null) {
            final IBlockState target = block.getDefaultState();
            return BlockStates.copyTo(state, target);
        }

        return null;
    }
}
