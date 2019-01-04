package net.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.BlockView;

import javax.annotation.Nullable;

public class RotatedStairsBlock extends StairsBlock {
  private final Block material;

  public RotatedStairsBlock(final Settings settings, final Block material) {
    super(material.getDefaultState(), settings);
    this.material = material;
  }

  public RotatedStairsBlock(final Block material) {
    this(Settings.copy(material), material);
  }

  @Override
  @Deprecated
  public MaterialColor getMaterialColor(final BlockState state, final BlockView view, final BlockPos position) {
    final BlockState materialState = this.material.getDefaultState()
      .with(Properties.AXIS_XYZ, state.get(Properties.AXIS_XYZ));

    return this.material.getMaterialColor(materialState, view, position);
  }

  @Override
  @Nullable
  public BlockState getPlacementState(final ItemPlacementContext context) {
    @Nullable final BlockState state = super.getPlacementState(context);

    return state == null ? null : state.with(Properties.AXIS_XYZ, context.getFacing().getAxis());
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
}
