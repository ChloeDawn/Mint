package net.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;

public class SimpleSlabBlock extends SlabBlock {
  public SimpleSlabBlock(final Block material) {
    super(Settings.copy(material));
  }
}
