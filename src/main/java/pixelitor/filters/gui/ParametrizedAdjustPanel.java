/*
 * Copyright 2015 Laszlo Balazs-Csiki
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

package pixelitor.filters.gui;

import pixelitor.ImageComponents;
import pixelitor.filters.FilterWithParametrizedGUI;
import pixelitor.utils.GridBagHelper;
import pixelitor.utils.Utils;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;

public class ParametrizedAdjustPanel extends AdjustPanel implements ParamAdjustmentListener {
    /**
     * Controls whether the params are reset to the default values when a new
     * ParametrizedAdjustPanel is created
     */
    private static boolean resetParams = true;

    public ParametrizedAdjustPanel(FilterWithParametrizedGUI filter, boolean showOriginal) {
        this(filter, null, showOriginal);
    }

    public ParametrizedAdjustPanel(FilterWithParametrizedGUI filter, Object otherInfo, boolean showOriginal) {
        super(filter);

        ParamSet params = filter.getParamSet();
        if (resetParams) {
            params.reset();
            params.considerImageSize(ImageComponents.getActiveComp().get().getCanvas().getBounds());
        }
        params.setAdjustmentListener(this);

        setupGUI(params, otherInfo, showOriginal);

        paramAdjusted();
    }


    /**
     * This can be overridden if a custom GUI is necessary
     */
    protected void setupGUI(ParamSet params, Object otherInfo, boolean showOriginal) {
        setupControlsInColumn(this, params, showOriginal);
    }

    public static void setupControlsInColumn(JPanel panel, ParamSet params, boolean showOriginal) {
        panel.setLayout(new GridBagLayout());

        int row = 0;
        JPanel buttonsPanel = null;

        GridBagHelper gridBagHelper = new GridBagHelper(panel);

        for (GUIParam param : params) {
            JComponent control = param.createGUI();

            if (param instanceof ActionParam) { // all the buttons go in one row
                if (buttonsPanel == null) {
                    buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    gridBagHelper.addOnlyControlToRow(buttonsPanel, row);
                }
                buttonsPanel.add(control);
                control.setName(param.getName());
            } else {
                int nrOfGridBagCols = param.getNrOfGridBagCols();
                if (nrOfGridBagCols == 1) {
                    gridBagHelper.addOnlyControlToRow(control, row);
                } else if (nrOfGridBagCols == 2) {
                    gridBagHelper.addLabel(param.getName() + ':', 0, row);
                    gridBagHelper.addLastControl(control);
                }
            }

            row++;
        }
        if (showOriginal) {
            gridBagHelper.addLabel("Show Original:", 0, row);

            JCheckBox showOriginalCB = new JCheckBox();
            showOriginalCB.addActionListener(e -> Utils.setShowOriginal(showOriginalCB.isSelected()));

            gridBagHelper.addLastControl(showOriginalCB);
        }
    }

    @Override
    public void paramAdjusted() {
        super.executeFilterPreview();
    }

    public static void setResetParams(boolean resetParams) {
        ParametrizedAdjustPanel.resetParams = resetParams;
    }
}