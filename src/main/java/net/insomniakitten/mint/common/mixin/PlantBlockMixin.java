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

package net.insomniakitten.mint.common.mixin;

import net.insomniakitten.mint.common.block.GrassSlabBlock;
import net.insomniakitten.mint.common.block.GrassStairsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlantBlock.class)
final class PlantBlockMixin {
  private PlantBlockMixin() {}

  @Inject(method = "canPlantOnTop", at = @At("HEAD"), cancellable = true)
  private void mint$canPlantOnTop(final BlockState state, final BlockView view, final BlockPos position, final CallbackInfoReturnable<Boolean> cir) {
    final Block block = state.getBlock();

    if (block instanceof GrassSlabBlock) {
      cir.setReturnValue(SlabType.BOTTOM != state.get(Properties.SLAB_TYPE));
    } else if (block instanceof GrassStairsBlock) {
      cir.setReturnValue(BlockHalf.TOP == state.get(Properties.BLOCK_HALF));
    }
  }
}
