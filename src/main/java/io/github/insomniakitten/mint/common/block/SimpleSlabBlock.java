package io.github.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;

public class SimpleSlabBlock extends SlabBlock {
  public SimpleSlabBlock(final Settings settings, final Block material) {
    super(settings);
  }

  public SimpleSlabBlock(final Block material) {
    this(Settings.copy(material), material);
  }
}
