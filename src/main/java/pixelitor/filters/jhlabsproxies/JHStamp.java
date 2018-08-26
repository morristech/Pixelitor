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

package pixelitor.filters.jhlabsproxies;

import com.jhlabs.image.StampFilter;
import pixelitor.filters.ParametrizedFilter;
import pixelitor.filters.gui.ColorParam;
import pixelitor.filters.gui.IntChoiceParam;
import pixelitor.filters.gui.IntChoiceParam.Value;
import pixelitor.filters.gui.RangeParam;
import pixelitor.filters.gui.ShowOriginal;

import java.awt.image.BufferedImage;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static pixelitor.filters.gui.ColorParam.OpacitySetting.USER_ONLY_OPACITY;
import static pixelitor.filters.gui.RandomizePolicy.IGNORE_RANDOMIZE;

/**
 * Stamp filter based on the JHLabs StampFilter
 */
public class JHStamp extends ParametrizedFilter {
    public static final String NAME = "Stamp";

    private final RangeParam lightDarkBalance = new RangeParam("Light/Dark Balance (%)", 0, 50, 100);
    private final RangeParam smoothness = new RangeParam("Smoothness", 0, 25, 50);
    private final RangeParam soften = new RangeParam("Soften", 0, 3, 100);
    private final ColorParam darkColor = new ColorParam("Dark Color", BLACK, USER_ONLY_OPACITY);
    private final ColorParam brightColor = new ColorParam("Bright Color", WHITE, USER_ONLY_OPACITY);

    private final IntChoiceParam blurMethod = new IntChoiceParam("Blur Method",
            new Value[]{
                    // this is calculated with floats, but the animation is still not smooth
                    new Value("Fast", StampFilter.BOX3_BLUR),
                    new Value("Gaussian (slow for large images!)", StampFilter.GAUSSIAN_BLUR)
            }, IGNORE_RANDOMIZE);

    private StampFilter filter;

    public JHStamp() {
        super(ShowOriginal.YES);

        setParams(
                lightDarkBalance,
                smoothness.withAdjustedRange(0.05),
                soften,
                brightColor,
                darkColor,
                blurMethod
        );
    }

    @Override
    public BufferedImage doTransform(BufferedImage src, BufferedImage dest) {
        if (filter == null) {
            filter = new StampFilter(NAME);
        }

        filter.setBlack(darkColor.getColor().getRGB());
        filter.setWhite(brightColor.getColor().getRGB());
        filter.setRadius(smoothness.getValueAsFloat());
        filter.setSoftness(soften.getValueAsPercentage());
        filter.setThreshold(lightDarkBalance.getValueAsPercentage());
        filter.setBlurMethod(blurMethod.getValue());

        dest = filter.filter(src, dest);
        return dest;
    }
}