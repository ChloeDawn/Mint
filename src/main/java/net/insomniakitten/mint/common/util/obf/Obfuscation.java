package net.insomniakitten.mint.common.util.obf;

import net.minecraft.item.Items;

import java.util.Locale;

enum Obfuscation {
  INTERMEDIARY, NAMED;

  private static final Obfuscation ENV_OBF = Obfuscation.determineEnvironmentObfuscation();

  public static Obfuscation environment() {
    return Obfuscation.ENV_OBF;
  }

  @SuppressWarnings("JavaReflectionMemberAccess")
  private static Obfuscation determineEnvironmentObfuscation() {
    final Class<Items> type = Items.class;
    try {
      type.getDeclaredField("AIR");
      return Obfuscation.NAMED;
    } catch (final NoSuchFieldException en) {
      try {
        type.getDeclaredField("field_8162");
        return Obfuscation.INTERMEDIARY;
      } catch (final NoSuchFieldException ei) {
        throw new IllegalStateException(ei);
      }
    }
  }

  public final boolean isIntermediary() {
    return Obfuscation.INTERMEDIARY == this;
  }

  public final boolean isNamed() {
    return Obfuscation.NAMED == this;
  }

  @Override
  public final String toString() {
    return this.name().toLowerCase(Locale.ROOT);
  }
}
