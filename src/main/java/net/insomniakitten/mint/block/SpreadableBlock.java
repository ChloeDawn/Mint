package net.insomniakitten.mint.block;

import net.insomniakitten.mint.util.BlockStates;
import net.insomniakitten.mint.util.GrassGrowth;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.CompositeFlowerFeature;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public interface SpreadableBlock {
    default boolean isInsufficientlyLit(final IWorldReaderBase reader, final BlockPos position) {
        final BlockPos up = position.up();

        if (4 <= reader.getLight(up)) {
            return false;
        }

        final IBlockState above = reader.getBlockState(up);

        return above.getOpacity(reader, up) >= reader.getMaxLightLevel();
    }

    default boolean doesBlockSpreading(final IWorldReaderBase reader, final BlockPos position) {
        final BlockPos up = position.up();

        if (4 > reader.getLight(up)) {
            return true;
        }

        final IBlockState above = reader.getBlockState(up);

        if (above.getOpacity(reader, up) >= reader.getMaxLightLevel()) {
            return true;
        }

        return reader.getFluidState(up).isTagged(FluidTags.WATER);
    }

    default void onSpreadingTick(final IBlockState state, final World world, final BlockPos position, final Random random) {
        if (world.isRemote) {
            return;
        }

        if (this.isInsufficientlyLit(world, position)) {
            @Nullable final IBlockState dirt = GrassGrowth.getDirtEquivalent(state);

            if (dirt != null) {
                world.setBlockState(position, dirt);
            }

            return;
        }

        if (world.getLight(position.up()) < 9) {
            return;
        }

        for (int attempt = 0; attempt < 4; ++attempt) {
            final int x = random.nextInt(3) - 1;
            final int y = random.nextInt(5) - 3;
            final int z = random.nextInt(3) - 1;

            final BlockPos offset = position.add(x, y, z);

            if (!world.isBlockPresent(offset)) {
                return;
            }

            if (this.doesBlockSpreading(world, offset)) {
                continue;
            }

            @Nullable final IBlockState grass = GrassGrowth.getGrassEquivalent(world.getBlockState(offset));

            if (grass != null) {
                world.setBlockState(offset, BlockStates.copyTo(state, grass));
            }
        }
    }

    default void onGrow(final World world, final BlockPos position, final Random random) {
        if (world.isRemote()) {
            return;
        }

        final BlockPos up = position.up();
        final IBlockState grass = Blocks.GRASS.getDefaultState();

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

                if (world.getBlockState(offset).isBlockNormalCube()) {
                    continue areaScan;
                }
            }

            if (world.getBlockState(offset).isAir()) {
                IBlockState flower = grass;

                if (random.nextInt(8) == 0) {
                    final List<CompositeFlowerFeature<?>> flowers = world.getBiome(offset).getFlowers();

                    if (flowers.isEmpty()) {
                        continue;
                    }

                    flower = flowers.get(0).getRandomFlower(random, offset);
                }
                if (flower.isValidPosition(world, offset)) {
                    world.setBlockState(offset, flower, 3);
                }
            }
        }
    }
}
