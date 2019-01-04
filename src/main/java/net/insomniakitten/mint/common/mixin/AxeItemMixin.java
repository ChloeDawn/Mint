package net.insomniakitten.mint.common.mixin;

import com.google.common.collect.ImmutableMap;
import net.insomniakitten.mint.common.block.RotatedSlabBlock;
import net.insomniakitten.mint.common.block.RotatedStairsBlock;
import net.insomniakitten.mint.common.init.MintBootstrap;
import net.insomniakitten.mint.common.util.BlockStates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.AxeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Mixin class for {@link AxeItem}
 *
 * @author InsomniaKitten
 */
@Mixin(AxeItem.class)
final class AxeItemMixin {
  private static final Map<Block, Block> MINT_BLOCK_STRIPPING_MAP = ImmutableMap.<Block, Block>builder()
    .put(MintBootstrap.getBlock("oak_log_stairs"),       MintBootstrap.getBlock("stripped_oak_log_stairs"))
    .put(MintBootstrap.getBlock("spruce_log_stairs"),    MintBootstrap.getBlock("stripped_spruce_log_stairs"))
    .put(MintBootstrap.getBlock("birch_log_stairs"),     MintBootstrap.getBlock("stripped_birch_log_stairs"))
    .put(MintBootstrap.getBlock("jungle_log_stairs"),    MintBootstrap.getBlock("stripped_jungle_log_stairs"))
    .put(MintBootstrap.getBlock("acacia_log_stairs"),    MintBootstrap.getBlock("stripped_acacia_log_stairs"))
    .put(MintBootstrap.getBlock("dark_oak_log_stairs"),  MintBootstrap.getBlock("stripped_dark_oak_log_stairs"))
    .put(MintBootstrap.getBlock("oak_wood_stairs"),      MintBootstrap.getBlock("stripped_oak_wood_stairs"))
    .put(MintBootstrap.getBlock("spruce_wood_stairs"),   MintBootstrap.getBlock("stripped_spruce_wood_stairs"))
    .put(MintBootstrap.getBlock("birch_wood_stairs"),    MintBootstrap.getBlock("stripped_birch_wood_stairs"))
    .put(MintBootstrap.getBlock("jungle_wood_stairs"),   MintBootstrap.getBlock("stripped_jungle_wood_stairs"))
    .put(MintBootstrap.getBlock("acacia_wood_stairs"),   MintBootstrap.getBlock("stripped_acacia_wood_stairs"))
    .put(MintBootstrap.getBlock("dark_oak_wood_stairs"), MintBootstrap.getBlock("stripped_dark_oak_wood_stairs"))
    .put(MintBootstrap.getBlock("oak_log_slab"),         MintBootstrap.getBlock("stripped_oak_log_slab"))
    .put(MintBootstrap.getBlock("spruce_log_slab"),      MintBootstrap.getBlock("stripped_spruce_log_slab"))
    .put(MintBootstrap.getBlock("birch_log_slab"),       MintBootstrap.getBlock("stripped_birch_log_slab"))
    .put(MintBootstrap.getBlock("jungle_log_slab"),      MintBootstrap.getBlock("stripped_jungle_log_slab"))
    .put(MintBootstrap.getBlock("acacia_log_slab"),      MintBootstrap.getBlock("stripped_acacia_log_slab"))
    .put(MintBootstrap.getBlock("dark_oak_log_slab"),    MintBootstrap.getBlock("stripped_dark_oak_log_slab"))
    .put(MintBootstrap.getBlock("oak_wood_slab"),        MintBootstrap.getBlock("stripped_oak_wood_slab"))
    .put(MintBootstrap.getBlock("spruce_wood_slab"),     MintBootstrap.getBlock("stripped_spruce_wood_slab"))
    .put(MintBootstrap.getBlock("birch_wood_slab"),      MintBootstrap.getBlock("stripped_birch_wood_slab"))
    .put(MintBootstrap.getBlock("jungle_wood_slab"),     MintBootstrap.getBlock("stripped_jungle_wood_slab"))
    .put(MintBootstrap.getBlock("acacia_wood_slab"),     MintBootstrap.getBlock("stripped_acacia_wood_slab"))
    .put(MintBootstrap.getBlock("dark_oak_wood_slab"),   MintBootstrap.getBlock("stripped_dark_oak_wood_slab"))
    .build();

  private AxeItemMixin() {}

  /**
   * Looks up a value in {@link AxeItemMixin#MINT_BLOCK_STRIPPING_MAP} if the value from the
   * prior lookup in {@link AxeItem#BLOCK_TRANSFORMATIONS_MAP} returns `null`
   *
   * @param map The original map in {@link AxeItem}
   * @param key The key being looked up in the map
   * @return A value from the original map, or one from Mint's map if the value was null
   * @author InsomniaKitten
   */
  @SuppressWarnings("SuspiciousMethodCalls")
  @Redirect(method = "useOnBlock", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
  private Object getValueOrMintValueIfNull(final Map map, final Object key) {
    @Nullable final Object value = map.get(key);

    return value == null ? AxeItemMixin.MINT_BLOCK_STRIPPING_MAP.get(key) : value;
  }

  /**
   * Ensures that all properties of the original log block are copied over to the new wood block
   * when axes run a map lookup and attempt to update the blockstate in the world with the new block
   *
   * @author InsomniaKitten
   * @see BlockStates#copyTo(BlockState, BlockState)
   */
  @Redirect(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
  private boolean cloneAndSetBlockState(final World world, final BlockPos position, final BlockState state, final int flags) {
    final BlockState original = world.getBlockState(position);
    final Block block = original.getBlock();

    if (block instanceof RotatedStairsBlock || block instanceof RotatedSlabBlock) {
      return world.setBlockState(position, BlockStates.copyTo(original, state), flags);
    }

    return world.setBlockState(position, state, flags);
  }
}
