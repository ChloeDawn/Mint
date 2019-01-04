package net.insomniakitten.mint.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public interface MeltableBlock {
  default void checkLightAndMelt(final BlockState state, final World world, final BlockPos position) {
    if (world.getLightLevel(LightType.BLOCK_LIGHT, position) > 11 - state.method_11581(world, position)) {
      this.melt(state, world, position);
    }
  }

  default void harvest(final World world, final BlockPos position) {
    //                       evaporatesWater()
    if (world.getDimension().method_12465()) {
      world.clearBlockState(position);
      return;
    }

    final Material material = world.getBlockState(position.down()).getMaterial();

    //           blocksMotion()
    if (material.suffocates() || material.isLiquid()) {
      world.setBlockState(position, Blocks.WATER.getDefaultState());
    }
  }

  default void melt(final BlockState state, final World world, final BlockPos position) {
    if (world.getDimension().method_12465()) {
      world.clearBlockState(position);
      return;
    }

    world.setBlockState(position, Blocks.WATER.getDefaultState());
    world.updateNeighbor(position, Blocks.WATER, position);
  }
}
