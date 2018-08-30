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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Mixin class for {@link ItemAxe}
 *
 * @author InsomniaKitten
 */
@Mixin(ItemAxe.class)
@SuppressWarnings("ProtectedField")
public final class ItemAxeMixin {
    private static final Map<Block, Block> MINT_BLOCK_STRIPPING_MAP = ImmutableMap.<Block, Block>builder()
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

    /**
     * Looks up a value in {@link ItemAxeMixin#MINT_BLOCK_STRIPPING_MAP} if the value from the
     * prior lookup in {@link ItemAxe#field_203176_a} returns `null`
     *
     * @param map The original map in {@link ItemAxe}
     * @param key The key being looked up in the map
     * @return A value from the original map, or one from Mint's map if the value was null
     * @author InsomniaKitten
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    @Redirect(method = "onItemUse", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object getValueOrMintValueIfNull(final Map map, final Object key) {
        @Nullable val value = map.get(key);

        return value == null ? ItemAxeMixin.MINT_BLOCK_STRIPPING_MAP.get(key) : value;
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
