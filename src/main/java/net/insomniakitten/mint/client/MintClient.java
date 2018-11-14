package net.insomniakitten.mint.client;

import com.google.common.base.MoreObjects;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.insomniakitten.mint.Mint;
import net.insomniakitten.mint.client.color.BlockItemColorMultiplier;
import net.insomniakitten.mint.client.color.GrassBlockColorMultiplier;
import net.insomniakitten.mint.init.MintBlocks;
import net.insomniakitten.mint.init.MintItems;
import net.insomniakitten.mint.util.FieldLookupException;
import net.insomniakitten.mint.util.obf.Mapping;
import net.insomniakitten.mint.util.state.RegistrationState;
import net.insomniakitten.pylon.annotation.rift.Listener;
import net.insomniakitten.pylon.ref.Side;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import org.dimdev.rift.listener.client.TileEntityRendererAdder;
import org.dimdev.rift.mixin.hook.client.MixinTileEntityRendererDispatcher;

import javax.annotation.Nullable;
import java.util.ConcurrentModificationException;
import java.util.Map;

// FIXME Rift needs a listener specifically for block and item color multiplier registration
// FIXME Rift needs a hook or patch to expose the ItemColors instance in the Minecraft class

/**
 * Listener class for Mint's client implementation.
 * Handles registration of color multipliers for blocks and items
 *
 * @author InsomniaKitten
 */
@Listener(priority = 1, side = Side.CLIENT)
@Log4j2(topic = Mint.ID + ".client")
public final class MintClient implements TileEntityRendererAdder {
    private static final MintClient INSTANCE = new MintClient();

    static {
        Mint.setInstanceForLoader(MintClient.class, MintClient.INSTANCE);
    }

    private final Mapping itemColors = Mapping.builder()
        .notch("ay").srg("field_184128_aI").mcp("itemColors").build();

    private volatile RegistrationState state = RegistrationState.initial();

    private MintClient() {}

    /**
     * Called by Rift's {@link TileEntityRendererDispatcher} mixin
     * Delegates to {@link MintClient#registerColorMultipliers()}
     * This is a temporary workaround until Rift implements a listener
     * type specifically for block and item color multiplier registration
     *
     * @see MixinTileEntityRendererDispatcher
     */
    @Override
    public void addTileEntityRenderers(final Map<Class<? extends TileEntity>, TileEntityRenderer<? extends TileEntity>> renderers) {
        this.registerColorMultipliers();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("state", this.state).toString();
    }

    /**
     * Looks up the game's {@link BlockColors} and {@link ItemColors} instances
     * and registers color multipliers to them for Mint's blocks and items
     */
    private void registerColorMultipliers() {
        if (this.state.isRegistered()) {
            throw new UnsupportedOperationException("Already registered");
        }

        if (this.state.isRegistering()) {
            throw new ConcurrentModificationException("Already registering");
        }

        this.state = RegistrationState.REGISTERING;

        @Nullable val blockColors = Minecraft.getInstance().getBlockColors();
        @Nullable val itemColors = this.getItemColorsInstanceReflectively();

        if (blockColors == null) {
            throw new IllegalStateException("BlockColors not initialized");
        }

        if (itemColors == null) {
            throw new IllegalStateException("ItemColors not initialized");
        }

        MintClient.LOGGER.info("Registering block color multipliers");

        val grassColor = new GrassBlockColorMultiplier(0.5, 1.0);

        this.registerColorMultiplier(blockColors, grassColor, MintBlocks.byName("grass_block_stairs"));
        this.registerColorMultiplier(blockColors, grassColor, MintBlocks.byName("grass_block_slab"));

        MintClient.LOGGER.info("Registering item color multipliers");

        val blockColor = new BlockItemColorMultiplier(blockColors);

        this.registerColorMultiplier(itemColors, blockColor, MintItems.byName("grass_block_stairs"));
        this.registerColorMultiplier(itemColors, blockColor, MintItems.byName("grass_block_slab"));

        this.state = RegistrationState.REGISTERED;
    }

    /**
     * Registers a color multiplier to the given {@link BlockColors} instance
     *
     * @param blockColors The block colors instance
     * @param blockColor The block color multiplier to register
     * @param block The block to register the color multiplier for
     */
    private void registerColorMultiplier(final BlockColors blockColors, final IBlockColor blockColor, final Block block) {
        MintClient.LOGGER.debug("Registering color multiplier {} for {}", blockColor, MintBlocks.getName(block));
        blockColors.register(blockColor, block);
    }

    /**
     * Registers a color multiplier to the given {@link ItemColors} instance
     *
     * @param itemColors The item colors instance
     * @param itemColor The item color multiplier to register
     * @param item The item to register the color multiplier for
     */
    private void registerColorMultiplier(final ItemColors itemColors, final IItemColor itemColor, final Item item) {
        MintClient.LOGGER.debug("Registering color multiplier {} for {}", itemColor, MintItems.getName(item));
        itemColors.register(itemColor, item);
    }

    /**
     * Reflects into {@link Minecraft} to retrieve the value of {@link Minecraft#itemColors}
     * This is a temporary workaround until Rift adds a hook for retrieving the instance
     * without the requirement of reflection or an access transformer
     *
     * @return The game's {@link ItemColors} instance, or `null` if the field value is `null`
     */
    @Nullable
    private ItemColors getItemColorsInstanceReflectively() {
        try {
            val fieldName = this.itemColors.getValue();
            val fieldReference = Minecraft.class.getDeclaredField(fieldName);

            fieldReference.setAccessible(true);

            return (ItemColors) fieldReference.get(Minecraft.getInstance());
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new FieldLookupException(this.itemColors.getValue(), e);
        }
    }
}
