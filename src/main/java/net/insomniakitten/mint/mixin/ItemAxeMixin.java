package net.insomniakitten.mint.mixin;

import com.google.common.collect.ImmutableMap;
import lombok.val;
import net.insomniakitten.mint.block.RotatedSlabBlock;
import net.insomniakitten.mint.block.RotatedStairsBlock;
import net.insomniakitten.mint.init.MintBlocks;
import net.insomniakitten.mint.util.BlockStates;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemAxe;
import net.minecraft.state.IStateHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

/**
 * Mixin class for {@link ItemAxe}
 *
 * @author InsomniaKitten
 */
@Mixin(ItemAxe.class)
@SuppressWarnings("ProtectedField")
public final class ItemAxeMixin {
    /**
     * Field shadowed and de-finalized by {@link ItemAxeMixin}
     */
    @Shadow
    @Final
    @Mutable
    protected static Map<Block, Block> field_203176_a;

    static {
        ItemAxeMixin.field_203176_a = ItemAxeMixin.rewriteBlockStrippingMap(ItemAxeMixin.field_203176_a);
    }

    /**
     * Rebuilds the immutable map of blocks that axes can strip from log to wood, appending
     * new blocks obtained through callbacks to {@link MintBlocks#byName(String)}
     *
     * @param oldMap The original map to be re-appended to the {@link ImmutableMap.Builder}
     * @return A new {@link ImmutableMap} containing the old map plus the new entries
     * @author InsomniaKitten
     */
    private static ImmutableMap<Block, Block> rewriteBlockStrippingMap(final Map<Block, Block> oldMap) {
        return ImmutableMap.<Block, Block>builder().putAll(oldMap)
            .put(MintBlocks.byName("oak_log_stairs"),       MintBlocks.byName("stripped_oak_log_stairs"))
            .put(MintBlocks.byName("spruce_log_stairs"),    MintBlocks.byName("stripped_spruce_log_stairs"))
            .put(MintBlocks.byName("birch_log_stairs"),     MintBlocks.byName("stripped_birch_log_stairs"))
            .put(MintBlocks.byName("jungle_log_stairs"),    MintBlocks.byName("stripped_jungle_log_stairs"))
            .put(MintBlocks.byName("acacia_log_stairs"),    MintBlocks.byName("stripped_acacia_log_stairs"))
            .put(MintBlocks.byName("dark_oak_log_stairs"),  MintBlocks.byName("stripped_dark_oak_log_stairs"))
            .put(MintBlocks.byName("oak_wood_stairs"),      MintBlocks.byName("stripped_oak_wood_stairs"))
            .put(MintBlocks.byName("spruce_wood_stairs"),   MintBlocks.byName("stripped_spruce_wood_stairs"))
            .put(MintBlocks.byName("birch_wood_stairs"),    MintBlocks.byName("stripped_birch_wood_stairs"))
            .put(MintBlocks.byName("jungle_wood_stairs"),   MintBlocks.byName("stripped_jungle_wood_stairs"))
            .put(MintBlocks.byName("acacia_wood_stairs"),   MintBlocks.byName("stripped_acacia_wood_stairs"))
            .put(MintBlocks.byName("dark_oak_wood_stairs"), MintBlocks.byName("stripped_dark_oak_wood_stairs"))
            .put(MintBlocks.byName("oak_log_slab"),         MintBlocks.byName("stripped_oak_log_slab"))
            .put(MintBlocks.byName("spruce_log_slab"),      MintBlocks.byName("stripped_spruce_log_slab"))
            .put(MintBlocks.byName("birch_log_slab"),       MintBlocks.byName("stripped_birch_log_slab"))
            .put(MintBlocks.byName("jungle_log_slab"),      MintBlocks.byName("stripped_jungle_log_slab"))
            .put(MintBlocks.byName("acacia_log_slab"),      MintBlocks.byName("stripped_acacia_log_slab"))
            .put(MintBlocks.byName("dark_oak_log_slab"),    MintBlocks.byName("stripped_dark_oak_log_slab"))
            .put(MintBlocks.byName("oak_wood_slab"),        MintBlocks.byName("stripped_oak_wood_slab"))
            .put(MintBlocks.byName("spruce_wood_slab"),     MintBlocks.byName("stripped_spruce_wood_slab"))
            .put(MintBlocks.byName("birch_wood_slab"),      MintBlocks.byName("stripped_birch_wood_slab"))
            .put(MintBlocks.byName("jungle_wood_slab"),     MintBlocks.byName("stripped_jungle_wood_slab"))
            .put(MintBlocks.byName("acacia_wood_slab"),     MintBlocks.byName("stripped_acacia_wood_slab"))
            .put(MintBlocks.byName("dark_oak_wood_slab"),   MintBlocks.byName("stripped_dark_oak_wood_slab"))
            .build();
    }

    /**
     * Ensures that all properties of the original log block are copied over to the new wood block
     * when axes run a map lookup and attempt to update the blockstate in the world with the new block
     *
     * @author InsomniaKitten
     * @see BlockStates#copyTo(IStateHolder, IBlockState)
     */
    @Redirect(method = "onItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z"))
    private boolean cloneAndSetBlockState(final World world, final BlockPos position, final IBlockState state, final int flags) {
        val original = world.getBlockState(position);
        val block = original.getBlock();

        // Re-patched: Now skipping "unsupported" blocks to avoid potential bugs
        if (block instanceof RotatedStairsBlock || block instanceof RotatedSlabBlock) {
            return world.setBlockState(position, BlockStates.copyTo(original, state), flags);
        }

        return world.setBlockState(position, state, flags);
    }
}
