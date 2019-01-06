package io.github.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;

public class SimpleSlabBlock extends SlabBlock implements MaterialMimickingBlock {
  private final Block material;

  public SimpleSlabBlock(final Settings settings, final Block material) {
    super(settings);
    this.material = material;
  }

  public SimpleSlabBlock(final Block material) {
    this(Settings.copy(material), material);
  }

  @Override
  public final Block getMimickedMaterial() {
    return this.material;
  }
}
