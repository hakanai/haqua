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

package org.trypticon.haqua.reflection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author trejkaz
 */
public class Class<T> {
    private final java.lang.Class<T> delegate;

    private Class(java.lang.Class<T> delegate) {
        this.delegate = delegate;
    }

    @NotNull
    public static <T> Class<? extends T> get(String name, java.lang.Class<T> superclass) {
        try {
            return new Class<>(java.lang.Class.forName(name).asSubclass(superclass));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class hierarchy of JRE has changed - have to update the hacks", e);
        }
    }

    @NotNull
    public List<Field<?>> getFields() {
        java.lang.reflect.Field[] delegates = delegate.getFields();
        List<Field<?>> fields = new ArrayList<>(delegates.length);
        for (java.lang.reflect.Field delegate : delegates) {
            fields.add(new Field<>(delegate));
        }
        return fields;
    }

    @NotNull
    public <T> Field<T> getDeclaredField(String name) {
        try {
            java.lang.reflect.Field field = delegate.getDeclaredField(name);
            Reflection.makeAccessible(field);
            return new Field<>(field);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Class hierarchy of JRE has changed - have to update the hacks", e);
        }
    }

    @NotNull
    public Constructor<T> getDeclaredConstructor(java.lang.Class<?>... parameterTypes) {
        try {
            final java.lang.reflect.Constructor<T> constructor = delegate.getDeclaredConstructor(parameterTypes);
            Reflection.makeAccessible(constructor);
            return new Constructor<>(constructor);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Class hierarchy of JRE has changed - have to update the hacks", e);
        }
    }

    @NotNull
    public <T> Method<T> getDeclaredMethod(String name, java.lang.Class<?>... parameterTypes) {
        try {
            final java.lang.reflect.Method method = delegate.getDeclaredMethod(name, parameterTypes);
            Reflection.makeAccessible(method);
            return new Method<>(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Class hierarchy of JRE has changed - have to update the hacks", e);
        }
    }

}
