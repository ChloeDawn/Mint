package net.insomniakitten.mint.mixin;

import net.insomniakitten.mint.block.IceSlabBlock;
import net.insomniakitten.mint.block.IceStairsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin class for {@link BlockBreakable}
 * @author InsomniaKitten
 */
@Mixin(BlockBreakable.class)
public final class BlockBreakableMixin {
    /**
     * Determines if the block this method is invoked on is an ice block, and whether it is adjacent
     * to other ice blocks, slabs, and stairs, in order to decide whether the method should return
     * early with a `true` value.
     *
     * @reason To allow for culling of ice blocks against ice stairs and slabs
     * @author InsomniaKitten
     */
    @Inject(method = "isSideInvisible", at = @At("HEAD"), cancellable = true)
    private void returnTrueIfIceIsAgainstIce(final IBlockState state, final IBlockState other, final EnumFacing face, final CallbackInfoReturnable<Boolean> cir) {
        if (Blocks.ICE != state.getBlock()) {
            return;
        }

        final Block block = other.getBlock();

        if (block instanceof IceSlabBlock) {
            if (face.getAxis().isVertical()) {
                cir.setReturnValue(true);
                return;
            }

            if (SlabType.DOUBLE == other.get(BlockStateProperties.SLAB_TYPE)) {
                cir.setReturnValue(true);
                return;
            }
        }

        if (block instanceof IceStairsBlock) {
            if (face.getAxis().isVertical()) {
                cir.setReturnValue(true);
                return;
            }

            if (face.getOpposite() == other.get(BlockStateProperties.HORIZONTAL_FACING)) {
                cir.setReturnValue(true);
            }
        }
    }
}
