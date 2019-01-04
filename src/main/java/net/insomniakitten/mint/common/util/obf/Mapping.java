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

package net.insomniakitten.mint.common.util.obf;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

public final class Mapping {
  private final String value;

  private Mapping(final String intermediary, final String named) {
    this.value = this.getMappingForEnvironment(intermediary, named);
  }

  public static Mapping of(final String intermediary, final String named) {
    Preconditions.checkArgument(!intermediary.isEmpty(), "'intermediary' cannot be empty");
    Preconditions.checkArgument(!named.isEmpty(), "'named' cannot be empty");

    return new Mapping(intermediary, named);
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
    return "Mapping['" + this.value + "']";
  }

  private String getMappingForEnvironment(final String intermediary, final String named) {
    final Obfuscation obf = Obfuscation.environment();

    switch (obf) {
      case INTERMEDIARY: return intermediary;
      case NAMED: return named;
    }

    throw new IllegalStateException(obf.toString());
  }

  public static final class Builder {
    @Nullable private String intermediary;
    @Nullable private String named;

    private Builder() {}

    public Builder intermediary(final String intermediary) {
      Preconditions.checkArgument(!intermediary.isEmpty(), "Value cannot be empty");
      this.intermediary = intermediary;

      return this;
    }

    public Builder named(final String named) {
      Preconditions.checkArgument(!named.isEmpty(), "Value cannot be empty");
      this.named = named;

      return this;
    }

    public Mapping build() {
      final String intermediary = Preconditions.checkNotNull(this.intermediary);
      final String named = MoreObjects.firstNonNull(this.named, intermediary);

      return new Mapping(intermediary, named);
    }
  }
}
