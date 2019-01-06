package io.github.insomniakitten.mint.common.mixin;

import io.github.insomniakitten.mint.common.block.SimpleSlabBlock;
import io.github.insomniakitten.mint.common.block.SimpleStairsBlock;
import io.github.insomniakitten.mint.common.block.TransparentSlabBlock;
import io.github.insomniakitten.mint.common.block.TransparentStairsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin class for {@link TransparentBlock}
 *
 * @author InsomniaKitten
 */
@Mixin(TransparentBlock.class)
public final class TransparentBlockMixin {
  /**
   * Determines whether this block is adjacent to transparent slabs and stairs or equal material type,
   * in order to decide whether the method should return early with a `true` value.
   *
   * @reason To allow for culling of blocks against stairs and slabs of equal type
   * @author InsomniaKitten
   */
  @Inject(method = "isSideVisible", at = @At("HEAD"), cancellable = true)
  private void mint$isSideInvisible(final BlockState state, final BlockState other, final Direction side, final CallbackInfoReturnable<Boolean> cir) {
    final Block a = state.getBlock();
    final Block b = other.getBlock();

    if (b instanceof TransparentSlabBlock && a == ((SimpleSlabBlock) b).getMaterial()) {
      if (SlabType.DOUBLE == other.get(Properties.SLAB_TYPE)) {
        cir.setReturnValue(true);
        return;
      }

      if (side.getAxis().isVertical()) {
        if ((Direction.UP == side) == (SlabType.BOTTOM == other.get(Properties.SLAB_TYPE))) {
          cir.setReturnValue(true);
          return;
        }
      }
    }

    if (b instanceof TransparentStairsBlock && a == ((SimpleStairsBlock) b).getMaterial()) {
      if (side.getOpposite() == other.get(Properties.FACING_HORIZONTAL)) {
        cir.setReturnValue(true);
        return;
      }

      if (side.getAxis().isVertical()) {
        if ((Direction.UP == side) == (BlockHalf.BOTTOM == other.get(Properties.BLOCK_HALF))) {
          cir.setReturnValue(true);
        }
      }
    }
  }
}
