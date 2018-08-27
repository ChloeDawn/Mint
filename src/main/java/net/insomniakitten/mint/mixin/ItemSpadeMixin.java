package net.insomniakitten.mint.mixin;

import net.insomniakitten.mint.init.MintBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSpade;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

/**
 * Mixin class for {@link ItemSpade}
 * @author InsomniaKitten
 */
@Mixin(ItemSpade.class)
public final class ItemSpadeMixin {
    /**
     * Shadowed by {@link ItemSpadeMixin}
     */
    @Shadow
    @Final
    private static Set<Block> EFFECTIVE_ON;

    static {
        ItemSpadeMixin.appendToEffectiveBlocksSet();
    }

    /**
     * Appends Mint's dirt and grass slabs/stair blocks to {@link ItemSpade#EFFECTIVE_ON}
     *
     * @author InsomniaKitten
     */
    private static void appendToEffectiveBlocksSet() {
        ItemSpadeMixin.EFFECTIVE_ON.add(MintBlocks.byName("dirt_stairs"));
        ItemSpadeMixin.EFFECTIVE_ON.add(MintBlocks.byName("dirt_slab"));
        ItemSpadeMixin.EFFECTIVE_ON.add(MintBlocks.byName("grass_block_stairs"));
        ItemSpadeMixin.EFFECTIVE_ON.add(MintBlocks.byName("grass_block_slab"));
    }
}
