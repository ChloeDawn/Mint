package io.github.insomniakitten.mint.common.block;

import net.fabricmc.fabric.block.FabricBlockSettings;
import net.fabricmc.fabric.tags.FabricItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.enums.BlockHalf;
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

public class GrassStairsBlock extends SimpleStairsBlock implements ExtendedSpreadableBlock, Fertilizable {
  public GrassStairsBlock() {
    super(FabricBlockSettings.copy(Blocks.GRASS_BLOCK)
      .breakByTool(FabricItemTags.SHOVELS)
      .build(), Blocks.GRASS_BLOCK);
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.MIPPED_CUTOUT;
  }

  @Override
  public void scheduledTick(final BlockState state, final World world, final BlockPos position, final Random rand) {
    this.spread(state, world, position, rand);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(final ItemPlacementContext context) {
    @Nullable final BlockState state = super.getPlacementState(context);

    if (state != null && BlockHalf.TOP == state.get(Properties.BLOCK_HALF)) {
      final BlockPos up = context.getPos().up();
      final BlockState above = context.getWorld().getBlockState(up);

      return state.with(Properties.SNOWY, this.isSnowBlock(above));
    }

    return state;
  }

  @Override
  public BlockState getStateForNeighborUpdate(final BlockState state, final Direction side, final BlockState neighbor, final IWorld world, final BlockPos position, final BlockPos neighborPosition) {
    if (Direction.UP == side && BlockHalf.TOP == state.get(Properties.BLOCK_HALF)) {
      return state.with(Properties.SNOWY, this.isSnowBlock(neighbor));
    }
    return super.getStateForNeighborUpdate(state, side, neighbor, world, position, neighborPosition);
  }

  @Override
  protected void appendProperties(final StateFactory.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.with(Properties.SNOWY));
  }

  @Override
  public boolean isFertilizable(final BlockView view, final BlockPos position, final BlockState state, final boolean isClient) {
    return BlockHalf.TOP == state.get(Properties.BLOCK_HALF);
  }

  @Override
  public boolean canGrow(final World world, final Random random, final BlockPos position, final BlockState state) {
    return BlockHalf.TOP == state.get(Properties.BLOCK_HALF);
  }

  @Override
  public void grow(final World world, final Random random, final BlockPos position, final BlockState state) {
    if (BlockHalf.TOP == state.get(Properties.BLOCK_HALF)) {
      this.grow(world, position, random);
    }
  }

  private boolean isSnowBlock(final BlockState state) {
    final Block block = state.getBlock();

    return Blocks.SNOW_BLOCK == block || Blocks.SNOW == block;
  }
}
