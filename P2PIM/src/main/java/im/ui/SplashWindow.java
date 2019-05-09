/*
 * Decompiled with CFR 0.137.
 */
package im.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.Border;

public class SplashWindow
extends JWindow {
    private static SplashWindow mInstance;
    private static final int SPLASH_DELAY = 3000;

    private SplashWindow(Icon icon) {
        JLabel jLabel = new JLabel(icon);
        JLabel jLabel2 = new JLabel("version " + System.getProperty("P2PIM.release.id"), 0);
        jLabel2.setForeground(new Color(82, 189, 255));
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setBackground(Color.white);
        jPanel.setOpaque(true);
        jPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        jPanel.add((Component)jLabel, "Center");
        jPanel.add((Component)jLabel2, "South");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dimension2 = jPanel.getPreferredSize();
        this.setLocation(dimension.width / 2 - dimension2.width / 2, dimension.height / 2 - dimension2.height / 2);
        this.addMouseListener(new MouseAdapter(){

            public void mousePressed(MouseEvent mouseEvent) {
                mInstance.dispose();
            }
        });
        this.getContentPane().add(jPanel);
        this.pack();
    }

    public static void showInstance(Icon icon) {
        if (mInstance == null) {
            mInstance = new SplashWindow(icon);
        }
        mInstance.setSize(new Dimension(170, 235));
        mInstance.show();
        Thread thread = new Thread(){

            public void run() {
                try {
                    Thread.sleep(3000L);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                mInstance.hide();
                mInstance = null;
            }
        };
        thread.start();
    }

}

