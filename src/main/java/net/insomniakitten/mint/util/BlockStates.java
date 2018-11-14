package net.insomniakitten.mint.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.state.IProperty;
import net.minecraft.state.IStateHolder;

import java.util.Collection;
import java.util.Map.Entry;

/**
 * Utility class for functions relating
 * to {@link IBlockState} manipulation
 *
 * @author InsomniaKitten
 */
public final class BlockStates {
    private BlockStates() {}

    /**
     * Copies all (viable) properties from the source {@link IBlockState} to the
     * target {@link IBlockState}. If a source's property does not exist in
     * the target blockstate, it will not be copied.
     *
     * @param source The source blockstate to copy properties and values from
     * @param target The target blockstate for properties and values to be copied to
     * @return The target, with all viable source properties and values copied to it
     */
    @SuppressWarnings("RedundantCast")
    public static IBlockState copyTo(final IStateHolder<IBlockState> source, final IBlockState target) {
        final Collection<IProperty<?>> properties = target.getProperties();
        IBlockState copy = target;

        for (final Entry<IProperty<?>, Comparable<?>> entry : source.getValues().entrySet()) {
            final IProperty property = entry.getKey();
            final Comparable value = entry.getValue();

            if (properties.contains(property)) {
                copy = copy.with(property, value);
            }
        }

        return copy;
    }
}
