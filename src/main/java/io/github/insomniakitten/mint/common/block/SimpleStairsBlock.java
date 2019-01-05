package io.github.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;

public class SimpleStairsBlock extends StairsBlock {
  public SimpleStairsBlock(final Settings settings, final Block material) {
    super(material.getDefaultState(), settings);
  }

  public SimpleStairsBlock(final Block material) {
    this(Settings.copy(material), material);
  }
}
