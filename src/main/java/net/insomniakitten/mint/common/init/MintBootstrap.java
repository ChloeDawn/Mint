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

package net.insomniakitten.mint.common.init;

import com.google.common.base.MoreObjects;
import com.google.common.base.Stopwatch;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.FabricLoader;
import net.insomniakitten.mint.client.init.MintClientBootstrap;
import net.insomniakitten.mint.common.Instance;
import net.insomniakitten.mint.common.Mint;
import net.insomniakitten.mint.common.state.InitializationState;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Logger;

import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;

/**
 * Initializer class for Mint's bootstrap
 *
 * @author InsomniaKitten
 */
public final class MintBootstrap implements ModInitializer {
  private static final MintBootstrap INSTANCE = new MintBootstrap();

  private static final Logger LOGGER = Mint.getLogger("bootstrap");

  private volatile InitializationState state = InitializationState.initial();

  private MintBootstrap() {}

  /**
   * Exposes the bootstrap for {@link FabricLoader}
   *
   * @return A singleton instance of {@link MintClientBootstrap}
   * @see FabricLoader#initializeMods()
   * @see Instance.Adapter#createInstance
   */
  @Instance
  public static MintBootstrap getInstance() {
    return MintBootstrap.INSTANCE;
  }

  public static Block getBlock(final String name) {
    return MintBlocks.INSTANCE.getBlock(name);
  }

  public static Identifier getName(final Block block) {
    return MintBlocks.INSTANCE.getName(block);
  }

  public static Item getItem(final String name) {
    return MintItems.INSTANCE.getItem(name);
  }

  public static Identifier getName(final Item item) {
    return MintItems.INSTANCE.getName(item);
  }

  /**
   * Initializes the bootstrap. If this is called whilst the bootstrap is
   * already initializing, or if the bootstrap is finalized, an exception
   * will be thrown.
   */
  @Override
  public void onInitialize() {
    if (this.state.isFinalized()) {
      throw new UnsupportedOperationException("Already initialized");
    }

    if (this.state.isInitializing()) {
      throw new ConcurrentModificationException("Already initialized");
    }

    this.state = InitializationState.INITIALIZING;

    MintBootstrap.LOGGER.info("Beginning bootstrap initialization");

    final Stopwatch stopwatch = Stopwatch.createStarted();

    MintBlocks.INSTANCE.registerBlocks();
    MintItems.INSTANCE.registerItems();

    final Stopwatch stopwatchResult = stopwatch.stop();
    final long elapsed = stopwatchResult.elapsed(TimeUnit.MILLISECONDS);

    MintBootstrap.LOGGER.info("Bootstrap initialized in {}ms", elapsed);

    this.state = InitializationState.FINALIZED;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("state", this.state).toString();
  }
}
