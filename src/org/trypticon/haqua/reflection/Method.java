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

import java.lang.reflect.InvocationTargetException;

/**
 * @author trejkaz
 */
public class Method<T> {
    private final java.lang.reflect.Method delegate;

    Method(java.lang.reflect.Method delegate) {
        this.delegate = delegate;
    }

    @NotNull
    @SuppressWarnings("unchecked") // would be fixable if Method would parameterise its return type...
    public T invoke(Object object, Object... args) {
        try {
            return (T) delegate.invoke(object, args);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Constructor wasn't set accessible", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exception thrown by constructor", e);
        }
    }

}
