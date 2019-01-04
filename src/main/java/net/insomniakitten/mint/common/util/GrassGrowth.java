package net.insomniakitten.mint.common.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.insomniakitten.mint.common.init.MintBootstrap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

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
    MintBootstrap.getBlock("dirt_stairs"), MintBootstrap.getBlock("grass_block_stairs"),
    MintBootstrap.getBlock("dirt_slab"),   MintBootstrap.getBlock("grass_block_slab")
  );

  private GrassGrowth() {}

  /**
   * Queries the {@link GrassGrowth#CONVERSION_MAP} to determine the
   * grass equivalent of the given {@link BlockState}
   *
   * @param state The block state to be converted to its grass equivalent
   * @return The conversion result, or `null` if it cannot be converted
   */
  @Nullable
  public static BlockState getGrassEquivalent(final BlockState state) {
    @Nullable final Block block = GrassGrowth.CONVERSION_MAP.get(state.getBlock());

    if (block != null) {
      final BlockState target = block.getDefaultState();
      return BlockStates.copyTo(state, target);
    }

    return null;
  }

  /**
   * Queries the {@link GrassGrowth#CONVERSION_MAP} to determine the
   * dirt equivalent of the given {@link BlockState}
   *
   * @param state The block state to be converted to its dirt equivalent
   * @return The conversion result, or `null` if it cannot be converted
   */
  @Nullable
  public static BlockState getDirtEquivalent(final BlockState state) {
    @Nullable final Block block = GrassGrowth.CONVERSION_MAP.inverse().get(state.getBlock());

    if (block != null) {
      final BlockState target = block.getDefaultState();
      return BlockStates.copyTo(state, target);
    }

    return null;
  }
}
