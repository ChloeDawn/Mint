package net.insomniakitten.mint.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class IcePillarBlock extends TranslucentPillarBlock implements MeltableBlock {
    public IcePillarBlock(final Block material) {
        super(material);
    }

    @Override
    @Deprecated
    public void tick(final IBlockState state, final World world, final BlockPos position, final Random random) {
        this.checkLightAndMelt(state, world, position);
    }

    @Override
    public int quantityDropped(final IBlockState state, final Random random) {
        return 0;
    }

    @Override
    public void harvestBlock(final World world, final EntityPlayer player, final BlockPos position, final IBlockState state, @Nullable final TileEntity entity, final ItemStack stack) {
        player.addStat(StatList.BLOCK_MINED.get(this));
        player.addExhaustion(0.005F);

        if (this.canSilkHarvest() && 0 < EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack)) {
            Block.spawnAsEntity(world, position, this.getSilkTouchDrop(state));
            return;
        }

        this.onHarvest(world, position, state, stack);
    }

    @Override
    @Deprecated
    public EnumPushReaction getPushReaction(final IBlockState state) {
        return EnumPushReaction.NORMAL;
    }
}
