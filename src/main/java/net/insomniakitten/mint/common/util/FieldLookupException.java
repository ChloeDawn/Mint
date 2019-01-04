package net.insomniakitten.mint.common.util;

/**
 * Exception for propagating {@link NoSuchFieldException} and {@link IllegalAccessException}
 * Only these two exception types are supported, and if an illegal exception type is passed
 * to the constructor, an {@link IllegalArgumentException} will be thrown. The constructor
 * accepts a base {@link ReflectiveOperationException} in order to support propagation of
 * a piped catch clause - a catch block containing multiple types as one parameter
 *
 * @author InsomniaKitten
 * @see FieldLookupException#verifyExceptionType(ReflectiveOperationException)
 */
public final class FieldLookupException extends RuntimeException {
  public FieldLookupException(final String message, final ReflectiveOperationException cause) {
    super(message, FieldLookupException.verifyExceptionType(cause));
  }

  /**
   * Verifies that the given {@link Exception} is supported for propagation
   *
   * @param exception The exception instance to verify
   * @return The given exception instance, if valid
   * @throws IllegalArgumentException If the exception is not valid
   */
  private static ReflectiveOperationException verifyExceptionType(final ReflectiveOperationException exception) {
    if (exception instanceof NoSuchFieldException || exception instanceof IllegalAccessException) {
      return exception;
    }

    throw new IllegalArgumentException(exception.getClass().getName());
  }
}
