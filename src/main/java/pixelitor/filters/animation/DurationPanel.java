/*
 * Copyright 2009-2014 Laszlo Balazs-Csiki
 *
 * This file is part of Pixelitor. Pixelitor is free software: you
 * can redistribute it and/or modify it under the terms of the GNU
 * General Public License, version 3 as published by the Free
 * Software Foundation.
 *
 * Pixelitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pixelitor.  If not, see <http://www.gnu.org/licenses/>.
 */
package pixelitor.filters.animation;

import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import pixelitor.utils.GridBagHelper;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DurationPanel extends JPanel {
    private JTextField nrSecondsTF = new JTextField("2", 3);
    private JTextField fpsTF = new JTextField("24", 3);
    private int nrFrames;
    private final JLabel nrFramesLabel;
    private double fps;
    private TweenWizard wizard;
    private Interpolation interpolation;
    private final JComboBox<Interpolation> ipCB;

    public DurationPanel(TweenWizard wizard) {
        super(new GridBagLayout());
        this.wizard = wizard;

        GridBagHelper.addLabel(this, "Number of seconds:", 0, 0);
        GridBagHelper.addControl(this, nrSecondsTF);

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateCalculations();
            }
        };
        nrSecondsTF.addKeyListener(keyAdapter);

        GridBagHelper.addLabel(this, "Frames per Second:", 0, 1);
        GridBagHelper.addControl(this, fpsTF);

        fpsTF.addKeyListener(keyAdapter);

        nrFramesLabel = new JLabel();
        updateCalculations();
        GridBagHelper.addLabel(this, "Number of Frames:", 0, 2);
        GridBagHelper.addControl(this, nrFramesLabel);

        EnumComboBoxModel<Interpolation> ipCBM = new EnumComboBoxModel<>(Interpolation.class);
        ipCB = new JComboBox<>(ipCBM);

        GridBagHelper.addLabel(this, "Interpolation:", 0, 3);
        GridBagHelper.addControl(this, ipCB);

    }

    private void updateCalculations() {
        try {
            double nrSeconds = Double.parseDouble(nrSecondsTF.getText().trim());
            fps = Double.parseDouble(fpsTF.getText().trim());
            nrFrames = (int) (nrSeconds * fps);
            nrFramesLabel.setText(String.valueOf(nrFrames));
            wizard.setNextButtonEnabled(true);
        } catch (Exception e) {
            // disable the next button in case of any formatting problem
            wizard.setNextButtonEnabled(false);
        }
    }


    public int getNumFrames() {
        return nrFrames;
    }

    public int getMillisBetweenFrames() {
        return (int) (1000.0 / fps);
    }

    public Interpolation getInterpolation() {
        return (Interpolation) ipCB.getSelectedItem();
    }
}