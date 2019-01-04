package net.insomniakitten.mint.common.util.state;

import java.util.ConcurrentModificationException;
import java.util.Locale;

/**
 * Represents the current initialization state of a class or instance
 * Each state holds a contract on the behaviour of the class
 *
 * @author InsomniaKitten
 */
public enum InitializationState {
  /**
   * The uninitialized state of the class or instance
   * <ul>
   * <li>The class or instance *cannot* be accessed within this state</li>
   * <li>The class or instance is *immutable* within this state</li>
   * </ul>
   */
  UNINITIALIZED,

  /**
   * The initializing state of the class or instance. The class can and should
   * throw a {@link ConcurrentModificationException} if accessed or modified
   * with concurrency during this state
   * <ul>
   * <li>The class or instance *can* be accessed within this state</li>
   * <li>The class or instance is *mutable* within this state</li>
   * </ul>
   */
  INITIALIZING,

  /**
   * The finalized state of the class or instance
   * <ul>
   * <li>The class or instance *can* be accessed within this state</li>
   * <li>The class or instance is *immutable* within this state</li>
   * </ul>
   */
  FINALIZED;

  /**
   * The initial uninitialized state of the class or instance
   */
  public static InitializationState initial() {
    return InitializationState.UNINITIALIZED;
  }

  /**
   * Determines if this state is {@link InitializationState#UNINITIALIZED}
   */
  public final boolean isUninitialized() {
    return InitializationState.UNINITIALIZED == this;
  }

  /**
   * Determines if this state is {@link InitializationState#INITIALIZING}
   */
  public final boolean isInitializing() {
    return InitializationState.INITIALIZING == this;
  }

  /**
   * Determines if this state is {@link InitializationState#FINALIZED}
   */
  public final boolean isFinalized() {
    return InitializationState.FINALIZED == this;
  }

  @Override
  public final String toString() {
    return this.name().toLowerCase(Locale.ROOT);
  }
}
