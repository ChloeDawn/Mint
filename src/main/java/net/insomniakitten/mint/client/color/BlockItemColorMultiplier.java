package net.insomniakitten.mint.client.color;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockColorMap;
import net.minecraft.client.render.block.BlockColorMapper;
import net.minecraft.client.render.item.ItemColorMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Lazy;

import javax.annotation.Nullable;

/**
 * An {@link ItemColorMapper} implementation that delegates to the {@link Block}
 * parent of an {@link BlockItem}, and invokes its {@link BlockColorMapper}
 *
 * @author InsomniaKitten
 */
public final class BlockItemColorMultiplier implements ItemColorMapper {
  private static final Lazy<BlockColorMap> BLOCK_COLOR_MAP = new Lazy<>(() ->
    MinecraftClient.getInstance().getBlockColorMap()
  );

  /**
   * Determines the color multiplier of the given {@link ItemStack} for the given tint index
   * Casts the item stack's item to an {@link BlockItem} and delegates to its block color
   *
   * @param stack The item stack to be cast to {@link BlockItem}
   * @param tintIndex The tint index to pass as an argument to the item's block color
   * @return The color multiplier of the given item stack's block
   * @throws IllegalStateException If the stack's item is not an {@link BlockItem}
   */
  @Override
  public int getColor(final ItemStack stack, final int tintIndex) {
    final Item item = stack.getItem();

    Preconditions.checkState(item instanceof BlockItem, "Not a BlockItem: %s", item.getClass().getName());

    return this.getBlockColor((BlockItem) item, tintIndex);
  }

  /**
   * Delegates to the given {@link BlockItem}'s block and returns its color multiplier
   *
   * @param item The {@link BlockItem} to retrieve a {@link Block} reference from
   * @param tintIndex The tint index to pass as an argument to the block color multiplier
   * @return The color multiplier of the given item's parent {@link Block}
   */
  private int getBlockColor(final BlockItem item, final int tintIndex) {
    final Block block = item.getBlock();
    final BlockState state = block.getDefaultState();

    return this.getBlockColorMap().getRenderColor(state, null, null, tintIndex);
  }

  /**
   * Retrieves the client's {@link BlockColorMap} lazily, utilizing
   * a {@link Lazy} supplying {@link MinecraftClient#getBlockColorMap()}
   *
   * @return The client's {@link BlockColorMap}
   */
  private BlockColorMap getBlockColorMap() {
    @Nullable final BlockColorMap map = BlockItemColorMultiplier.BLOCK_COLOR_MAP.get();

    Preconditions.checkState(map != null, "BlockColorMap not initialized");

    return map;
  }
}
