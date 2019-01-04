package net.insomniakitten.mint.common.mixin;

import net.insomniakitten.mint.common.init.MintBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ShovelItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

/**
 * Mixin class for {@link ShovelItem}
 *
 * @author InsomniaKitten
 */
@Mixin(ShovelItem.class)
final class ShovelItemMixin {
  /**
   * Shadowed by {@link ShovelItemMixin}
   */
  @Shadow @Final private static Set<Block> EFFECTIVE_BLOCKS;

  static {
    ShovelItemMixin.mint$appendToEffectiveBlocksSet();
  }

  private ShovelItemMixin() {}

  /**
   * Appends Mint's dirt and grass slabs/stair blocks to {@link ShovelItem#EFFECTIVE_BLOCKS}
   *
   * @author InsomniaKitten
   */
  private static void mint$appendToEffectiveBlocksSet() {
    ShovelItemMixin.EFFECTIVE_BLOCKS.add(MintBlocks.byName("dirt_stairs"));
    ShovelItemMixin.EFFECTIVE_BLOCKS.add(MintBlocks.byName("dirt_slab"));
    ShovelItemMixin.EFFECTIVE_BLOCKS.add(MintBlocks.byName("grass_block_stairs"));
    ShovelItemMixin.EFFECTIVE_BLOCKS.add(MintBlocks.byName("grass_block_slab"));
  }
}
