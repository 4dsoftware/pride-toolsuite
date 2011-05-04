package uk.ac.ebi.pride.gui.component.chart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * <p>A.</p>
 *
 * @author Antonio Fabregat
 * Date: 23-ago-2010
 * Time: 14:25:06
 */
class ChartButton extends JButton implements MouseListener {

    ChartButton(Icon icon, String tooltip) {
        super(icon);
        setToolTipText(tooltip);
        setContentAreaFilled(false);
        setBackground(Color.WHITE);
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //Nothing here
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        //Nothing here
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        //Nothing here
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        Component cmp = mouseEvent.getComponent();
        if (cmp instanceof ChartButton) {
            ChartButton b = (ChartButton) cmp;
            b.setContentAreaFilled(true);
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        Component cmp = mouseEvent.getComponent();
        if (cmp instanceof ChartButton) {
            ChartButton b = (ChartButton) cmp;
            b.setContentAreaFilled(false);
        }
    }
}
