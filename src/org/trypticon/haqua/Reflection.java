/*
 * Haqua - a collection of hacks to work around issues in the Aqua look and feel
 * Copyright (C) 2014  Trejkaz, Haqua Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.trypticon.haqua;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author trejkaz
 */
class Reflection {
    private Reflection() {
    }

    static <T> Class<? extends T> getClass(String name, Class<T> superclass) {
        try {
            return Class.forName(name).asSubclass(superclass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class hierarchy of JRE has changed - have to update the hacks", e);
        }
    }

    static Field getDeclaredField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            makeAccessible(field);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Class hierarchy of JRE has changed - have to update the hacks", e);
        }
    }

    static Object get(Field field, Object object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field wasn't set accessible", e);
        }
    }

    static <T> Constructor<? extends T> getDeclaredConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        try {
            final Constructor<? extends T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            makeAccessible(constructor);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Class hierarchy of JRE has changed - have to update the hacks", e);
        }
    }

    static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            final Method method = clazz.getDeclaredMethod(name, parameterTypes);
            makeAccessible(method);
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Class hierarchy of JRE has changed - have to update the hacks", e);
        }
    }

    static Object invoke(Method method, Object object, Object... args) {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Constructor wasn't set accessible", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exception thrown by constructor", e);
        }
    }

    static <T> T newInstance(Constructor<T> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException e) {
            throw new RuntimeException("Class hierarchy of JRE has changed - have to update the hacks", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Constructor wasn't set accessible", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exception thrown by constructor", e);
        }
    }

    private static void makeAccessible(final AccessibleObject object) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                object.setAccessible(true);
                return null; // void
            }
        });
    }
}
