/*
 * Copyright (C) 2018 InsomniaKitten
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.insomniakitten.mint.util.obf;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

public final class Mapping {
    private final String value;

    private Mapping(final String notch, final String srg, final String mcp) {
        this.value = this.getMappingForEnvironment(notch, srg, mcp);
    }

    public static Mapping of(final String notch, final String srg, final String mcp) {
        Preconditions.checkArgument(!notch.isEmpty(), "Value 'notch' cannot be empty");
        Preconditions.checkArgument(!srg.isEmpty(), "Value 'srg' cannot be empty");
        Preconditions.checkArgument(!mcp.isEmpty(), "Value 'mcp' cannot be empty");
        return new Mapping(notch, srg, mcp);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        return this == obj || obj instanceof Mapping && this.value.equals(((Mapping) obj).value);
    }

    @Override
    public String toString() {
        return "Mapping[" + this.value + "]";
    }

    private String getMappingForEnvironment(final String notch, final String srg, final String mcp) {
        final Obfuscation obf = Obfuscation.environment();
        if (obf.isNotch()) return notch;
        if (obf.isSrg()) return srg;
        if (obf.isMcp()) return mcp;
        throw new IllegalStateException(obf.toString());
    }

    public static final class Builder {
        @Nullable private String notch;
        @Nullable private String srg;
        @Nullable private String mcp;

        private Builder() {}

        public Builder notch(final String notch) {
            Preconditions.checkArgument(!notch.isEmpty(), "Value cannot be empty");
            this.notch = notch;
            return this;
        }

        public Builder srg(final String srg) {
            Preconditions.checkArgument(!srg.isEmpty(), "Value cannot be empty");
            this.srg = srg;
            return this;
        }

        public Builder mcp(final String mcp) {
            Preconditions.checkArgument(!mcp.isEmpty(), "Value cannot be empty");
            this.mcp = mcp;
            return this;
        }

        public Mapping build() {
            final String notch = Preconditions.checkNotNull(this.notch);
            final String srg = MoreObjects.firstNonNull(this.srg, notch);
            final String mcp = MoreObjects.firstNonNull(this.mcp, srg);
            return new Mapping(notch, srg, mcp);
        }
    }
}
