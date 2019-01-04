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

package net.insomniakitten.mint.common;

import com.google.common.collect.MoreCollectors;
import net.fabricmc.loader.language.LanguageAdapter;
import net.fabricmc.loader.language.LanguageAdapterException;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Instance {
  final class Adapter implements LanguageAdapter {
    @Override
    @Nullable
    public Object createInstance(final Class<?> clazz, final Options options) throws LanguageAdapterException {
      try {
        return Arrays.stream(clazz.getDeclaredMethods())
          .filter(method ->
            this.returnsType(method, clazz)
              && this.hasNoArguments(method)
              && this.isPublicStatic(method)
              && this.hasAnnotation(method))
          .collect(MoreCollectors.toOptional())
          .orElseThrow(IllegalStateException::new)
          .invoke(null);
      } catch (final IllegalAccessException e) {
        throw new IllegalStateException(e);
      } catch (final InvocationTargetException e) {
        final MissingSuperclassBehavior behavior = options.getMissingSuperclassBehavior();
        switch (behavior) {
          case RETURN_NULL:
            e.printStackTrace();
            return null;
          case CRASH:
            final Throwable t = e.getCause();
            throw new LanguageAdapterException(t.toString(), t);
          default:
            throw new IllegalArgumentException("Behavior " + behavior + " in " + options);
        }
      }
    }

    private boolean returnsType(final Method method, final Class<?> type) {
      return type == method.getReturnType();
    }

    private boolean hasNoArguments(final Method method) {
      return 0 == method.getParameterCount();
    }

    private boolean isPublicStatic(final Method method) {
      final int modifiers = method.getModifiers();
      return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers);
    }

    private boolean hasAnnotation(final Method method) {
      return method.isAnnotationPresent(Instance.class);
    }
  }
}
