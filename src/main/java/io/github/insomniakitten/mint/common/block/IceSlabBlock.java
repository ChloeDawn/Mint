package io.github.insomniakitten.mint.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class IceSlabBlock extends TransparentSlabBlock implements MeltableBlock {
  public IceSlabBlock(final Block material) {
    super(material, BlockRenderLayer.TRANSLUCENT);
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
}
