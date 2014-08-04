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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * @author trejkaz
 */
class Images {
    private Images() {
    }

    static BufferedImage createCompatibleTranslucentImage(int width, int height) {
        GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return graphicsConfiguration.createCompatibleImage(width, height, BufferedImage.TRANSLUCENT);
    }

    static int[] getPixels(@NotNull BufferedImage image, int x, int y, int w, int h, @Nullable int[] pixels) {
        if (w == 0 || h == 0) {
            return new int[0];
        }

        if (pixels == null) {
            pixels = new int[w * h];
        } else if (pixels.length < w * h) {
            throw new IllegalArgumentException("pixels array must have a length >= w * h");
        }

        int imageType = image.getType();
        if (imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_INT_RGB) {
            Raster raster = image.getRaster();
            return (int[]) raster.getDataElements(x, y, w, h, pixels);
        }

        return image.getRGB(x, y, w, h, pixels, 0, w);
    }

    static void setPixels(@NotNull BufferedImage image, int x, int y, int w, int h, @Nullable int[] pixels) {
        if (pixels == null || w == 0 || h == 0) {
            return;
        } else if (pixels.length < w * h) {
            throw new IllegalArgumentException("pixels array must have a length >= w * h");
        }

        int imageType = image.getType();
        if (imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_INT_RGB) {
            WritableRaster raster = image.getRaster();
            raster.setDataElements(x, y, w, h, pixels);
        } else {
            image.setRGB(x, y, w, h, pixels, 0, w);
        }
    }

}
