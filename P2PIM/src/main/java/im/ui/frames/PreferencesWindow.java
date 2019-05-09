/*
 * Decompiled with CFR 0.137.
 */
package im.ui.frames;

import im.prefs.Preferences;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PreferencesWindow
extends JFrame
implements ActionListener {
    private JMenuItem mClose;
    private JTextArea mProfile;
    private JButton mOK;
    private JButton mCancel;
    private String mProfileContent;
    private JPanel mMainPanel;
    private Preferences mPrefs;

    public PreferencesWindow(ImageIcon imageIcon, Preferences preferences) {
        super("Preferences");
        this.setIconImage(imageIcon.getImage().getScaledInstance(16, 16, 1));
        this.mPrefs = preferences;
        this.mProfileContent = this.mPrefs.getProfile();
        this.initComponents();
        this.pack();
        this.show();
    }

    private void initComponents() {
        this.doCreateMenuBar();
        JLabel jLabel = new JLabel("Profile");
        this.mProfile = new JTextArea(15, 25);
        if (this.mProfileContent != null) {
            this.mProfile.append(this.mProfileContent);
        }
        this.mProfile.setLineWrap(true);
        this.mProfile.setWrapStyleWord(true);
        JScrollPane jScrollPane = new JScrollPane(this.mProfile, 22, 31);
        this.mOK = new JButton("OK");
        this.mOK.addActionListener(this);
        this.mCancel = new JButton("Cancel");
        this.mCancel.addActionListener(this);
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JPanel jPanel = new JPanel(gridBagLayout);
        gridBagConstraints.anchor = 10;
        gridBagConstraints.fill = 0;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagLayout.setConstraints(this.mOK, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagLayout.setConstraints(this.mCancel, gridBagConstraints);
        jPanel.add(this.mOK);
        jPanel.add(this.mCancel);
        GridBagLayout gridBagLayout2 = new GridBagLayout();
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        this.mMainPanel = new JPanel(gridBagLayout2);
        gridBagConstraints2.anchor = 18;
        gridBagConstraints2.fill = 0;
        gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 0;
        gridBagLayout2.setConstraints(jLabel, gridBagConstraints2);
        gridBagConstraints2.gridx = 1;
        gridBagLayout2.setConstraints(jScrollPane, gridBagConstraints2);
        gridBagConstraints2.anchor = 10;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagLayout2.setConstraints(jPanel, gridBagConstraints2);
        this.mMainPanel.add(jLabel);
        this.mMainPanel.add(jScrollPane);
        this.mMainPanel.add(jPanel);
        this.setContentPane(this.mMainPanel);
    }

    private void doCreateMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("File");
        this.mClose = new JMenuItem("Close");
        this.mClose.addActionListener(this);
        jMenu.add(this.mClose);
        jMenuBar.add(jMenu);
        this.setJMenuBar(jMenuBar);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(this.mOK)) {
            this.mPrefs.setProfile(this.mProfile.getText());
        }
        this.dispose();
    }
}

