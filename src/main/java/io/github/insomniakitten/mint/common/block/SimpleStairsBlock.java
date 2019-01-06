package io.github.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;

public class SimpleStairsBlock extends StairsBlock implements MaterialMimickingBlock {
  private final Block material;

  public SimpleStairsBlock(final Settings settings, final Block material) {
    super(material.getDefaultState(), settings);
    this.material = material;
  }

  public SimpleStairsBlock(final Block material) {
    this(Settings.copy(material), material);
  }

  @Override
  public final Block getMimickedMaterial() {
    return this.material;
  }
}
