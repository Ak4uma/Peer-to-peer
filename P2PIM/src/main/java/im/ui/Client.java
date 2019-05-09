/*
 * Decompiled with CFR 0.137.
 */
package im.ui;

import im.control.Control;
import im.prefs.Preferences;
import im.ui.SplashWindow;
import im.ui.custom.EditableComboBox;
import im.ui.frames.GroupWindow;
import im.ui.frames.IMWindow;
import im.ui.frames.PreferencesWindow;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class Client
extends JFrame
implements ActionListener,
MouseListener,
WindowListener {
    private JMenuBar mMenuBar;
    private JMenu mFileMenu;
    private JMenu mActionsMenu;
    private JMenu mOptionsMenu;
    private JMenu mHelp;
    private JMenuItem mIM;
    private JMenuItem mGroup;
    private JMenuItem mExit;
    private JMenuItem mAbout;
    private JMenuItem mLicense;
    private JMenuItem mPreferences;
    private JButton mIMButton;
    private JButton mNewGroupButton;
    private JButton mSignOffButton;
    private ImageIcon mLogo;
    private ImageIcon mScaledLogo;
    private ImageIcon mTopHalfOfLogo;
    private ImageIcon mIMIcon;
    private ImageIcon mGroupIcon;
    private ImageIcon mSignOffIcon;
    private JLabel mUserNameLabel;
    private EditableComboBox mUserNameText;
    private JButton mLogin;
    private JButton mCancel;
    private JCheckBox mAutoLogin;
    private JPanel mAboutPanel;
    private JPanel mLicensePanel;
    private JTree mContacts;
    private JScrollPane mTreeScroll;
    private JPanel mMainPanel;
    private JPanel mLoginPanel;
    private JPanel mCardPanel;
    private CardLayout mCardLayout;
    private Border mRaisedBorder;
    private Border mLoweredBorder;
    private HashMap mIMWindows;
    private HashMap mGroupWindows;
    private JTextArea mProfileText;
    private JScrollPane mProfilePane;
    private Color mLogoBlue;
    private Preferences mPrefs;
    private ArrayList mUsernameHistory;
    private static Control mController;
    public static String userName;
    private static final Dimension SIZE_32;
    private static final String SIGN_ON_MESSAGE = "Username:";
    private static final String LOGIN_VIEW = "login";
    private static final String CONTACTS_VIEW = "contacts";
    private static final String LAST_USERNAME = "username.recent";
    private static final String HISTORY_USERNAME = "username.history";
    private static final String AUTO_LOGIN = "auto.login";
    private static final int PAST_USERNAMES = 5;

    public Client(Control control) {
        super("P2PIM");
        this.mLogo = new ImageIcon(ClassLoader.getSystemResource("im/images/p2pIMBlue.gif"));
        this.mScaledLogo = new ImageIcon(this.mLogo.getImage().getScaledInstance(70, -1, 1));
        this.mTopHalfOfLogo = new ImageIcon(ClassLoader.getSystemResource("im/images/topLogotrans.gif"));
        this.setIconImage(this.mTopHalfOfLogo.getImage().getScaledInstance(16, 16, 1));
        this.mLogoBlue = new Color(82, 189, 255);
        UIManager.getDefaults().put("ScrollBar.thumb", this.mLogoBlue);
        UIManager.getDefaults().put("ScrollBar.thumbHighlight", this.mLogoBlue);
        UIManager.getDefaults().put("ScrollBar.thumbDarkShadow", this.mLogoBlue);
        UIManager.getDefaults().put("ScrollBar.thumbLightShadow", this.mLogoBlue);
        Thread thread = new Thread(){

            public void run() {
                SplashWindow.showInstance(Client.this.mLogo);
            }
        };
        thread.start();
        mController = control;
        this.initComponents();
        this.mIMWindows = new HashMap();
        this.mGroupWindows = new HashMap();
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                Client.this.savePreferences();
                mController.logout();
                System.exit(0);
            }
        });
        this.setSize(new Dimension(200, 425));
        this.setResizable(false);
        this.show();
        if (this.mAutoLogin.isSelected()) {
            this.login();
        }
    }

    public Client(String string) {
        super(string);
        this.initComponents();
        this.mIMWindows = new HashMap();
        this.mGroupWindows = new HashMap();
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        this.setSize(new Dimension(200, 425));
        this.setResizable(false);
        this.show();
    }

    private void initComponents() {
        this.mPrefs = mController.getMainPrefs();
        this.sortUsernameHistory(this.mPrefs.getMostRecentUsername());
        this.mIMIcon = new ImageIcon(ClassLoader.getSystemResource("im/ui/icons/iconfactory/PlainPeople.gif"));
        this.mGroupIcon = new ImageIcon(ClassLoader.getSystemResource("im/ui/icons/iconfactory/MorePeople.gif"));
        this.mSignOffIcon = new ImageIcon(ClassLoader.getSystemResource("im/ui/icons/iconfactory/Hangman.gif"));
        this.mRaisedBorder = BorderFactory.createEtchedBorder();
        this.mLoweredBorder = BorderFactory.createBevelBorder(1);
        this.mMenuBar = new JMenuBar();
        this.mFileMenu = new JMenu("File");
        this.mActionsMenu = new JMenu("Actions");
        this.mHelp = new JMenu("Help");
        this.mIM = new JMenuItem("Send IM");
        this.mIM.setEnabled(false);
        this.mIM.addActionListener(this);
        this.mIM.setAccelerator(KeyStroke.getKeyStroke(73, 2));
        this.mGroup = new JMenuItem("Join Group");
        this.mGroup.setEnabled(false);
        this.mGroup.addActionListener(this);
        this.mGroup.setAccelerator(KeyStroke.getKeyStroke(71, 2));
        this.mExit = new JMenuItem("Exit");
        this.mExit.addActionListener(this);
        this.mExit.setAccelerator(KeyStroke.getKeyStroke(88, 2));
        this.mPreferences = new JMenuItem("Preferences");
        this.mPreferences.addActionListener(this);
        this.mAbout = new JMenuItem("About P2PIM");
        this.mAbout.addActionListener(this);
        this.mLicense = new JMenuItem("License Info");
        this.mLicense.addActionListener(this);
        this.mFileMenu.add(this.mPreferences);
        this.mFileMenu.add(new JSeparator());
        this.mFileMenu.add(this.mExit);
        this.mActionsMenu.add(this.mIM);
        this.mActionsMenu.add(this.mGroup);
        this.mHelp.add(this.mAbout);
        this.mHelp.add(this.mLicense);
        this.mMenuBar.add(this.mFileMenu);
        this.mMenuBar.add(this.mActionsMenu);
        this.mMenuBar.add(this.mHelp);
        this.setJMenuBar(this.mMenuBar);
        DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode("Users");
        defaultMutableTreeNode.add(new DefaultMutableTreeNode("User1"));
        this.mContacts = new JTree(defaultMutableTreeNode);
        this.mContacts.addMouseListener(this);
        this.mContacts.setPreferredSize(new Dimension(170, 295));
        this.mContacts.getSelectionModel().setSelectionMode(1);
        this.mLoginPanel = this.doLayoutLoginPanel();
        this.mTreeScroll = new JScrollPane(this.mContacts);
        this.mTreeScroll.setPreferredSize(new Dimension(170, 300));
        this.mTreeScroll.setVerticalScrollBarPolicy(20);
        this.mTreeScroll.setHorizontalScrollBarPolicy(30);
        this.mCardLayout = new CardLayout();
        this.mCardPanel = new JPanel(this.mCardLayout);
        this.mCardPanel.add(LOGIN_VIEW, this.mLoginPanel);
        this.mCardPanel.add(CONTACTS_VIEW, this.mTreeScroll);
        this.mCardLayout.show(this.mCardPanel, LOGIN_VIEW);
        this.mIMButton = new JButton(this.mIMIcon);
        this.mIMButton.setBorder(null);
        this.mIMButton.setEnabled(false);
        this.mIMButton.setToolTipText("Send IM");
        this.mIMButton.setPreferredSize(SIZE_32);
        this.mIMButton.addActionListener(this);
        this.mIMButton.addMouseListener(this);
        this.mNewGroupButton = new JButton(this.mGroupIcon);
        this.mNewGroupButton.setBorder(null);
        this.mNewGroupButton.setEnabled(false);
        this.mNewGroupButton.setToolTipText("New Group");
        this.mNewGroupButton.setPreferredSize(SIZE_32);
        this.mNewGroupButton.addActionListener(this);
        this.mNewGroupButton.addMouseListener(this);
        this.mSignOffButton = new JButton(this.mSignOffIcon);
        this.mSignOffButton.setBorder(null);
        this.mSignOffButton.setEnabled(false);
        this.mSignOffButton.setToolTipText("Logout");
        this.mSignOffButton.setPreferredSize(SIZE_32);
        this.mSignOffButton.addActionListener(this);
        this.mSignOffButton.addMouseListener(this);
        this.mAboutPanel = this.doLayoutAboutPanel();
        this.mLicensePanel = this.doLayoutLicensePanel();
        this.doCreateProfilePane();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        this.mMainPanel = new JPanel(gridBagLayout);
        gridBagConstraints.fill = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.weightx = 2.0;
        gridBagLayout.setConstraints(this.mCardPanel, gridBagConstraints);
        gridBagConstraints.anchor = 17;
        gridBagConstraints.fill = 0;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(this.mIMButton, gridBagConstraints);
        gridBagConstraints.anchor = 10;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 1;
        gridBagLayout.setConstraints(this.mNewGroupButton, gridBagConstraints);
        gridBagConstraints.anchor = 13;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridx = 2;
        gridBagLayout.setConstraints(this.mSignOffButton, gridBagConstraints);
        this.mMainPanel.add(this.mCardPanel);
        this.mMainPanel.add(this.mIMButton);
        this.mMainPanel.add(this.mNewGroupButton);
        this.mMainPanel.add(this.mSignOffButton);
        this.getContentPane().add(this.mMainPanel);
    }

    public void addContact(String string) {
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel)this.mContacts.getModel();
        defaultTreeModel.insertNodeInto(new DefaultMutableTreeNode(string.trim()), (DefaultMutableTreeNode)defaultTreeModel.getRoot(), ((DefaultMutableTreeNode)defaultTreeModel.getRoot()).getChildCount());
        this.mContacts.expandRow(0);
        IMWindow iMWindow = (IMWindow)this.mIMWindows.get(string);
        if (iMWindow != null) {
            iMWindow.toggleSend(true);
        }
    }

    public void removeContact(String string) {
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel)this.mContacts.getModel();
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode)defaultTreeModel.getRoot();
        Enumeration enumeration = defaultMutableTreeNode.children();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode defaultMutableTreeNode2 = (DefaultMutableTreeNode)enumeration.nextElement();
            if (!defaultMutableTreeNode2.toString().equals(string.trim())) continue;
            defaultTreeModel.removeNodeFromParent(defaultMutableTreeNode2);
            break;
        }
        IMWindow iMWindow = (IMWindow)this.mIMWindows.get(string);
        if (iMWindow != null) {
            iMWindow.toggleSend(false);
        }
    }

    private void doCreateProfilePane() {
        this.mProfileText = new JTextArea(20, 25);
        this.mProfileText.setLineWrap(true);
        this.mProfileText.setWrapStyleWord(true);
        this.mProfileText.setEditable(false);
        this.mProfilePane = new JScrollPane(this.mProfileText, 20, 31);
    }

    private JPanel doLayoutLoginPanel() {
        String string = this.mPrefs.getMostRecentUsername();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        Dimension dimension = new Dimension(65, 21);
        this.mUserNameLabel = new JLabel("Username: ");
        this.mUserNameText = new EditableComboBox(string, this.mUsernameHistory);
        this.mUserNameText.setPreferredSize(new Dimension(160, 21));
        ((JTextField)this.mUserNameText.getEditor().getEditorComponent()).addActionListener(this);
        String string2 = this.mPrefs.getAutoLogin();
        this.mAutoLogin = new JCheckBox("Auto-Login");
        this.mAutoLogin.addActionListener(this);
        if (string2 != null) {
            this.mAutoLogin.setSelected(new Boolean(string2));
        }
        this.mLogin = new JButton("Login");
        this.mLogin.setBorder(this.mRaisedBorder);
        this.mLogin.setPreferredSize(dimension);
        this.mLogin.addActionListener(this);
        this.mCancel = new JButton("Cancel");
        this.mCancel.setBorder(this.mRaisedBorder);
        this.mCancel.setPreferredSize(dimension);
        this.mCancel.addActionListener(this);
        JPanel jPanel = new JPanel(gridBagLayout);
        gridBagConstraints.anchor = 17;
        gridBagConstraints.fill = 0;
        gridBagConstraints.insets = new Insets(1, 12, 1, 12);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagLayout.setConstraints(this.mUserNameLabel, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        gridBagLayout.setConstraints(this.mUserNameText, gridBagConstraints);
        gridBagConstraints.insets = new Insets(1, 12, 12, 12);
        gridBagConstraints.gridy = 2;
        gridBagLayout.setConstraints(this.mAutoLogin, gridBagConstraints);
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagLayout.setConstraints(this.mLogin, gridBagConstraints);
        gridBagConstraints.anchor = 13;
        gridBagConstraints.gridx = 1;
        gridBagLayout.setConstraints(this.mCancel, gridBagConstraints);
        jPanel.add(this.mUserNameLabel);
        jPanel.add(this.mUserNameText);
        jPanel.add(this.mAutoLogin);
        jPanel.add(this.mLogin);
        jPanel.add(this.mCancel);
        return jPanel;
    }

    private JPanel doLayoutLicensePanel() {
        Object object;
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JPanel jPanel = new JPanel(gridBagLayout);
        JTextArea jTextArea = new JTextArea(15, 55);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setEditable(false);
        try {
            object = ClassLoader.getSystemResourceAsStream("im/LICENSE");
            jTextArea.read(new InputStreamReader((InputStream)object), null);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        object = new JScrollPane(jTextArea, 20, 31);
        gridBagLayout.setConstraints((Component)object, gridBagConstraints);
        jPanel.add((Component)object);
        return jPanel;
    }

    private JPanel doLayoutAboutPanel() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JPanel jPanel = new JPanel(gridBagLayout);
        JTextArea jTextArea = new JTextArea(10, 20);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setText("P2PIM .\n\nDevelopers:\n\nAkram Alkhaled\nAbdulhadi Zakkour");
        JScrollPane jScrollPane = new JScrollPane(jTextArea, 20, 31);
        gridBagLayout.setConstraints(jScrollPane, gridBagConstraints);
        jPanel.add(jScrollPane);
        return jPanel;
    }

    private String getSelectedContact() {
        String string = null;
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode)this.mContacts.getLastSelectedPathComponent();
        if (defaultMutableTreeNode != null && !defaultMutableTreeNode.equals(this.mContacts.getModel().getRoot())) {
            string = defaultMutableTreeNode.toString();
        }
        return string;
    }

    private void createGroupWindow(String string) {
        if (string == null) {
            string = this.getSelectedContact();
        }
        GroupWindow groupWindow = new GroupWindow(string);
        this.mGroupWindows.put(string, groupWindow);
        groupWindow.addWindowListener(this);
        groupWindow.setIconImage(this.mTopHalfOfLogo.getImage().getScaledInstance(16, 16, 1));
        groupWindow.show();
        this.joinGroup();
        this.mNewGroupButton.setEnabled(false);
    }

    private void login() {
        userName = this.mUserNameText.getText();
        if (userName.length() == 0) {
            JOptionPane.showMessageDialog(this, "You must provide a Username.", "Error", 0);
        } else {
            this.mCardLayout.show(this.mCardPanel, CONTACTS_VIEW);
            this.mIM.setEnabled(true);
            this.mGroup.setEnabled(true);
            this.mIMButton.setEnabled(true);
            this.mNewGroupButton.setEnabled(true);
            this.mSignOffButton.setEnabled(true);
            if (this.mUserNameText.indexOf(userName) < 0) {
                this.mUserNameText.addItem(userName);
            }
            this.mPrefs.setMostRecentUsername(userName);
            this.mPrefs.setAutoLogin(this.mAutoLogin.isSelected());
            mController.login(userName);
        }
    }

    public void logout(String string) {
        if (string != null) {
            JOptionPane.showMessageDialog(this, string, "Logout", 0);
        }
        userName = null;
        Iterator iterator = this.mGroupWindows.values().iterator();
        if (iterator.hasNext()) {
            GroupWindow groupWindow = (GroupWindow)iterator.next();
            groupWindow.dispose();
        }
        ((DefaultTreeModel)this.mContacts.getModel()).setRoot(new DefaultMutableTreeNode("Users"));
        this.mUserNameText.setText(this.mPrefs.getMostRecentUsername());
        this.mCardLayout.show(this.mCardPanel, LOGIN_VIEW);
        this.mIM.setEnabled(false);
        this.mGroup.setEnabled(false);
        this.mIMButton.setEnabled(false);
        this.mNewGroupButton.setEnabled(false);
        this.mSignOffButton.setEnabled(false);
        mController.logout();
    }

    private IMWindow createIMWindow(String string) {
        IMWindow iMWindow = null;
        if (string == null) {
            string = this.getSelectedContact();
        }
        if (string == null) {
            JOptionPane.showMessageDialog(this, "Please select a network contact.", "First select a user", 0);
        } else {
            iMWindow = (IMWindow)this.mIMWindows.get(string);
            if (iMWindow == null) {
                iMWindow = new IMWindow(string, Color.red, Color.blue);
                this.mIMWindows.put(string, iMWindow);
                iMWindow.addWindowListener(this);
                iMWindow.setIconImage(this.mTopHalfOfLogo.getImage().getScaledInstance(16, 16, 1));
                iMWindow.show();
            } else {
                iMWindow.requestFocus();
            }
        }
        return iMWindow;
    }

    public static void sendMessage(String string, String string2) {
        mController.sendText(string, string2);
    }

    public static void sendFormattedMessage(String string, Object object) {
        mController.sendFormattedText(string, object);
    }

    public void receivedMessage(String string, String string2) {
        IMWindow iMWindow = (IMWindow)this.mIMWindows.get(string);
        if (iMWindow == null) {
            iMWindow = this.createIMWindow(string);
        }
        iMWindow.receivedMessage(string.trim(), string2.trim());
    }

    public void receivedMessage(String string, Object object) {
        IMWindow iMWindow = (IMWindow)this.mIMWindows.get(string);
        if (iMWindow == null) {
            iMWindow = this.createIMWindow(string);
        }
        iMWindow.postMessage(string.trim(), (Document)object);
    }

    public static void sendGroupMessage(String string, String string2) {
        mController.sendGroupText(string, string2);
    }

    public void receivedGroupMessage(String string, String string2) {
        Iterator iterator = this.mGroupWindows.values().iterator();
        if (iterator.hasNext()) {
            GroupWindow groupWindow = (GroupWindow)iterator.next();
            groupWindow.postMessage(string.trim(), string2.trim());
        }
    }

    public void addGroupMember(String string) {
        Iterator iterator = this.mGroupWindows.values().iterator();
        if (iterator.hasNext()) {
            GroupWindow groupWindow = (GroupWindow)iterator.next();
            groupWindow.addMember(string.trim());
        }
    }

    public void removeGroupMember(String string) {
        Iterator iterator = this.mGroupWindows.values().iterator();
        if (iterator.hasNext()) {
            GroupWindow groupWindow = (GroupWindow)iterator.next();
            groupWindow.removeMember(string.trim());
        }
    }

    private void leaveGroup() {
        mController.leaveGroup();
    }

    private void joinGroup() {
        mController.joinGroup();
    }

    private void savePreferences() {
        for (int i = 1; i < this.mUsernameHistory.size() && i < 5; ++i) {
            this.mPrefs.setUsernameHistory((String)this.mUsernameHistory.get(i), i);
        }
        this.mPrefs.savePreferences();
    }

    private void sortUsernameHistory(String string) {
        this.mUsernameHistory = new ArrayList();
        if (string != null) {
            this.mUsernameHistory.add(string);
            for (int i = 1; i < 5; ++i) {
                String string2 = this.mPrefs.getUsernameHistory(i);
                if (string2 == null) break;
                this.mUsernameHistory.add(string2);
            }
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(this.mSignOffButton)) {
            this.logout(null);
        } else if (actionEvent.getSource().equals(this.mExit) || actionEvent.getSource().equals(this.mCancel)) {
            this.savePreferences();
            mController.logout();
            System.exit(0);
        } else if (actionEvent.getSource().equals(this.mLogin) || actionEvent.getSource().equals(this.mUserNameText.getEditor().getEditorComponent())) {
            this.login();
        } else if (actionEvent.getSource().equals(this.mIMButton) || actionEvent.getSource().equals(this.mIM)) {
            this.createIMWindow(null);
        } else if (actionEvent.getSource().equals(this.mNewGroupButton) || actionEvent.getSource().equals(this.mGroup)) {
            this.createGroupWindow(null);
        } else if (actionEvent.getSource().equals(this.mAutoLogin)) {
            this.mPrefs.setAutoLogin(this.mAutoLogin.isSelected());
        } else if (actionEvent.getSource().equals(this.mAbout)) {
            JOptionPane.showMessageDialog(this, this.mAboutPanel, "About P2PIM", 1, this.mScaledLogo);
        } else if (actionEvent.getSource().equals(this.mLicense)) {
            JOptionPane.showMessageDialog(this, this.mLicensePanel, "P2PIM License", 1, this.mScaledLogo);
        } else if (actionEvent.getSource().equals(this.mPreferences)) {
            PreferencesWindow preferencesWindow = new PreferencesWindow(this.mTopHalfOfLogo, this.mPrefs);
        }
    }

    public void mouseEntered(MouseEvent mouseEvent) {
        JButton jButton;
        if (mouseEvent.getSource() instanceof JButton && (jButton = (JButton)mouseEvent.getSource()).isEnabled()) {
            jButton.setBorder(this.mRaisedBorder);
        }
    }

    public void mouseExited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof JButton) {
            ((JButton)mouseEvent.getSource()).setBorder(null);
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        JButton jButton;
        if (mouseEvent.getSource() instanceof JButton && (jButton = (JButton)mouseEvent.getSource()).isEnabled()) {
            jButton.setBorder(this.mLoweredBorder);
        }
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        JButton jButton;
        if (mouseEvent.getSource() instanceof JButton && (jButton = (JButton)mouseEvent.getSource()).isEnabled()) {
            jButton.setBorder(this.mRaisedBorder);
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        TreePath treePath;
        TreeNode treeNode;
        if (mouseEvent.getSource().equals(this.mContacts)) {
            treePath = this.mContacts.getPathForLocation(mouseEvent.getPoint().x, mouseEvent.getPoint().y);
            if (treePath == null) {
                return;
            }
            treeNode = (TreeNode)treePath.getLastPathComponent();
            if (!treeNode.isLeaf()) {
                return;
            }
        } else {
            return;
        }
        if (mouseEvent.getClickCount() == 2) {
            if (treePath.equals(this.mContacts.getSelectionPath())) {
                this.createIMWindow(treeNode.toString());
            }
        } else if (mouseEvent.getModifiers() == 4) {
            final String string = treeNode.toString();
            this.mProfileText.setText(mController.getProfile(string));
            JPopupMenu jPopupMenu = new JPopupMenu();
            JMenuItem jMenuItem = new JMenuItem();
            jMenuItem.setAction(new AbstractAction("Get Profile..."){

                public void actionPerformed(ActionEvent actionEvent) {
                    JOptionPane.showMessageDialog(((JMenuItem)actionEvent.getSource()).getParent(), Client.this.mProfilePane, string + "'s Profile", 1, Client.this.mScaledLogo);
                }
            });
            jPopupMenu.add(jMenuItem);
            jPopupMenu.show(this.mContacts, mouseEvent.getPoint().x, mouseEvent.getPoint().y);
        }
    }

    public void windowOpened(WindowEvent windowEvent) {
        if (windowEvent.getSource() instanceof GroupWindow) {
            ((GroupWindow)windowEvent.getSource()).focusTextField();
        }
    }

    public void windowClosing(WindowEvent windowEvent) {
    }

    public void windowIconified(WindowEvent windowEvent) {
    }

    public void windowDeiconified(WindowEvent windowEvent) {
    }

    public void windowActivated(WindowEvent windowEvent) {
    }

    public void windowDeactivated(WindowEvent windowEvent) {
    }

    public void windowClosed(WindowEvent windowEvent) {
        JFrame jFrame = (JFrame)windowEvent.getSource();
        if (jFrame instanceof IMWindow) {
            this.mIMWindows.remove(jFrame.getName());
        } else {
            this.mGroupWindows.remove(jFrame.getName());
            this.leaveGroup();
            if (userName != null) {
                this.mNewGroupButton.setEnabled(true);
            }
        }
    }

    public static void main(String[] arrstring) {
        try {
            Client client = new Client("P2PIM");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    static {
        SIZE_32 = new Dimension(32, 32);
    }

}

