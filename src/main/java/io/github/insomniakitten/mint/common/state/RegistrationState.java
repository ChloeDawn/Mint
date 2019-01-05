package io.github.insomniakitten.mint.common.state;

import java.util.Locale;

/**
 * Represents the current registration state of a class or instance
 * Each state holds a contract on the behaviour of the registry
 *
 * @author InsomniaKitten
 */
public enum RegistrationState {
  /**
   * The pre-registration state of the registry class or instance
   * <ul>
   * <li>Registry objects *cannot* be registered within this state</li>
   * <li>Registry objects *cannot* be accessed within this state</li>
   * </ul>
   */
  UNREGISTERED,

  /**
   * The mid-registration state of the registry class or instance
   * <ul>
   * <li>Registry objects *can* be registered within this state</li>
   * <li>Registry objects *cannot* be accessed during this state</li>
   * </ul>
   */
  REGISTERING,

  /**
   * The post-registration state of the registry class or instance
   * <ul>
   * <li>Registry objects *cannot* be registered during this state</li>
   * <li>Registry objects *can* be accessed within this state</li>
   * </ul>
   */
  REGISTERED;

  /**
   * The initial unregistered state of the registry class or instance
   */
  public static RegistrationState initial() {
    return RegistrationState.UNREGISTERED;
  }

  /**
   * Determines if this state is {@link RegistrationState#UNREGISTERED}
   */
  public final boolean isUnregistered() {
    return RegistrationState.UNREGISTERED == this;
  }

  /**
   * Determines if this state is {@link RegistrationState#REGISTERING}
   */
  public final boolean isRegistering() {
    return RegistrationState.REGISTERING == this;
  }

  /**
   * Determines if this state is {@link RegistrationState#REGISTERED}
   */
  public final boolean isRegistered() {
    return RegistrationState.REGISTERED == this;
  }

  @Override
  public final String toString() {
    return this.name().toLowerCase(Locale.ROOT);
  }
}
