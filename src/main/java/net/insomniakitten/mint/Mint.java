package net.insomniakitten.mint;

import net.insomniakitten.pylon.annotation.rift.Mod;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.riftloader.RiftLoader;

import javax.annotation.Nullable;

/**
 * Reference class for holding identifier constants
 * and miscellaneous static utility methods
 *
 * @author InsomniaKitten
 */
@Mod(id = Mint.ID, name = Mint.NAME, version = Mint.VERSION, authors = Mint.AUTHOR)
public final class Mint {
    public static final String ID = "mint";
    public static final String NAME = "Mint";
    public static final String VERSION = "0.1.0";
    public static final String AUTHOR = "InsomniaKitten";

    private static final Logger LOGGER = Mint.getLogger("main");

    @Nullable
    private static Boolean deobfRuntime;

    private Mint() {}

    /**
     * Prepends the given {@link String} with Mint's namespace
     *
     * @param string The string to prepended to
     * @return A namespaced string
     * @throws IllegalArgumentException If the string is empty
     */
    public static ResourceLocation withNamespace(final String string) {
        if (string.isEmpty()) {
            throw new IllegalArgumentException("String cannot be empty");
        }

        return new ResourceLocation(Mint.ID, string);
    }

    /**
     * Retrieves a namespaced {@link Logger} for the given topic
     *
     * @param topic The topic of the logger
     * @return A logger instance from the factory
     * @throws IllegalArgumentException If the topic is empty
     */
    public static Logger getLogger(final String topic) {
        if (topic.isEmpty()) {
            throw new IllegalArgumentException("Topic cannot be empty");
        }

        return LogManager.getLogger(Mint.ID + '.' + topic);
    }

    /**
     * Determines if the runtime is deobfuscated, through looking up a field by
     * its deobfuscated name in a silent try/catch. If the lookup fails, it
     * is assumed that the runtime is obfuscated, at least to an extent.
     * The result of this lookup is cached to {@link Mint#deobfRuntime}
     *
     * @return True if the runtime is deobfuscated to an extent
     */
    public static boolean isRuntimeDeobfuscated() {
        if (Mint.deobfRuntime == null) {
            try {
                Blocks.class.getDeclaredField("AIR");
                Mint.deobfRuntime = true;
            } catch (final NoSuchFieldException ignored) {
                Mint.deobfRuntime = false;
            }
        }

        return Mint.deobfRuntime;
    }

    /**
     * Assigns the given instance to the given class in Rift's loader
     *
     * @param clazz The class to have an instance assigned for
     * @param instance The instance to be assigned to the class
     * @see RiftLoader#setInstanceForListenerClass(Class, Object)
     * @see RiftLoader#listenerInstanceMap
     */
    public static <T> void setInstanceForLoader(final Class<T> clazz, final T instance) {
        Mint.LOGGER.debug("Assigning instance {} for {}", instance, clazz);

        RiftLoader.instance.setInstanceForListenerClass(clazz, instance);
    }
}
