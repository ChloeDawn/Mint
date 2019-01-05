package io.github.insomniakitten.mint.common.mixin;

import io.github.insomniakitten.mint.common.block.IceSlabBlock;
import io.github.insomniakitten.mint.common.block.IceStairsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TransparentBlock;
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
   * Determines if the block this method is invoked on is an ice block, and whether it is adjacent
   * to other ice blocks, slabs, and stairs, in order to decide whether the method should return
   * early with a `true` value.
   *
   * @reason To allow for culling of ice blocks against ice stairs and slabs
   * @author InsomniaKitten
   */
  @Inject(method = "isSideVisible", at = @At("HEAD"), cancellable = true)
  private void mint$isSideInvisible(final BlockState state, final BlockState other, final Direction side, final CallbackInfoReturnable<Boolean> cir) {
    if (Blocks.ICE != state.getBlock()) {
      return;
    }

    final Block block = other.getBlock();

    if (block instanceof IceSlabBlock) {
      if (side.getAxis().isVertical()) {
        cir.setReturnValue(true);
        return;
      }

      if (SlabType.DOUBLE == other.get(Properties.SLAB_TYPE)) {
        cir.setReturnValue(true);
        return;
      }
    }

    if (block instanceof IceStairsBlock) {
      if (side.getAxis().isVertical()) {
        cir.setReturnValue(true);
        return;
      }

      if (side.getOpposite() == other.get(Properties.FACING_HORIZONTAL)) {
        cir.setReturnValue(true);
      }
    }
  }
}
