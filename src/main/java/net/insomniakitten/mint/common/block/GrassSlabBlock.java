package net.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.enums.SlabType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class GrassSlabBlock extends SimpleSlabBlock implements ExtendedSpreadableBlock, Fertilizable {
  public GrassSlabBlock() {
    super(Blocks.GRASS_BLOCK);
    this.setDefaultState(
      this.getDefaultState()
        .with(Properties.SNOWY, false)
    );
  }

  @Override
  @Deprecated
  public boolean isFullBoundsCubeForCulling(final BlockState state) {
    return SlabType.DOUBLE == state.get(Properties.SLAB_TYPE);
  }

  @Override
  @Deprecated
  public void scheduledTick(final BlockState state, final World world, final BlockPos position, final Random rand) {
    this.spread(state, world, position, rand);
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.MIPPED_CUTOUT;
  }

  @Override
  public boolean isFertilizable(final BlockView view, final BlockPos position, final BlockState state, final boolean isClient) {
    return SlabType.BOTTOM != state.get(Properties.SLAB_TYPE) && view.getBlockState(position.up()).isAir();
  }

  @Override
  public boolean canGrow(final World world, final Random random, final BlockPos position, final BlockState state) {
    return SlabType.BOTTOM != state.get(Properties.SLAB_TYPE);
  }

  @Override
  public void grow(final World world, final Random random, final BlockPos position, final BlockState state) {
    if (SlabType.BOTTOM != state.get(Properties.SLAB_TYPE)) {
      this.grow(world, position, random);
    }
  }

  @Override
  protected void appendProperties(final StateFactory.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.with(Properties.SNOWY));
  }

  @Override
  public boolean hasSolidTopSurface(final BlockState state, final BlockView view, final BlockPos position) {
    return SlabType.BOTTOM != state.get(Properties.SLAB_TYPE);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(final ItemPlacementContext context) {
    @Nullable final BlockState state = super.getPlacementState(context);

    if (state != null && SlabType.BOTTOM != state.get(Properties.SLAB_TYPE)) {
      final BlockPos up = context.getPos().up();
      final BlockState above = context.getWorld().getBlockState(up);

      return state.with(Properties.SNOWY, this.isSnowBlock(above));
    }

    return state;
  }

  @Override
  public BlockState getStateForNeighborUpdate(final BlockState state, final Direction side, final BlockState neighbor, final IWorld world, final BlockPos position, final BlockPos neighborPosition) {
    if (Direction.UP == side && SlabType.BOTTOM != state.get(Properties.SLAB_TYPE)) {
      return state.with(Properties.SNOWY, this.isSnowBlock(neighbor));
    }
    return super.getStateForNeighborUpdate(state, side, neighbor, world, position, neighborPosition);
  }

  private boolean isSnowBlock(final BlockState state) {
    final Block block = state.getBlock();

    return Blocks.SNOW_BLOCK == block || Blocks.SNOW == block;
  }
}
