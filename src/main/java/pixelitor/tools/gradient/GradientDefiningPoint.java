/*
 * Copyright 2018 Laszlo Balazs-Csiki and Contributors
 *
 * This file is part of Pixelitor. Pixelitor is free software: you
 * can redistribute it and/or modify it under the terms of the GNU
 * General Public License, version 3 as published by the Free
 * Software Foundation.
 *
 * Pixelitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pixelitor. If not, see <http://www.gnu.org/licenses/>.
 */

package pixelitor.tools.gradient;

import pixelitor.gui.ImageComponent;
import pixelitor.tools.DraggablePoint;
import pixelitor.utils.Utils;

import java.awt.Color;
import java.awt.Point;

/**
 * Either a gradient start point or a gradient end point
 */
public class GradientDefiningPoint extends DraggablePoint {
    private GradientDefiningPoint other;
    private GradientCenterPoint center;

    public GradientDefiningPoint(String name, int x, int y, ImageComponent ic, Color color, Color activeColor) {
        super(name, x, y, ic, color, activeColor);
    }

    public void setOther(GradientDefiningPoint other) {
        this.other = other;
    }

    public void setCenter(GradientCenterPoint center) {
        this.center = center;
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);

        // also move the center point
        int cx = (x + other.x) / 2;
        int cy = (y + other.y) / 2;
        center.setLocationWithoutMovingChildren(cx, cy);
    }

    @Override
    public void setConstrainedLocation(int mouseX, int mouseY) {
        // constrain it relative to the other point:
        // it seems more useful than constraining it relative to its own drag start
        Point p = Utils.constrainEndPoint(other.x, other.y, mouseX, mouseY);
        setLocation(p.x, p.y);
    }

    @Override
    protected void afterMouseReleasedActions() {
        calcImCoords();
        center.calcImCoords();
    }
}
