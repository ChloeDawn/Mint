package net.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;

public class SimpleStairsBlock extends StairsBlock {
  public SimpleStairsBlock(final Block material) {
    super(material.getDefaultState(), Settings.copy(material));
  }
}
