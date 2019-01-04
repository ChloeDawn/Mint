package net.insomniakitten.mint.client;

import com.google.common.base.MoreObjects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.insomniakitten.mint.client.color.BlockItemColorMultiplier;
import net.insomniakitten.mint.client.color.GrassBlockColorMultiplier;
import net.insomniakitten.mint.common.Mint;
import net.insomniakitten.mint.common.init.MintBlocks;
import net.insomniakitten.mint.common.init.MintItems;
import net.insomniakitten.mint.common.util.state.RegistrationState;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockColorMap;
import net.minecraft.client.render.block.BlockColorMapper;
import net.minecraft.client.render.item.ItemColorMap;
import net.minecraft.client.render.item.ItemColorMapper;
import net.minecraft.item.Item;
import org.apache.logging.log4j.Logger;

import java.util.ConcurrentModificationException;

// FIXME Rift needs a listener specifically for block and item color multiplier registration
// FIXME Rift needs a hook or patch to expose the ItemColors instance in the Minecraft class

/**
 * Listener class for Mint's client implementation.
 * Handles registration of color multipliers for blocks and items
 *
 * @author InsomniaKitten
 */
public final class MintClient implements ClientModInitializer {
  private static final MintClient INSTANCE = new MintClient();

  private static final Logger LOGGER = Mint.getLogger("client");

  static {
    Mint.setInstanceForLoader(MintClient.class, MintClient.INSTANCE);
    MintClient.INSTANCE.registerColorMultipliers(); // todo
  }

  private volatile RegistrationState state = RegistrationState.initial();

  public MintClient() {} // todo

  @Override
  public void onInitializeClient() {

  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("state", this.state).toString();
  }

  /**
   * Looks up the game's {@link BlockColorMap} and {@link ItemColorMap} instances
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

    MintClient.LOGGER.info("Registering block color multipliers");

    final BlockColorMapper grassColor = new GrassBlockColorMultiplier(0.5, 1.0);

    this.registerColorMultiplier(grassColor, MintBlocks.byName("grass_block_stairs"));
    this.registerColorMultiplier(grassColor, MintBlocks.byName("grass_block_slab"));

    MintClient.LOGGER.info("Registering item color multipliers");

    final ItemColorMapper blockColor = new BlockItemColorMultiplier();

    this.registerColorMultiplier(blockColor, MintItems.byName("grass_block_stairs"));
    this.registerColorMultiplier(blockColor, MintItems.byName("grass_block_slab"));

    this.state = RegistrationState.REGISTERED;
  }

  /**
   * Registers a color multiplier to the given {@link BlockColorMap}
   *
   * @param mapper The block color multiplier to register
   * @param block The block to register the color multiplier for
   */
  private void registerColorMultiplier(final BlockColorMapper mapper, final Block block) {
    MintClient.LOGGER.debug("Registering color multiplier {} for {}", mapper, MintBlocks.getName(block));
    ColorProviderRegistry.BLOCK.register(mapper, block);
  }

  /**
   * Registers a color multiplier to the given {@link ItemColorMapper}
   *
   * @param mapper The item color multiplier to register
   * @param item The item to register the color multiplier for
   */
  private void registerColorMultiplier(final ItemColorMapper mapper, final Item item) {
    MintClient.LOGGER.debug("Registering color multiplier {} for {}", mapper, MintItems.getName(item));
    ColorProviderRegistry.ITEM.register(mapper, item);
  }
}
