package net.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RotatedSlabBlock extends SimpleSlabBlock {
  private final Block material;

  public RotatedSlabBlock(final Settings settings, final Block material) {
    super(settings, material);
    this.material = material;
  }

  public RotatedSlabBlock(final Block material) {
    super(material);
    this.material = material;
  }

  @Override
  @Deprecated
  public MaterialColor getMaterialColor(final BlockState state, final BlockView view, final BlockPos position) {
    final BlockState materialState = this.material.getDefaultState()
      .with(Properties.AXIS_XYZ, state.get(Properties.AXIS_XYZ));

    return this.material.getMaterialColor(materialState, view, position);
  }

  @Override
  @Deprecated
  public BlockState applyRotation(final BlockState state, final Rotation rotation) {
    if (Rotation.ROT_270 == rotation || Rotation.ROT_90 == rotation) {
      final Axis axis = state.get(Properties.AXIS_XYZ);

      if (axis.isHorizontal()) {
        return state.with(Properties.AXIS_XYZ, Axis.X == axis ? Axis.Z : Axis.X);
      }
    }

    return state;
  }

  @Override
  protected void appendProperties(final StateFactory.Builder<Block, BlockState> builder) {
    super.appendProperties(builder.with(Properties.AXIS_XYZ));
  }

  @Override
  @Nullable
  public BlockState getPlacementState(final ItemPlacementContext context) {
    @Nullable final BlockState state = super.getPlacementState(context);

    if (state != null) {
      final World world = context.getWorld();
      final BlockPos position = context.getPos();
      final BlockState currentState = world.getBlockState(position);
      final boolean same = this == currentState.getBlock();
      final Axis axis = same ? currentState.get(Properties.AXIS_XYZ) : context.getFacing().getAxis();

      return state.with(Properties.AXIS_XYZ, axis);
    }

    return null;
  }
}
