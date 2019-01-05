package io.github.insomniakitten.mint.client.color;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BiomeColors;
import net.minecraft.client.render.block.BlockColorMapper;
import net.minecraft.client.render.block.GrassColorHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;

import javax.annotation.Nullable;

/**
 * An {@link BlockColorMapper} implementation that returns the grass color of
 * the given position in the given world, or a default color when no world
 * or position are present - determined through parameter nullability
 *
 * @author InsomniaKitten
 */
public final class GrassBlockColorMultiplier implements BlockColorMapper {
  private final double temperature;
  private final double humidity;

  /**
   * Constructs a new {@link GrassBlockColorMultiplier} with the given default values
   * that are used for the grass colormap if a world and position are not present
   *
   * @param temperature The default grass colormap temperature
   * @param humidity The default grass colormap humidity
   */
  public GrassBlockColorMultiplier(final double temperature, final double humidity) {
    this.temperature = temperature;
    this.humidity = humidity;
  }

  /**
   * Determines the grass color for the given position in the given world, or a default
   * grass color if the world and position are not present in the context
   *
   * @param state The blockstate a color has been requested for
   * @param view The world context, 'null' if not present
   * @param position The block position, 'null' if not present
   * @param tintIndex The tint index a color has been requested for
   * @return The grass color, or a default color if world and position are 'null'
   */
  @Override
  public int getColor(final BlockState state, @Nullable final ExtendedBlockView view, @Nullable final BlockPos position, final int tintIndex) {
    if (view != null && position != null) {
      return BiomeColors.grassColorAt(view, position);
    }

    return GrassColorHandler.getColor(this.temperature, this.humidity);
  }
}
