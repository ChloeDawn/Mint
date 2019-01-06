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

package io.github.insomniakitten.mint.client.mixin;

import io.github.insomniakitten.mint.common.block.TransparentStairsBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockState.class)
final class BlockStateMixin {
  private BlockStateMixin() {}

  @Inject(method = "getCullShape", at = @At("HEAD"), cancellable = true)
  private void mint$getCullShape(
    final BlockView view,
    final BlockPos position,
    final Direction side,
    final CallbackInfoReturnable<VoxelShape> cir
  ) {
    final BlockState self = BlockState.class.cast(this);
    if (self.getBlock() instanceof TransparentStairsBlock) {
      final BlockState other = view.getBlockState(position.offset(side));
      if (self.getBlock() != other.getBlock()) {
        cir.setReturnValue(VoxelShapes.empty());
      }
    }
  }
}
