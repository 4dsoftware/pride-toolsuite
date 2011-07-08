package uk.ac.ebi.pride.gui.component;

import org.jdesktop.swingx.border.DropShadowBorder;
import uk.ac.ebi.pride.gui.GUIUtilities;
import uk.ac.ebi.pride.gui.PrideInspectorContext;
import uk.ac.ebi.pride.gui.utils.GraphicsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;

/**
 * A panel displays pride's logo and showing a loading status
 * <p/>
 * User: rwang
 * Date: 14/03/11
 * Time: 16:41
 */
public class PrideInspectorLoadingPanel extends PrideInspectorPanel {

    public PrideInspectorLoadingPanel() {
        initComponents();
    }

    private void initComponents() {
        PrideInspectorContext context = (PrideInspectorContext) uk.ac.ebi.pride.gui.desktop.Desktop.getInstance().getDesktopContext();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;

        JPanel panel = new RectPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.white);
        panel.setPreferredSize(new Dimension(350, 150));

        ImageIcon loadingIcon = (ImageIcon) GUIUtilities.loadIcon(context.getProperty("loading.large.icon"));
        JLabel label = new JLabel();
        label.setOpaque(false);
        label.setIcon(loadingIcon);
        label.setText(context.getProperty("loading.title"));
        label.setFont(label.getFont().deriveFont(18f));
        panel.add(label, c);
        this.add(panel, c);
        this.setBackground(Color.white);
    }

    private class RectPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            paintDropShadow(g);
            paintWindow(g);
        }

        private void paintDropShadow(Graphics g) {
            // size
            int xPos = 10;
            int yPos = 10;
            int width = getWidth() - 10;
            int height = getHeight() - 10;

            // create a picture with an alpha channel
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // draw rectangle
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // set color
            g2.setColor(Color.gray);

            g2.fillRoundRect(0, 0, width, height, 30, 30);

            g2.dispose();

            image = GraphicsUtils.getGaussianBlurFilter(3, true).filter(image, null);
            image = GraphicsUtils.getGaussianBlurFilter(3, false).filter(image, null);

            g.drawImage(image, xPos, yPos, null);
        }

        private void paintWindow(Graphics g) {
            // size
            int xPos = 7;
            int yPos = 7;
            int width = getWidth() - 10;
            int height = getHeight() - 10;

            // create a picture with an alpha channel
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // draw rectangle
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.white);
            g2.fillRoundRect(0, 0, width, height, 30, 30);
            g2.setColor(Color.gray);
            g2.drawRoundRect(0, 0, width, height, 30, 30);

            g2.dispose();

            g.drawImage(image, xPos, yPos, null);
        }
    }

}
