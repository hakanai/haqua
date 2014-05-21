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

import java.lang.reflect.InvocationTargetException;

/**
 * @author trejkaz
 */
public class Constructor<T> {
    private final java.lang.reflect.Constructor<T> delegate;

    Constructor(java.lang.reflect.Constructor<T> delegate) {
        this.delegate = delegate;
    }

    public T newInstance(Object... args) {
        try {
            return delegate.newInstance(args);
        } catch (InstantiationException e) {
            throw new RuntimeException("Class hierarchy of JRE has changed - have to update the hacks", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Constructor wasn't set accessible", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exception thrown by constructor", e);
        }
    }
}
