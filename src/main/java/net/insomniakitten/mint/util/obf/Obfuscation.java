package net.insomniakitten.mint.util.obf;

import net.minecraft.init.Blocks;

import java.util.Locale;

public enum Obfuscation {
    NOTCH, SRG, MCP;

    private static final Obfuscation ENV_OBF = Obfuscation.determineEnvironmentObfuscation();

    public static Obfuscation environment() {
        return Obfuscation.ENV_OBF;
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    private static Obfuscation determineEnvironmentObfuscation() {
        final Class<Blocks> type = Blocks.class;
        try {
            type.getDeclaredField("AIR");
            return Obfuscation.MCP;
        } catch (final NoSuchFieldException eMcp) {
            try {
                type.getDeclaredField("field_150350_a");
                return Obfuscation.SRG;
            } catch (final NoSuchFieldException eSrg) {
                try {
                    type.getDeclaredField("a");
                    return Obfuscation.NOTCH;
                } catch (final NoSuchFieldException eNotch) {
                    throw new IllegalStateException(eNotch);
                }
            }
        }
    }

    public final boolean isNotch() {
        return Obfuscation.NOTCH == this;
    }

    public final boolean isSrg() {
        return Obfuscation.SRG == this;
    }

    public final boolean isMcp() {
        return Obfuscation.MCP == this;
    }

    @Override
    public final String toString() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
