/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import org.jext.Jext;
import org.jext.Utilities;
import org.jext.gui.ModifiedCellRenderer;
import org.jext.misc.FindFilter;
import org.jext.misc.FindFilterFactory;
import org.jext.misc.FindProgressCallback;

class FindByName
extends JPanel
implements FindFilterFactory {
    protected String NAME_PATTERN = Jext.getProperty("find.accessory.pattern");
    protected String NAME_CONTAINS = Jext.getProperty("find.accessory.contains");
    protected String NAME_IS = Jext.getProperty("find.accessory.is");
    protected String NAME_STARTS_WITH = Jext.getProperty("find.accessory.starts");
    protected String NAME_ENDS_WITH = Jext.getProperty("find.accessory.ends");
    protected int NAME_PATTERN_INDEX = 0;
    protected int NAME_CONTAINS_INDEX = 1;
    protected int NAME_IS_INDEX = 2;
    protected int NAME_STARTS_WITH_INDEX = 3;
    protected int NAME_ENDS_WITH_INDEX = 4;
    protected String[] criteria = new String[]{this.NAME_PATTERN, this.NAME_CONTAINS, this.NAME_IS, this.NAME_STARTS_WITH, this.NAME_ENDS_WITH};
    protected JTextField nameField = null;
    protected JComboBox combo = null;
    protected JCheckBox ignoreCaseCheck = null;

    FindByName() {
        this.setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 2, 2, 2));
        this.combo = new JComboBox<String>(this.criteria);
        this.combo.setPreferredSize(this.combo.getPreferredSize());
        this.combo.setRenderer(new ModifiedCellRenderer());
        jPanel.add(this.combo);
        this.nameField = new JTextField(12);
        jPanel.add(this.nameField);
        jPanel.add(new JLabel("", 4));
        this.ignoreCaseCheck = new JCheckBox(Jext.getProperty("find.accessory.ignorecase"), true);
        this.ignoreCaseCheck.setForeground(Color.black);
        jPanel.add(this.ignoreCaseCheck);
        this.add((Component)jPanel, "North");
    }

    public FindFilter createFindFilter() {
        return new NameFilter(this.nameField.getText(), this.combo.getSelectedIndex(), this.ignoreCaseCheck.isSelected());
    }

    class NameFilter
    implements FindFilter {
        protected String match;
        protected int howToMatch;
        protected boolean ignoreCase;

        NameFilter(String string, int n, boolean bl) {
            this.match = null;
            this.howToMatch = -1;
            this.ignoreCase = true;
            this.match = string;
            this.howToMatch = n;
            this.ignoreCase = bl;
        }

        public boolean accept(File file, FindProgressCallback findProgressCallback) {
            if (file == null) {
                return false;
            }
            if (this.match == null || this.match.length() == 0) {
                return true;
            }
            if (this.howToMatch < 0) {
                return true;
            }
            String string = file.getName();
            if (this.howToMatch == FindByName.this.NAME_PATTERN_INDEX) {
                return Utilities.match(this.match, string);
            }
            if (this.howToMatch == FindByName.this.NAME_CONTAINS_INDEX) {
                if (this.ignoreCase) {
                    if (string.toLowerCase().indexOf(this.match.toLowerCase()) >= 0) {
                        return true;
                    }
                    return false;
                }
                if (string.indexOf(this.match) >= 0) {
                    return true;
                }
                return false;
            }
            if (this.howToMatch == FindByName.this.NAME_IS_INDEX) {
                if (this.ignoreCase) {
                    if (string.equalsIgnoreCase(this.match)) {
                        return true;
                    }
                    return false;
                }
                if (string.equals(this.match)) {
                    return true;
                }
                return false;
            }
            if (this.howToMatch == FindByName.this.NAME_STARTS_WITH_INDEX) {
                if (this.ignoreCase) {
                    if (string.toLowerCase().startsWith(this.match.toLowerCase())) {
                        return true;
                    }
                    return false;
                }
                if (string.startsWith(this.match)) {
                    return true;
                }
                return false;
            }
            if (this.howToMatch == FindByName.this.NAME_ENDS_WITH_INDEX) {
                if (this.ignoreCase) {
                    if (string.toLowerCase().endsWith(this.match.toLowerCase())) {
                        return true;
                    }
                    return false;
                }
                if (string.endsWith(this.match)) {
                    return true;
                }
                return false;
            }
            return true;
        }
    }

}

