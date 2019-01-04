package net.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class IcePillarBlock extends TranslucentPillarBlock implements MeltableBlock {
  public IcePillarBlock(final Block material) {
    super(material);
  }

  @Override
  @Deprecated
  public void scheduledTick(final BlockState state, final World world, final BlockPos position, final Random random) {
    this.checkLightAndMelt(state, world, position);
  }

  @Override
  public void afterBreak(final World world, final PlayerEntity player, final BlockPos pos, final BlockState state, @Nullable final BlockEntity entity, final ItemStack stack) {
    super.afterBreak(world, player, pos, state, entity, stack);
    this.harvest(world, pos);
  }

  @Override
  @Deprecated
  public PistonBehavior getPistonBehavior(final BlockState state) {
    return PistonBehavior.NORMAL;
  }

  @Override
  @Deprecated
  public boolean isSideVisible(final BlockState state, final BlockState other, final Direction side) {
    return Blocks.ICE == other.getBlock() || super.isSideVisible(state, other, side);
  }
}
