package io.github.insomniakitten.mint.common.util;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

import java.util.Collection;
import java.util.Map.Entry;

/**
 * Utility class for functions relating
 * to {@link BlockState} manipulation
 *
 * @author InsomniaKitten
 */
public final class BlockStates {
  private BlockStates() {}

  /**
   * Copies all (viable) properties from the source {@link BlockState} to the
   * target {@link BlockState}. If a source's property does not exist in
   * the target blockstate, it will not be copied.
   *
   * @param source The source blockstate to copy properties and values from
   * @param target The target blockstate for properties and values to be copied to
   * @return The target, with all viable source properties and values copied to it
   */
  @SuppressWarnings("unchecked")
  public static BlockState copyTo(final BlockState source, final BlockState target) {
    final Collection<Property<?>> properties = target.getProperties();
    BlockState copy = target;

    for (final Entry<Property<?>, Comparable<?>> entry : source.getEntries().entrySet()) {
      final Property property = entry.getKey();

      if (properties.contains(property)) {
        copy = copy.with(property, (Comparable) entry.getValue());
      }
    }

    return copy;
  }
}
