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

import java.lang.reflect.Modifier;

/**
 * @author trejkaz
 */
public class Field<T> {
    private final java.lang.reflect.Field delegate;

    Field(java.lang.reflect.Field delegate) {
        this.delegate = delegate;
    }

    public boolean isStatic() {
        return (delegate.getModifiers() & Modifier.STATIC) != 0;
    }

    @SuppressWarnings("unchecked") // would be fixable if Field would parameterise its return type...
    public T get(Object object) {
        try {
            return (T) delegate.get(object);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field wasn't set accessible", e);
        }
    }

    public int getInt(Object object) {
        try {
            return delegate.getInt(object);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field wasn't set accessible", e);
        }
    }

    public long getLong(Object object) {
        try {
            return delegate.getLong(object);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Field wasn't set accessible", e);
        }
    }

}
