package net.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlacementEnvironment;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

// todo falling

public class SandLayerBlock extends Block implements Waterloggable {
  private static final VoxelShape[] VOXEL_SHAPES = {
    VoxelShapes.empty(),
    Block.createCubeShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
    Block.createCubeShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
    Block.createCubeShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
    Block.createCubeShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
    Block.createCubeShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
    Block.createCubeShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
    Block.createCubeShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
    Block.createCubeShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
  };

  public SandLayerBlock(final Block material) {
    super(Settings.copy(material));
    this.setDefaultState(
      this.getDefaultState()
        .with(Properties.LAYERS, 1)
        .with(Properties.WATERLOGGED, false)
    );
  }

  @Override
  public boolean canFillWithFluid(final BlockView view, final BlockPos position, final BlockState state, final Fluid fluid) {
    return Fluids.WATER == fluid && !state.get(Properties.WATERLOGGED);
  }

  @Override
  public boolean tryFillWithFluid(final IWorld world, final BlockPos position, final BlockState state, final FluidState fluidState) {
    if (Fluids.WATER == fluidState.getFluid() && !state.get(Properties.WATERLOGGED)) {
      if (!world.isClient()) {
        world.setBlockState(position, state.with(Properties.WATERLOGGED, true), 3);
        world.getFluidTickScheduler().schedule(position, Fluids.WATER, Fluids.WATER.method_15789(world));
      }

      return true;
    }

    return false;
  }

  @Override
  public Fluid tryDrainFluid(final IWorld world, final BlockPos position, final BlockState state) {
    if (state.get(Properties.WATERLOGGED)) {
      world.setBlockState(position, state.with(Properties.WATERLOGGED, false), 3);
      return Fluids.WATER;
    }

    return Fluids.EMPTY;
  }

  @Override
  @Deprecated
  public BlockState getStateForNeighborUpdate(final BlockState state, final Direction side, final BlockState neighbor, final IWorld world, final BlockPos position, final BlockPos neighborPosition) {
    if (!state.canPlaceAt(world, position)) {
      return Blocks.AIR.getDefaultState();
    }

    return super.getStateForNeighborUpdate(state, side, neighbor, world, position, neighborPosition);
  }

  @Override //   canPathThrough
  public boolean canPlaceAtSide(final BlockState state, final BlockView view, final BlockPos position, final PlacementEnvironment environment) {
    return PlacementEnvironment.LAND == environment && state.get(Properties.LAYERS) < 5;
  }

  @Override
  @Deprecated
  public boolean method_9616(final BlockState state, final ItemPlacementContext context) {
    final int layers = state.get(Properties.LAYERS);
    final Item item = context.getItemStack().getItem();

    if (item == this.getItem() && 8 > layers) {
      return !context.method_7717() || Direction.UP == context.getFacing();
    }

    return 1 == layers;
  }

  @Override
  @Deprecated
  public VoxelShape getBoundingShape(final BlockState state, final BlockView view, final BlockPos position) {
    return SandLayerBlock.VOXEL_SHAPES[state.get(Properties.LAYERS)];
  }

  @Override
  @Deprecated
  public boolean method_9526(final BlockState state) {
    return true;
  }

  @Override
  @Deprecated
  public boolean canPlaceAt(final BlockState state, final ViewableWorld world, final BlockPos position) {
    final BlockPos down = position.down();
    final BlockState below = world.getBlockState(down);
    final VoxelShape shape = below.getCollisionShape(world, down);

    if (Block.isFaceFullCube(shape, Direction.UP)) {
      return true;
    }

    if (below.matches(BlockTags.LEAVES)) {
      return true;
    }

    if (this == below.getBlock()) {
      return 8 == below.get(Properties.LAYERS);
    }

    return false;
  }

  @Override
  @Nullable
  public BlockState getPlacementState(final ItemPlacementContext context) {
    final World world = context.getWorld();
    final BlockPos position = context.getPos();
    final BlockState state = world.getBlockState(position);

    if (this == state.getBlock()) {
      final int oldLayers = state.get(Properties.LAYERS);
      final int newLayers = Math.min(8, oldLayers + 1);

      return state.with(Properties.LAYERS, newLayers);
    }

    @Nullable final BlockState state1 = super.getPlacementState(context);

    if (state1 != null) {
      final FluidState fluidState = world.getFluidState(position);
      final boolean isWater = Fluids.WATER == fluidState.getFluid();

      return state1.with(Properties.WATERLOGGED, isWater);
    }

    return null;
  }

  @Override
  @Deprecated
  public FluidState getFluidState(final BlockState state) {
    if (state.get(Properties.WATERLOGGED)) {
      return Fluids.WATER.getState(true);
    }

    return super.getFluidState(state);
  }

  @Override
  protected void appendProperties(final StateFactory.Builder<Block, BlockState> builder) {
    builder.with(Properties.LAYERS).with(Properties.WATERLOGGED);
  }
}
