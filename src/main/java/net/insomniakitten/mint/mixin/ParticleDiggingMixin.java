package net.insomniakitten.mint.mixin;

import net.insomniakitten.mint.block.GrassSlabBlock;
import net.insomniakitten.mint.block.GrassStairsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleDigging;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin class for {@link ParticleDigging}
 *
 * @author InsomniaKitten
 */
@Mixin(ParticleDigging.class)
public final class ParticleDiggingMixin {
    /**
     * Shadowed by {@link ParticleDiggingMixin}
     */
    @Shadow
    @Final
    private IBlockState sourceState;

    /**
     * Determines if the {@link ParticleDigging#sourceState} is a grass slab or stair block, and
     * prevents color multipliers being applied to the particle color. This follows the hard-coded
     * implementation that exists for vanilla grass blocks
     *
     * @reason To prevent color multiplication of particles generated for grass slabs and stairs
     * @author InsomniaKitten
     */
    @Inject(method = "multiplyColor", at = @At("HEAD"), cancellable = true)
    private void returnIfSourceIsGrassSlabOrStairs(final CallbackInfo ci) {
        final Block block = this.sourceState.getBlock();

        if (block instanceof GrassSlabBlock || block instanceof GrassStairsBlock) {
            ci.cancel();
        }
    }
}
