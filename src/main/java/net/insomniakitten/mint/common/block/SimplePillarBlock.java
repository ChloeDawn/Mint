package net.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.PillarBlock;

public class SimplePillarBlock extends PillarBlock {
  public SimplePillarBlock(final Block material) {
    super(Settings.copy(material));
  }
}
