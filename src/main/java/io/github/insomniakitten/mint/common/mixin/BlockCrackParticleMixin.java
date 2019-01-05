package io.github.insomniakitten.mint.common.mixin;

import io.github.insomniakitten.mint.common.block.GrassSlabBlock;
import io.github.insomniakitten.mint.common.block.GrassStairsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.BlockCrackParticle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin class for {@link BlockCrackParticle}
 *
 * @author InsomniaKitten
 */
@Mixin(BlockCrackParticle.class)
final class BlockCrackParticleMixin {
  @Shadow @Final private BlockState block;

  private BlockCrackParticleMixin() {}

  /**
   * Determines if the {@link BlockCrackParticle#block} is a grass slab or stair block, and
   * prevents color multipliers being applied to the particle color. This follows the hard-coded
   * implementation that exists for vanilla grass blocks
   *
   * @reason To prevent color multiplication of particles generated for grass slabs and stairs
   * @author InsomniaKitten
   */
  @Inject(method = "updateColor", at = @At("HEAD"), cancellable = true)
  private void mint$updateColor(final CallbackInfo ci) {
    final Block block = this.block.getBlock();

    if (block instanceof GrassSlabBlock || block instanceof GrassStairsBlock) {
      ci.cancel();
    }
  }
}
