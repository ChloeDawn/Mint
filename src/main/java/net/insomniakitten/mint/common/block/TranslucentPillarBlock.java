package net.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class TranslucentPillarBlock extends SimplePillarBlock {
  public TranslucentPillarBlock(final Block material) {
    super(material);
  }

  @Override
  public boolean isSimpleFullBlock(final BlockState state, final BlockView view, final BlockPos position) {
    return false;
  }

  @Override
  @Deprecated // isSideInvisible
  public boolean isSideVisible(final BlockState state, final BlockState other, final Direction face) {
    return this == other.getBlock();
  }

  @Override
  public boolean isWaterLogged(final BlockState state, final BlockView view, final BlockPos position) {
    return true;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
}
