/*
 * Decompiled with CFR 0.137.
 */
package im.ui.frames;

import im.prefs.Preferences;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.rtf.RTFEditorKit;

public abstract class AbstractMessageWindow
extends JFrame
implements ActionListener {
    protected JMenuBar mMenuBar;
    protected JMenu mFile;
    protected JMenu mEdit;
    protected JMenuItem mClose;
    protected JCheckBoxMenuItem mMenuBold;
    protected JCheckBoxMenuItem mMenuItalic;
    protected JCheckBoxMenuItem mMenuUnderline;
    protected JTextPane mConversation;
    protected JTextPane mMessage;
    protected RTFEditorKit mKit;
    protected RTFEditorKit mConvKit;
    protected JScrollPane mConversationScroll;
    protected JScrollPane mMessageScroll;
    protected JToggleButton mBold;
    protected JToggleButton mItalic;
    protected JToggleButton mUnderline;
    protected JButton mSend;
    protected JButton mClear;
    protected JComboBox mFonts;
    protected JPanel mMainPanel;
    protected JPanel mMessagePanel;
    protected File mConversationTmp;
    protected File mMessageTmp;
    protected FileInputStream mConversationIn;
    protected FileInputStream mMessageIn;
    protected FileOutputStream mConversationOut;
    protected FileOutputStream mMessageOut;
    protected Color mMyColor;
    protected Color mContactColor;
    protected AbstractAction mSendAction;
    private Border mEtchedBorder;
    protected static final ImageIcon BOLD_24 = new ImageIcon(ClassLoader.getSystemResource("im/ui/icons/sun/text/Bold24.gif"));
    protected static final ImageIcon BOLD_16 = new ImageIcon(ClassLoader.getSystemResource("im/ui/icons/sun/text/Bold16.gif"));
    protected static final ImageIcon ITALIC_24 = new ImageIcon(ClassLoader.getSystemResource("im/ui/icons/sun/text/Italic24.gif"));
    protected static final ImageIcon ITALIC_16 = new ImageIcon(ClassLoader.getSystemResource("im/ui/icons/sun/text/Italic16.gif"));
    protected static final ImageIcon UNDERLINE_24 = new ImageIcon(ClassLoader.getSystemResource("im/ui/icons/sun/text/Underline24.gif"));
    protected static final ImageIcon UNDERLINE_16 = new ImageIcon(ClassLoader.getSystemResource("im/ui/icons/sun/text/Underline16.gif"));
    protected static final ImageIcon SEND = new ImageIcon(ClassLoader.getSystemResource("im/ui/icons/iconfactory/ArrowRight.gif"));
    protected static final ImageIcon CLEAR = new ImageIcon(ClassLoader.getSystemResource("im/ui/icons/sun/general/Remove24.gif"));
    protected static final Dimension SIZE_32 = new Dimension(32, 32);
    protected static final Dimension SIZE_24 = new Dimension(24, 24);
    protected static final Dimension IM_SIZE = new Dimension(365, 325);
    protected static final Dimension GROUP_SIZE = new Dimension(700, 500);

    public AbstractMessageWindow(String string, Color color, Color color2) {
        super(string);
        this.setDefaultCloseOperation(2);
        this.mEtchedBorder = BorderFactory.createEtchedBorder();
        if (color == null && color2 == null) {
            this.mMyColor = Color.blue;
            this.mContactColor = Color.blue;
        } else {
            this.mMyColor = color;
            this.mContactColor = color2;
        }
        try {
            File file = Preferences.findPrefsDirectory();
            this.mConversationTmp = File.createTempFile("cnv", ".p2p", file);
            this.mMessageTmp = File.createTempFile("msg", ".p2p", file);
            this.mConversationTmp.deleteOnExit();
            this.mMessageTmp.deleteOnExit();
            this.mConversationIn = new FileInputStream(this.mConversationTmp);
            this.mMessageIn = new FileInputStream(this.mMessageTmp);
            this.mConversationOut = new FileOutputStream(this.mConversationTmp);
            this.mMessageOut = new FileOutputStream(this.mMessageTmp);
        }
        catch (Exception exception) {
            System.out.println("Error Initializing log files...");
            exception.printStackTrace();
        }
        this.setName(string);
        this.init();
        this.layoutFrame();
    }

    protected void init() {
        this.mMenuBar = new JMenuBar();
        this.mFile = new JMenu("File");
        this.mClose = new JMenuItem("Close");
        this.mClose.addActionListener(this);
        this.mClose.setAccelerator(KeyStroke.getKeyStroke(88, 2));
        this.mEdit = new JMenu("Edit");
        this.mMenuBold = new JCheckBoxMenuItem("Bold", BOLD_16);
        this.mMenuBold.addActionListener(this);
        this.mMenuBold.setAccelerator(KeyStroke.getKeyStroke(66, 2));
        this.mMenuItalic = new JCheckBoxMenuItem("Italic", ITALIC_16);
        this.mMenuItalic.addActionListener(this);
        this.mMenuItalic.setAccelerator(KeyStroke.getKeyStroke(73, 2));
        this.mMenuUnderline = new JCheckBoxMenuItem("Underline", UNDERLINE_16);
        this.mMenuUnderline.addActionListener(this);
        this.mMenuUnderline.setAccelerator(KeyStroke.getKeyStroke(85, 2));
        this.mFile.add(this.mClose);
        this.mEdit.add(this.mMenuBold);
        this.mEdit.add(this.mMenuItalic);
        this.mEdit.add(this.mMenuUnderline);
        this.mMenuBar.add(this.mFile);
        this.mMenuBar.add(this.mEdit);
        this.setJMenuBar(this.mMenuBar);
        this.mBold = new JToggleButton(BOLD_24);
        this.mBold.addActionListener(this);
        this.mBold.setPreferredSize(SIZE_24);
        this.mItalic = new JToggleButton(ITALIC_24);
        this.mItalic.addActionListener(this);
        this.mItalic.setPreferredSize(SIZE_24);
        this.mUnderline = new JToggleButton(UNDERLINE_24);
        this.mUnderline.addActionListener(this);
        this.mUnderline.setPreferredSize(SIZE_24);
        this.mConversation = new JTextPane();
        this.mConversation.setEditable(false);
        this.mConversationScroll = new JScrollPane(this.mConversation, 20, 30);
        this.mMessage = new JTextPane();
        this.mMessage.grabFocus();
        this.mMessageScroll = new JScrollPane(this.mMessage, 20, 30);
        this.mConvKit = new RTFEditorKit();
        this.mKit = new RTFEditorKit();
        this.mMessage.setEditorKit(this.mKit);
        this.mSendAction = new AbstractAction(){

            public void actionPerformed(ActionEvent actionEvent) {
                if (AbstractMessageWindow.this.mSend.isEnabled()) {
                    AbstractMessageWindow.this.sendMessage();
                }
            }
        };
        this.initStyles();
        this.mSend = new JButton("Send", SEND);
        this.mSend.setBorder(this.mEtchedBorder);
        this.mSend.addActionListener(this);
        this.mSend.setPreferredSize(SIZE_32);
        this.mClear = new JButton(CLEAR);
        this.mClear.setBorder(this.mEtchedBorder);
        this.mClear.addActionListener(this);
        this.mClear.setPreferredSize(SIZE_32);
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] arrstring = graphicsEnvironment.getAvailableFontFamilyNames();
        this.mFonts = new JComboBox<String>(arrstring);
        this.mFonts.setBorder(this.mEtchedBorder);
        this.mFonts.addActionListener(this);
        this.mFonts.removeItem("Arial");
        this.mFonts.insertItemAt("Arial", 0);
        this.mFonts.setSelectedIndex(0);
        this.mMainPanel = new JPanel(new BorderLayout());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void dispose() {
        super.dispose();
        try {
            try {
                this.mConversationIn.close();
                this.mMessageIn.close();
                this.mConversationOut.flush();
                this.mConversationOut.close();
                this.mMessageOut.flush();
                this.mMessageOut.close();
            }
            catch (Exception exception) {
                exception.printStackTrace();
                Object var3_2 = null;
                this.mConversationTmp.delete();
                this.mMessageTmp.delete();
            }
            Object var3_1 = null;
            this.mConversationTmp.delete();
            this.mMessageTmp.delete();
        }
        catch (Throwable throwable) {
            Object var3_3 = null;
            this.mConversationTmp.delete();
            this.mMessageTmp.delete();
            throw throwable;
        }
    }

    protected void initStyles() {
        Style style = StyleContext.getDefaultStyleContext().getStyle("default");
        Style style2 = this.mConversation.addStyle("regular", style);
        StyleConstants.setFontFamily(style, "SansSerif");
        Style style3 = this.mConversation.addStyle("regular", style);
        StyleConstants.setFontFamily(style3, "SansSerif");
        style3 = this.mConversation.addStyle("localUser", style);
        StyleConstants.setForeground(style3, this.mMyColor);
        StyleConstants.setBold(style3, true);
        style3 = this.mConversation.addStyle("contact", style);
        StyleConstants.setForeground(style3, this.mContactColor);
        StyleConstants.setBold(style3, true);
        style3 = this.mConversation.addStyle("alert", style);
        StyleConstants.setBold(style3, true);
    }

    abstract void layoutFrame();

    abstract JPanel doLayoutMessagePanel();

    public abstract void receivedMessage(String var1, String var2);

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(this.mSend)) {
            this.sendMessage();
        } else if (actionEvent.getSource().equals(this.mClear)) {
            this.clearMessageText();
        } else if (actionEvent.getSource().equals(this.mClose)) {
            this.dispose();
        } else if (actionEvent.getSource().equals(this.mBold)) {
            this.mMenuBold.setSelected(this.mBold.isSelected());
            this.boldChanged();
        } else if (actionEvent.getSource().equals(this.mMenuBold)) {
            this.mBold.setSelected(this.mMenuBold.isSelected());
            this.boldChanged();
        } else if (actionEvent.getSource().equals(this.mItalic)) {
            this.mMenuItalic.setSelected(this.mItalic.isSelected());
            this.italicChanged();
        } else if (actionEvent.getSource().equals(this.mMenuItalic)) {
            this.mItalic.setSelected(this.mMenuItalic.isSelected());
            this.italicChanged();
        } else if (actionEvent.getSource().equals(this.mUnderline)) {
            this.mMenuUnderline.setSelected(this.mUnderline.isSelected());
            this.underlineChanged();
        } else if (actionEvent.getSource().equals(this.mMenuUnderline)) {
            this.mUnderline.setSelected(this.mMenuUnderline.isSelected());
            this.underlineChanged();
        } else if (actionEvent.getSource().equals(this.mFonts)) {
            this.fontChanged();
        }
    }

    private void boldChanged() {
        int n = this.mMessage.getSelectionStart();
        int n2 = this.mMessage.getSelectionEnd();
        MutableAttributeSet mutableAttributeSet = this.mKit.getInputAttributes();
        StyleConstants.setBold(mutableAttributeSet, this.mBold.isSelected());
        if (n < n2) {
            ((DefaultStyledDocument)this.mMessage.getDocument()).setCharacterAttributes(n, n2 - n, mutableAttributeSet, false);
        }
        this.mMessage.grabFocus();
    }

    private void italicChanged() {
        int n = this.mMessage.getSelectionStart();
        int n2 = this.mMessage.getSelectionEnd();
        MutableAttributeSet mutableAttributeSet = this.mKit.getInputAttributes();
        StyleConstants.setItalic(mutableAttributeSet, this.mItalic.isSelected());
        if (n < n2) {
            ((DefaultStyledDocument)this.mMessage.getDocument()).setCharacterAttributes(n, n2 - n, mutableAttributeSet, false);
        }
        this.mMessage.grabFocus();
    }

    private void underlineChanged() {
        int n = this.mMessage.getSelectionStart();
        int n2 = this.mMessage.getSelectionEnd();
        MutableAttributeSet mutableAttributeSet = this.mKit.getInputAttributes();
        StyleConstants.setUnderline(mutableAttributeSet, this.mUnderline.isSelected());
        if (n < n2) {
            ((DefaultStyledDocument)this.mMessage.getDocument()).setCharacterAttributes(n, n2 - n, mutableAttributeSet, false);
        }
        this.mMessage.grabFocus();
    }

    private void fontChanged() {
        int n = this.mMessage.getSelectionStart();
        int n2 = this.mMessage.getSelectionEnd();
        MutableAttributeSet mutableAttributeSet = this.mKit.getInputAttributes();
        StyleConstants.setFontFamily(mutableAttributeSet, (String)this.mFonts.getSelectedItem());
        if (n < n2) {
            ((DefaultStyledDocument)this.mMessage.getDocument()).setCharacterAttributes(n, n2 - n, mutableAttributeSet, false);
        }
        this.boldChanged();
        this.italicChanged();
        this.underlineChanged();
        this.mMessage.grabFocus();
    }

    protected void clearMessageText() {
        this.mMessage.setDocument(new DefaultStyledDocument());
        this.boldChanged();
        this.italicChanged();
        this.underlineChanged();
        this.fontChanged();
    }

    abstract void sendMessage();

}

