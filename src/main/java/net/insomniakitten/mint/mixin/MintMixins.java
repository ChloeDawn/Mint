package net.insomniakitten.mint.mixin;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import net.insomniakitten.mint.Mint;
import net.insomniakitten.mint.util.state.InitializationState;
import net.insomniakitten.pylon.annotation.rift.Listener;
import org.apache.logging.log4j.Logger;
import org.dimdev.riftloader.RiftLoader;
import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.transformer.Config;

import javax.annotation.Nullable;
import java.util.ConcurrentModificationException;

// TODO Look into removing the requirement of JSON, instead handling configuration setup at runtime
// TODO Alternatively, setup Pylon to allow generation of mixin configuration JSONs from annotations

/**
 * Listener class for Mint's mixin implementation.
 * Handles initialization of the mixin bootstrap, and
 * registration of Mint's mixin configuration file
 *
 * @author InsomniaKitten
 * @see MixinBootstrap#init
 * @see Mixins#addConfiguration(String)
 * @see MintMixins#CONFIGURATION_FILE
 */
@Listener(priority = Integer.MIN_VALUE + 1)
public final class MintMixins implements InitializationListener {
    private static final MintMixins INSTANCE = new MintMixins();

    private static final Logger LOGGER = Mint.getLogger("mixins");

    private static final String CONFIGURATION_FILE = "mixins.mint.json";

    static {
        Mint.setInstanceForLoader(MintMixins.class, MintMixins.INSTANCE);
    }

    private volatile InitializationState state = InitializationState.initial();

    @Nullable private IMixinConfig cfg;
    @Nullable private MixinEnvironment env;

    private MintMixins() {}

    /**
     * Called by {@link RiftLoader#initMods} during the runtime
     * initialization phase. Registers the mixin configuration
     * and prints diagnostics, before finalizing the state
     */
    @Override
    public void onInitialization() {
        if (this.state.isFinalized()) {
            throw new IllegalStateException("Already initialized");
        }

        if (this.state.isInitializing()) {
            throw new ConcurrentModificationException("Already initializing");
        }

        this.state = InitializationState.INITIALIZING;

        this.registerConfiguration();
        this.cacheReferences();
        this.printDiagnostics();

        this.state = InitializationState.FINALIZED;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("configuration", this.cfg)
            .add("environment", this.env)
            .add("state", this.state)
            .toString();
    }

    /**
     * Initializes the Mixin bootstrap and registers
     * the {@link MintMixins#CONFIGURATION_FILE}
     *
     * @see MixinBootstrap#init()
     * @see Mixins#addConfiguration(String)
     */
    private void registerConfiguration() {
        MintMixins.LOGGER.info("Initializing bootstrap");
        MixinBootstrap.init();

        MintMixins.LOGGER.info("Registering configuration");
        Mixins.addConfiguration(MintMixins.CONFIGURATION_FILE);
    }

    /**
     * Looks up the configuration instance from the Mixin configuration
     * collection, and caches it to {@link MintMixins#cfg}
     * along with a reference to its {@link MixinEnvironment} context
     *
     * @see Mixins#getConfigs()
     */
    private void cacheReferences() {
        Preconditions.checkState(this.cfg == null && this.env == null, "References already cached");

        MintMixins.LOGGER.info("Caching references");

        for (final Config config : Mixins.getConfigs()) {
            if (MintMixins.CONFIGURATION_FILE.equals(config.getName())) {
                this.cfg = config.getConfig();
                this.env = config.getEnvironment();

                return;
            }
        }

        throw new IllegalStateException("Missing external references");
    }

    /**
     * Prints information about the mixin configuration and environment
     * to the debug output of {@link MintMixins#LOGGER}
     */
    private void printDiagnostics() {
        Preconditions.checkState(this.cfg != null || this.env != null, "References not cached");
        Preconditions.checkState((this.cfg == null) == (this.env == null), "Reference cache imbalance");

        MintMixins.LOGGER.debug("Environment version: {}", this.env.getVersion());
        MintMixins.LOGGER.debug("Environment side: {}", this.env.getSide());
        MintMixins.LOGGER.debug("Package: {}", this.cfg.getMixinPackage());
        MintMixins.LOGGER.debug("Priority: {}", this.cfg.getPriority());
        MintMixins.LOGGER.debug("Targets:");

        for (final String target : this.cfg.getTargets()) {
            MintMixins.LOGGER.debug(" - {}", target);
        }
    }
}
