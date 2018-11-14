package net.insomniakitten.mint.client.color;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * An {@link IItemColor} implementation that delegates to the {@link Block}
 * parent of an {@link ItemBlock}, and invokes its {@link IBlockColor} from
 * within the given {@link BlockColors} instance
 *
 * @author InsomniaKitten
 */
public final class BlockItemColorMultiplier implements IItemColor {
    private final BlockColors blockColors;

    /**
     * Constructs a new {@link BlockItemColorMultiplier} with the
     * given {@link BlockColors} instance to delegate lookups to
     * @param blockColors The block colors instance to delegate to
     */
    public BlockItemColorMultiplier(final BlockColors blockColors) {
        this.blockColors = blockColors;
    }

    /**
     * Determines the color multiplier of the given {@link ItemStack} for the given tint index
     * Casts the item stack's item to an {@link ItemBlock} and delegates to its block color
     *
     * @param stack The item stack to be cast to {@link ItemBlock}
     * @param tintIndex The tint index to pass as an argument to the item's block color
     * @return The color multiplier of the given item stack's block
     * @throws IllegalStateException If the stack's item is not an {@link ItemBlock}
     */
    @Override
    public int getColor(final ItemStack stack, final int tintIndex) {
        final Item item = stack.getItem();

        if (item instanceof ItemBlock) {
            return this.getBlockColor((ItemBlock) item, tintIndex);
        }

        throw new IllegalStateException("Not a block item: " + item.getClass().getName());
    }

    /**
     * Delegates to the given {@link ItemBlock}'s block and returns its color multiplier
     *
     * @param item The {@link ItemBlock} to retrieve a {@link Block} reference from
     * @param tintIndex The tint index to pass as an argument to the block color multiplier
     * @return The color multiplier of the given item's parent {@link Block}
     */
    private int getBlockColor(final ItemBlock item, final int tintIndex) {
        final Block block = item.getBlock();
        final IBlockState state = block.getDefaultState();

        return this.blockColors.getColor(state, null, null, tintIndex);
    }
}
