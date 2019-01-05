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
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.SlabType;
import net.minecraft.container.EnchantingTableContainer;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(EnchantingTableContainer.class)
final class EnchantingTableContainerMixin {
  /**
   * @see EnchantingTableContainerMixin
   */
  private final Random mint$random = new Random();

  private EnchantingTableContainerMixin() {}

  /**
   * Redirects the blockstate query for nearby bookshelves to determine the presence of a bookshelf slab
   * block or bookshelves stairs block. There is a 50% chance for a slab to be considered a bookshelf
   * and a 75% chance for stairs to be considered a bookshelf when returning from this redirection.
   *
   * @return The queried blockstate, or a bookshelf blockstate if conditions are met
   * @reason To allow bookshelf slabs and stairs to contribute to the enchantment power
   * @author InsomniaKitten
   */
  @Redirect(method = "onContentChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), require = 6)
  private BlockState mint$onContentsChanged$getBlock(final World world, final BlockPos position) {
    final BlockState state = world.getBlockState(position);
    final Block block = state.getBlock();

    this.mint$random.setSeed(position.asLong());

    if (block instanceof BookshelfSlabBlock) {
      if (SlabType.DOUBLE == state.get(Properties.SLAB_TYPE)) {
        return Blocks.BOOKSHELF.getDefaultState();
      }

      if (0 == this.mint$random.nextInt(2)) {
        return Blocks.BOOKSHELF.getDefaultState();
      }
    }

    if (block instanceof BookshelfStairsBlock) {
      if (0 != this.mint$random.nextInt(4)) {
        return Blocks.BOOKSHELF.getDefaultState();
      }
    }

    return state;
  }
}
