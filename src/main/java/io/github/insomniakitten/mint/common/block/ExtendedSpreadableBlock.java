package io.github.insomniakitten.mint.common.block;

import io.github.insomniakitten.mint.common.util.BlockStates;
import io.github.insomniakitten.mint.common.util.GrassGrowth;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FlowerFeature;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@Deprecated
public interface ExtendedSpreadableBlock {
  default boolean isInsufficientlyLit(final World world, final BlockPos position) {
    final BlockPos up = position.up();

    if (4 <= world.method_8602(up)) {
      return false;
    }

    final BlockState above = world.getBlockState(up);

    return above.method_11581(world, up) >= world.getMaxLightLevel();
  }

  default boolean doesBlockSpreading(final World world, final BlockPos position) {
    final BlockPos up = position.up();

    if (4 > world.method_8602(up)) {
      return true;
    }

    final BlockState above = world.getBlockState(up);

    if (above.method_11581(world, up) >= world.getMaxLightLevel()) {
      return true;
    }

    return FluidTags.WATER.contains(world.getFluidState(up).getFluid());
  }

  default void spread(final BlockState state, final World world, final BlockPos position, final Random random) {
    if (world.isClient()) {
      return;
    }

    if (this.isInsufficientlyLit(world, position)) {
      @Nullable final BlockState dirt = GrassGrowth.getDirtEquivalent(state);

      if (dirt != null) {
        world.setBlockState(position, dirt);
      }

      return;
    }

    if (world.method_8602(position.up()) < 9) {
      return;
    }

    for (int attempt = 0; attempt < 4; ++attempt) {
      final int x = random.nextInt(3) - 1;
      final int y = random.nextInt(5) - 3;
      final int z = random.nextInt(3) - 1;

      final BlockPos offset = position.add(x, y, z);

      if (!world.isAir(offset)) {
        return;
      }

      if (this.doesBlockSpreading(world, offset)) {
        continue;
      }

      @Nullable final BlockState grass = GrassGrowth.getGrassEquivalent(world.getBlockState(offset));

      if (grass != null) {
        world.setBlockState(offset, BlockStates.copyTo(state, grass));
      }
    }
  }

  default void grow(final World world, final BlockPos position, final Random random) {
    if (world.isClient()) {
      return;
    }

    final BlockPos up = position.up();
    final BlockState grass = Blocks.GRASS.getDefaultState();

    areaScan:
    for (int range = 0; range < 128; ++range) {
      BlockPos offset = up;

      for (int step = 0; step < range / 16; ++step) {
        final int x = random.nextInt(3) - 1;
        final int y = (random.nextInt(3) - 1) * random.nextInt(3) / 2;
        final int z = random.nextInt(3) - 1;

        offset = offset.add(x, y, z);

        if (world.getBlockState(offset.down()).getBlock() != this) {
          continue areaScan;
        }

        if (world.getBlockState(offset).blocksLight(world, offset)) {
          continue areaScan;
        }
      }

      if (world.getBlockState(offset).isAir()) {
        BlockState flower = grass;

        if (random.nextInt(8) == 0) {
          final List<ConfiguredFeature<?>> flowers = world.getBiome(offset).getFlowerFeatures();

          if (flowers.isEmpty()) {
            continue;
          }

          final DecoratedFeatureConfig config = (DecoratedFeatureConfig) flowers.get(0).config;
          final FlowerFeature feature = (FlowerFeature) config.feature.feature;

          flower = feature.method_13175(random, offset);
        }
        if (flower.canPlaceAt(world, offset)) {
          world.setBlockState(offset, flower, 3);
        }
      }
    }
  }
}
