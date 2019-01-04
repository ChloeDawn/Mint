package net.insomniakitten.mint.common;

import com.google.common.base.Preconditions;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

/**
 * Reference class for holding identifier constants
 * and miscellaneous static utility methods
 *
 * @author InsomniaKitten
 */
public final class Mint {
  private static final String ID = "mint";

  private static final Logger LOGGER = Mint.getLogger("main");

  @Nullable private static Boolean deobfRuntime;

  private Mint() {}

  /**
   * Prepends the given {@link String} with Mint's namespace
   *
   * @param string The string to prepended to
   * @return A namespaced string
   * @throws IllegalArgumentException If the string is empty
   */
  public static Identifier withNamespace(final String string) {
    Preconditions.checkArgument(!string.isEmpty(), "String cannot be empty");
    return new Identifier(Mint.ID, string);
  }

  /**
   * Retrieves a namespaced {@link Logger} for the given topic
   *
   * @param topic The topic of the logger
   * @return A logger instance from the factory
   * @throws IllegalArgumentException If the topic is empty
   */
  public static Logger getLogger(final String topic) {
    Preconditions.checkArgument(!topic.isEmpty(), "Topic cannot be empty");
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
}
