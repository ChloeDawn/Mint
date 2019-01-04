package net.insomniakitten.mint.client.init;

import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.FabricLoader;
import net.insomniakitten.mint.common.Instance;
import net.insomniakitten.mint.common.Mint;
import net.insomniakitten.mint.common.state.InitializationState;
import org.apache.logging.log4j.Logger;

import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;

/**
 * Initializer class for Mint's client bootstrap
 *
 * @author InsomniaKitten
 */
public final class MintClientBootstrap implements ClientModInitializer {
  private static final MintClientBootstrap INSTANCE = new MintClientBootstrap();

  private static final Logger LOGGER = Mint.getLogger("client.bootstrap");

  private volatile InitializationState state = InitializationState.initial();

  private MintClientBootstrap() {}

  /**
   * Exposes the client bootstrap for {@link FabricLoader}
   *
   * @return A singleton instance of {@link MintClientBootstrap}
   * @see FabricLoader#initializeMods()
   * @see Instance.Adapter#createInstance
   */
  @Instance
  public static MintClientBootstrap getInstance() {
    return MintClientBootstrap.INSTANCE;
  }

  /**
   * Initializes the client bootstrap. If this is called whilst the client bootstrap is
   * already initializing, or if the client bootstrap is finalized, an exception
   * will be thrown.
   */
  @Override
  public void onInitializeClient() {
    if (this.state.isFinalized()) {
      throw new UnsupportedOperationException("Already initialized");
    }

    if (this.state.isInitializing()) {
      throw new ConcurrentModificationException("Already initialized");
    }

    this.state = InitializationState.INITIALIZING;

    MintClientBootstrap.LOGGER.info("Beginning client bootstrap initialization");

    final Stopwatch stopwatch = Stopwatch.createStarted();

    MintColorMultipliers.INSTANCE.registerColorMultipliers();

    final Stopwatch stopwatchResult = stopwatch.stop();
    final long elapsed = stopwatchResult.elapsed(TimeUnit.MILLISECONDS);

    MintClientBootstrap.LOGGER.info("Client bootstrap initialized in {}ms", elapsed);

    this.state = InitializationState.FINALIZED;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("state", this.state).toString();
  }
}
