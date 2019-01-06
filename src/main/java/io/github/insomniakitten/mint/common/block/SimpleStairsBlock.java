package io.github.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;

public class SimpleStairsBlock extends StairsBlock {
  private final Block material;

  public SimpleStairsBlock(final Settings settings, final Block material) {
    super(material.getDefaultState(), settings);
    this.material = material;
  }

  public SimpleStairsBlock(final Block material) {
    this(Settings.copy(material), material);
  }

  public final Block getMaterial() {
    return this.material;
  }
}
