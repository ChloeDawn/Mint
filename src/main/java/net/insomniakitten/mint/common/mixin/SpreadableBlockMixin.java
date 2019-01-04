package net.insomniakitten.mint.common.mixin;

import net.insomniakitten.mint.common.block.ExtendedSpreadableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

// TODO Multiple injects to replace immediately cancelled callback in head

/**
 * Mixin class for {@link SpreadableBlock}
 *
 * @author InsomniaKitten
 */
@Mixin(SpreadableBlock.class)
public final class SpreadableBlockMixin implements ExtendedSpreadableBlock {
  /**
   * Delegates the logic of {@link Block#scheduledTick} to {@link ExtendedSpreadableBlock#spread}
   *
   * @reason To allow for spreading of grass blocks onto grass slabs and stairs
   * @author InsomniaKitten
   */
  @Inject(method = "scheduledTick", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
  public void mint$scheduledTick(final BlockState state, final World world, final BlockPos position, final Random random, final CallbackInfo ci) {
    this.spread(state, world, position, random);
    ci.cancel();
  }
}
