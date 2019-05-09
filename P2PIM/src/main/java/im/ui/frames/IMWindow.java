/*
 * Decompiled with CFR 0.137.
 */
package im.ui.frames;

import im.ui.Client;
import im.ui.frames.AbstractMessageWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Keymap;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.rtf.RTFEditorKit;

public class IMWindow
extends AbstractMessageWindow
implements ActionListener {
    private JSplitPane mSplitter;

    public IMWindow(String string, Color color, Color color2) {
        super(string, color, color2);
    }

    protected void layoutFrame() {
        this.mMessage.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(10, 2), this.mSendAction);
        this.mMessagePanel = this.doLayoutMessagePanel();
        this.mSplitter = new JSplitPane(0, this.mConversationScroll, this.mMessagePanel);
        this.mSplitter.setDividerLocation(90);
        this.mMainPanel.add((Component)this.mSplitter, "Center");
        this.getContentPane().add(this.mMainPanel);
    }

    protected void init() {
        super.init();
        this.setSize(AbstractMessageWindow.IM_SIZE);
        this.mSend.setText(null);
    }

    protected JPanel doLayoutMessagePanel() {
        JPanel jPanel = new JPanel(new BorderLayout());
        JPanel jPanel2 = new JPanel(new FlowLayout());
        jPanel2.add(this.mFonts);
        jPanel2.add(this.mBold);
        jPanel2.add(this.mItalic);
        jPanel2.add(this.mUnderline);
        JPanel jPanel3 = new JPanel(new FlowLayout());
        jPanel3.add(this.mSend);
        jPanel3.add(this.mClear);
        jPanel.add((Component)jPanel2, "North");
        jPanel.add((Component)this.mMessageScroll, "Center");
        jPanel.add((Component)jPanel3, "South");
        return jPanel;
    }

    protected void sendMessage() {
        if (this.mMessage.getDocument().getLength() == 0) {
            JOptionPane.showMessageDialog(this, "You have not completed this field.", "Error", 0);
            return;
        }
        try {
            Client.sendFormattedMessage(this.getName(), this.mMessage.getDocument());
            this.postMessage(Client.userName, this.mMessage.getDocument());
        }
        catch (Exception exception) {
            System.out.println("Excetption: IMWindow.sendMessage() ");
            exception.printStackTrace();
        }
        super.clearMessageText();
    }

    public void postMessage(String string, Document document) {
        Color color = this.mMyColor;
        if (!string.equals(Client.userName)) {
            color = this.mContactColor;
        }
        try {
            DefaultStyledDocument defaultStyledDocument = (DefaultStyledDocument)this.mKit.createDefaultDocument();
            Document document2 = this.mConversation.getDocument();
            if (document2.getLength() > 0) {
                this.mKit.write(this.mConversationOut, document2, 0, document2.getLength());
                this.mKit.read(this.mConversationIn, (Document)defaultStyledDocument, 0);
            }
            this.mKit.write(this.mMessageOut, document, 0, document.getLength());
            int n = defaultStyledDocument.getLength();
            MutableAttributeSet mutableAttributeSet = this.mConvKit.getInputAttributes();
            StyleConstants.setBold(mutableAttributeSet, true);
            StyleConstants.setForeground(mutableAttributeSet, color);
            defaultStyledDocument.insertString(defaultStyledDocument.getLength(), string + ":  ", mutableAttributeSet);
            this.mKit.read(this.mMessageIn, (Document)defaultStyledDocument, n);
            defaultStyledDocument.remove(defaultStyledDocument.getLength() - 1, 1);
            this.mConversation.setDocument(defaultStyledDocument);
            this.mConversation.setCaretPosition(defaultStyledDocument.getLength());
            if (string.equals(Client.userName)) {
                this.mMessage.grabFocus();
            }
        }
        catch (Exception exception) {}
    }

    public void receivedMessage(String string, String string2) {
        DefaultStyledDocument defaultStyledDocument = new DefaultStyledDocument();
        try {
            defaultStyledDocument.insertString(defaultStyledDocument.getLength(), string2, this.mConversation.getStyle("default"));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.postMessage(string, defaultStyledDocument);
    }

    public void toggleSend(boolean bl) {
        this.mSend.setEnabled(bl);
    }
}

