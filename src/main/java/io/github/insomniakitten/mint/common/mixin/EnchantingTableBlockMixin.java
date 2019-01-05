/*
 * Copyright (C) 2018 InsomniaKitten
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.insomniakitten.mint.common.mixin;

import io.github.insomniakitten.mint.common.block.BookshelfSlabBlock;
import io.github.insomniakitten.mint.common.block.BookshelfStairsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(EnchantingTableBlock.class)
final class EnchantingTableBlockMixin {
  private EnchantingTableBlockMixin() {}

  /**
   * Injects a blockstate query alongside the existing query to determine the presence of a
   * bookshelf slab block or bookshelves stairs block. If a slab or stairs are found, particles
   * will be spawned in the same fashion as they are spawned for a bookshelf block
   *
   * @reason To allow bookshelf slabs and stairs to generate enchant particles
   * @author InsomniaKitten
   */
  @Inject(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), locals = LocalCapture.CAPTURE_FAILHARD)
  private void mint$randomDisplayTick(
    final BlockState state,
    final World world,
    final BlockPos position,
    final Random random,
    final CallbackInfo ci,
    final int x,
    final int z,
    final int y,
    final BlockPos offsetPosition
  ) {
    final Block block = world.getBlockState(offsetPosition).getBlock();
    if (block instanceof BookshelfSlabBlock || block instanceof BookshelfStairsBlock) {
      if (world.isAir(position.add(x / 2, 0, z / 2))) {
        world.addParticle(
          ParticleTypes.ENCHANT,
          (double) position.getX() + 0.5,
          (double) position.getY() + 2.0,
          (double) position.getZ() + 0.5,
          (double) ((float) x + random.nextFloat()) - 0.5,
          (double) ((float) y - random.nextFloat() - 1.0F),
          (double) ((float) z + random.nextFloat()) - 0.5
        );
      }
    }
  }
}
