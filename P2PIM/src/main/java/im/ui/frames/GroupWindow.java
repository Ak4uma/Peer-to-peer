/*
 * Decompiled with CFR 0.137.
 */
package im.ui.frames;

import im.ui.Client;
import im.ui.frames.AbstractMessageWindow;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;

public class GroupWindow
extends AbstractMessageWindow {
    private JMenu mActions;
    private JMenuItem mMenuInvite;
    private DefaultListModel mListModel;
    private JTextField mMessageText;
    private JList mGroupMembers;
    private JScrollPane mMembersScroll;
    private Dimension mMinSize;

    public GroupWindow(String string) {
        super(string, Color.blue, Color.blue);
        this.setTitle("Group Chat");
        this.addMember(Client.userName);
    }

    protected void init() {
        super.init();
        this.mMinSize = AbstractMessageWindow.GROUP_SIZE;
        this.setSize(this.mMinSize);
        this.addComponentListener(new ComponentAdapter(){

            public void componentResized(ComponentEvent componentEvent) {
                Dimension dimension = GroupWindow.this.getSize();
                if (dimension.width < GroupWindow.access$000((GroupWindow)GroupWindow.this).width || dimension.height < GroupWindow.access$000((GroupWindow)GroupWindow.this).height) {
                    GroupWindow.this.setSize(GroupWindow.this.mMinSize);
                }
            }
        });
        this.mActions = new JMenu("Actions");
        this.mMenuInvite = new JMenuItem("Group Invite");
        this.mMenuInvite.addActionListener(this);
        this.mMenuInvite.setAccelerator(KeyStroke.getKeyStroke(71, 2));
        this.mActions.add(this.mMenuInvite);
        this.mMenuBar.remove(this.mEdit);
        this.mListModel = new DefaultListModel();
        this.mGroupMembers = new JList(this.mListModel);
        this.mGroupMembers.setPreferredSize(new Dimension(125, 145));
        this.mGroupMembers.setSelectionMode(0);
        this.mMessageText = new JTextField(35);
        this.mMessageText.addActionListener(this);
        this.mSend.setIcon(null);
        this.mSend.setPreferredSize(new Dimension(65, 21));
        this.mMembersScroll = new JScrollPane(this.mGroupMembers, 20, 30);
    }

    protected void layoutFrame() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JPanel jPanel = new JPanel(gridBagLayout);
        jPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel jPanel2 = new JPanel(new FlowLayout());
        jPanel2.add(this.mMembersScroll);
        jPanel2.setBorder(BorderFactory.createTitledBorder("Group Members"));
        jPanel2.add(this.mMembersScroll);
        gridBagConstraints.anchor = 10;
        gridBagConstraints.fill = 0;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagLayout.setConstraints(jPanel2, gridBagConstraints);
        jPanel.add(jPanel2);
        this.mMainPanel.add((Component)this.doLayoutMessagePanel(), "Center");
        this.mMainPanel.add((Component)jPanel, "East");
        this.getContentPane().add(this.mMainPanel);
    }

    protected void sendMessage() {
        String string = this.mMessageText.getText();
        if (string.length() > 0) {
            this.mMessageText.setText("");
            Client.sendGroupMessage(this.getName(), string);
            this.postMessage(Client.userName, string);
        } else {
            JOptionPane.showMessageDialog(this, "You have not completed this field.", "Error", 0);
        }
    }

    public void postMessage(String string, String string2) {
        try {
            DefaultStyledDocument defaultStyledDocument = (DefaultStyledDocument)this.mConversation.getDocument();
            defaultStyledDocument.insertString(defaultStyledDocument.getLength(), string + ":  ", this.mConversation.getStyle("localUser"));
            defaultStyledDocument.insertString(defaultStyledDocument.getLength(), string2 + "\n", this.mConversation.getStyle("default"));
            this.mConversation.setCaretPosition(defaultStyledDocument.getLength());
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected JPanel doLayoutMessagePanel() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        JPanel jPanel = new JPanel(gridBagLayout);
        gridBagConstraints.anchor = 10;
        gridBagConstraints.fill = 1;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 7.0;
        gridBagLayout.setConstraints(this.mConversationScroll, gridBagConstraints);
        gridBagConstraints.anchor = 11;
        gridBagConstraints.fill = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weighty = 0.25;
        gridBagConstraints.weightx = 5.0;
        gridBagLayout.setConstraints(this.mMessageText, gridBagConstraints);
        gridBagConstraints.anchor = 12;
        gridBagConstraints.fill = 0;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagLayout.setConstraints(this.mSend, gridBagConstraints);
        jPanel.add(this.mConversationScroll);
        jPanel.add(this.mMessageText);
        jPanel.add(this.mSend);
        return jPanel;
    }

    public void focusTextField() {
        this.mMessageText.grabFocus();
    }

    public void addMember(String string) {
        if (this.mListModel.contains(string)) {
            return;
        }
        this.mListModel.addElement(string);
        try {
            DefaultStyledDocument defaultStyledDocument = (DefaultStyledDocument)this.mConversation.getDocument();
            defaultStyledDocument.insertString(defaultStyledDocument.getLength(), "***  Attention: ", this.mConversation.getStyle("alert"));
            defaultStyledDocument.insertString(defaultStyledDocument.getLength(), string, this.mConversation.getStyle("localUser"));
            defaultStyledDocument.insertString(defaultStyledDocument.getLength(), " has entered the group.  ***\n", this.mConversation.getStyle("alert"));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void removeMember(String string) {
        block2 : {
            if (!this.mListModel.contains(string)) break block2;
            this.mListModel.removeElement(string);
            try {
                DefaultStyledDocument defaultStyledDocument = (DefaultStyledDocument)this.mConversation.getDocument();
                defaultStyledDocument.insertString(defaultStyledDocument.getLength(), "***  Attention: ", this.mConversation.getStyle("alert"));
                defaultStyledDocument.insertString(defaultStyledDocument.getLength(), string, this.mConversation.getStyle("localUser"));
                defaultStyledDocument.insertString(defaultStyledDocument.getLength(), " has left the group.  ***\n", this.mConversation.getStyle("alert"));
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void receivedMessage(String string, String string2) {
        this.postMessage(string, string2);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (!actionEvent.getSource().equals(this.mMenuInvite)) {
            if (actionEvent.getSource().equals(this.mMessageText)) {
                this.sendMessage();
            } else {
                super.actionPerformed(actionEvent);
            }
        }
    }

}

