/*
 * Decompiled with CFR 0.137.
 */
package im.ui.custom;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class EditableComboBox
extends JComboBox {
    private ArrayList mHistory;
    private JTextField mText;

    public EditableComboBox(String string, ArrayList arrayList) {
        super(arrayList.toArray());
        this.mHistory = arrayList == null ? new ArrayList() : arrayList;
        this.mText = (JTextField)this.getEditor().getEditorComponent();
        this.setEditable(true);
        this.setSelectedItem(string);
        this.highlightText();
    }

    public String getText() {
        return this.mText.getText();
    }

    public void setText(String string) {
        int n = this.indexOf(string);
        if (n == -1) {
            this.addItem(string);
        } else if (n > 0) {
            this.moveToTop(string);
        }
        this.setSelectedIndex(this.indexOf(string));
        this.highlightText();
    }

    public void addItem(String string) {
        super.insertItemAt(string, 0);
        this.mHistory.add(0, string);
        this.removeItem("");
    }

    public void removeItem(Object object) {
        super.removeItem(object);
        int n = this.mHistory.indexOf(object);
        if (n > -1) {
            this.mHistory.remove(n);
        }
    }

    public void highlightText() {
        this.mText.setSelectionStart(0);
        this.mText.setSelectionEnd(this.mText.getText().length());
    }

    public int indexOf(String string) {
        return this.mHistory.indexOf(string);
    }

    private void moveToTop(String string) {
        super.removeItem(string);
        super.insertItemAt(string, 0);
        this.mHistory.remove(string);
        this.mHistory.add(0, string);
    }
}

