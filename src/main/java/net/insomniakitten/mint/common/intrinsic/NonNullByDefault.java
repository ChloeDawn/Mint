package net.insomniakitten.mint.common.intrinsic;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Applied to a package, class, method, or field to indicate that all
 * of its enclosing values are non-null unless explicitly annotated
 * otherwise.
 *
 * @author InsomniaKitten
 */
@Documented
@Nonnull
@TypeQualifierDefault(
  {
    ElementType.FIELD,
    ElementType.LOCAL_VARIABLE,
    ElementType.METHOD,
    ElementType.PARAMETER
  }
)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonNullByDefault {}
