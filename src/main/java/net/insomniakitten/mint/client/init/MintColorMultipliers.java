package net.insomniakitten.mint.client.init;

import com.google.common.base.MoreObjects;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.insomniakitten.mint.client.color.BlockItemColorMultiplier;
import net.insomniakitten.mint.client.color.GrassBlockColorMultiplier;
import net.insomniakitten.mint.common.Mint;
import net.insomniakitten.mint.common.init.MintBootstrap;
import net.insomniakitten.mint.common.state.RegistrationState;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockColorMap;
import net.minecraft.client.render.block.BlockColorMapper;
import net.minecraft.client.render.item.ItemColorMap;
import net.minecraft.client.render.item.ItemColorMapper;
import net.minecraft.item.Item;
import org.apache.logging.log4j.Logger;

import java.util.ConcurrentModificationException;

/**
 * Listener class for registration for Mint's color multiplier registration
 *
 * @author InsomniaKitten
 */
final class MintColorMultipliers {
  static final MintColorMultipliers INSTANCE = new MintColorMultipliers();

  private static final Logger LOGGER = Mint.getLogger("client.colormultipliers");

  private volatile RegistrationState state = RegistrationState.initial();

  private MintColorMultipliers() {}

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("state", this.state).toString();
  }

  /**
   * Looks up the game's {@link BlockColorMap} and {@link ItemColorMap} instances
   * and registers color multipliers to them for Mint's blocks and items
   */
  void registerColorMultipliers() {
    if (this.state.isRegistered()) {
      throw new UnsupportedOperationException("Already registered");
    }

    if (this.state.isRegistering()) {
      throw new ConcurrentModificationException("Already registering");
    }

    this.state = RegistrationState.REGISTERING;

    MintColorMultipliers.LOGGER.info("Registering block color multipliers");

    final BlockColorMapper grassColor = new GrassBlockColorMultiplier(0.5, 1.0);

    this.registerColorMultiplier(grassColor, MintBootstrap.getBlock("grass_block_stairs"));
    this.registerColorMultiplier(grassColor, MintBootstrap.getBlock("grass_block_slab"));

    MintColorMultipliers.LOGGER.info("Registering item color multipliers");

    final ItemColorMapper blockColor = new BlockItemColorMultiplier();

    this.registerColorMultiplier(blockColor, MintBootstrap.getItem("grass_block_stairs"));
    this.registerColorMultiplier(blockColor, MintBootstrap.getItem("grass_block_slab"));

    this.state = RegistrationState.REGISTERED;
  }

  /**
   * Registers a color multiplier to the given {@link BlockColorMap}
   *
   * @param mapper The block color multiplier to register
   * @param block The block to register the color multiplier for
   */
  private void registerColorMultiplier(final BlockColorMapper mapper, final Block block) {
    MintColorMultipliers.LOGGER.debug("Registering color multiplier {} for {}", mapper, MintBootstrap.getName(block));
    ColorProviderRegistry.BLOCK.register(mapper, block);
  }

  /**
   * Registers a color multiplier to the given {@link ItemColorMapper}
   *
   * @param mapper The item color multiplier to register
   * @param item The item to register the color multiplier for
   */
  private void registerColorMultiplier(final ItemColorMapper mapper, final Item item) {
    MintColorMultipliers.LOGGER.debug("Registering color multiplier {} for {}", mapper, MintBootstrap.getName(item));
    ColorProviderRegistry.ITEM.register(mapper, item);
  }
}
