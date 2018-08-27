package net.insomniakitten.mint.mixin;

import net.insomniakitten.mint.block.SpreadableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirtSnowySpreadable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

/**
 * Mixin class for {@link BlockDirtSnowySpreadable}
 * @author InsomniaKitten
 */
@Mixin(BlockDirtSnowySpreadable.class)
public final class BlockSpreadableMixin implements SpreadableBlock {
    /**
     * Delegates the logic of {@link Block#tick} to {@link SpreadableBlock#onSpreadingTick}
     *
     * @reason To allow for spreading of grass blocks onto grass slabs and stairs
     * @author InsomniaKitten
     */
    @Overwrite
    public void tick(final IBlockState state, final World world, final BlockPos position, final Random random) {
        this.onSpreadingTick(state, world, position, random);
    }
}
