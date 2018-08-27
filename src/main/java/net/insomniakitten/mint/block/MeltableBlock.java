package net.insomniakitten.mint.block;

import lombok.val;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumLightType;
import net.minecraft.world.World;

public interface MeltableBlock {
    default void checkLightAndMelt(final IBlockState state, final World world, final BlockPos position) {
        if (world.getLightFor(EnumLightType.BLOCK, position) > 11 - state.getOpacity(world, position)) {
            this.onMelt(state, world, position);
        }
    }

    default void onHarvest(final World world, final BlockPos position, final IBlockState state, final ItemStack stack) {
        if (world.getDimension().doesWaterVaporize()) {
            world.removeBlock(position);
            return;
        }

        state.dropBlockAsItem(world, position, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));

        val material = world.getBlockState(position.down()).getMaterial();

        if (material.blocksMovement() || material.isLiquid()) {
            world.setBlockState(position, Blocks.WATER.getDefaultState());
        }
    }

    default void onMelt(final IBlockState state, final World world, final BlockPos position) {
        if (world.getDimension().doesWaterVaporize()) {
            world.removeBlock(position);
            return;
        }

        state.dropBlockAsItem(world, position, 0);
        world.setBlockState(position, Blocks.WATER.getDefaultState());
        world.neighborChanged(position, Blocks.WATER, position);
    }
}
