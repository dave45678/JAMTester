/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import org.jext.Jext;
import org.jext.gui.ModifiedCellRenderer;
import org.jext.misc.FindFilter;
import org.jext.misc.FindFilterFactory;
import org.jext.misc.FindProgressCallback;

class FindByDate
extends JPanel
implements FindFilterFactory {
    public static String THE_BIG_BANG = Jext.getProperty("find.accessory.bang");
    public static String THE_BIG_CRUNCH = Jext.getProperty("find.accessory.crunch");
    public static String YESTERDAY = Jext.getProperty("find.accessory.yesterday");
    public static String TODAY = Jext.getProperty("find.accessory.today");
    public static String NOW = Jext.getProperty("find.accessory.now");
    public static String MODIFIED_LABEL = Jext.getProperty("find.accessory.modified");
    public static String FORMAT_LABEL = Jext.getProperty("find.accessory.format");
    public static String FROM_DATE_LABEL = Jext.getProperty("find.accessory.from");
    public static String TO_DATE_LABEL = Jext.getProperty("find.accessory.to");
    protected JComboBox fromDateField = null;
    protected JComboBox toDateField = null;
    protected String[] fromDateItems = new String[]{THE_BIG_BANG, YESTERDAY, TODAY};
    protected String[] toDateItems = new String[]{THE_BIG_CRUNCH, TODAY, NOW, YESTERDAY};

    FindByDate() {
        this.setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 2, 2, 2));
        JLabel jLabel = new JLabel(MODIFIED_LABEL, 2);
        jPanel.add(jLabel);
        JLabel jLabel2 = new JLabel(FORMAT_LABEL, 2);
        jPanel.add(jLabel2);
        JLabel jLabel3 = new JLabel(FROM_DATE_LABEL, 4);
        jPanel.add(jLabel3);
        this.fromDateField = new JComboBox<String>(this.fromDateItems);
        this.fromDateField.setEditable(true);
        this.fromDateField.setRenderer(new ModifiedCellRenderer());
        jPanel.add(this.fromDateField);
        JLabel jLabel4 = new JLabel(TO_DATE_LABEL, 4);
        jPanel.add(jLabel4);
        this.toDateField = new JComboBox<String>(this.toDateItems);
        this.toDateField.setEditable(true);
        this.toDateField.setRenderer(new ModifiedCellRenderer());
        jPanel.add(this.toDateField);
        this.add((Component)jPanel, "North");
    }

    public FindFilter createFindFilter() {
        long l = -1;
        long l2 = -1;
        l = this.startDateToTime((String)this.fromDateField.getSelectedItem());
        l2 = this.endDateToTime((String)this.toDateField.getSelectedItem());
        return new DateFilter(l, l2);
    }

    protected long startDateToTime(String string) {
        if (string == null) {
            return -1;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = simpleDateFormat.parse(string, new ParsePosition(0));
        if (date == null) {
            if (string.equalsIgnoreCase(TODAY)) {
                String string2 = simpleDateFormat.format(new Date());
                date = simpleDateFormat.parse(string2, new ParsePosition(0));
            } else if (string.equalsIgnoreCase(YESTERDAY)) {
                String string3 = simpleDateFormat.format(new Date(new Date().getTime() - 86400000));
                date = simpleDateFormat.parse(string3, new ParsePosition(0));
            } else if (string.equalsIgnoreCase(THE_BIG_BANG)) {
                return 0;
            }
        }
        if (date != null) {
            return date.getTime();
        }
        return -1;
    }

    protected long endDateToTime(String string) {
        if (string == null) {
            return -1;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        long l = -1;
        Date date = simpleDateFormat.parse(string, new ParsePosition(0));
        if (date == null) {
            if (string.equalsIgnoreCase(TODAY)) {
                String string2 = simpleDateFormat.format(new Date());
                date = simpleDateFormat.parse(string2, new ParsePosition(0));
                if (date != null) {
                    l = date.getTime() + 86400000;
                }
            } else if (string.equalsIgnoreCase(YESTERDAY)) {
                String string3 = simpleDateFormat.format(new Date(new Date().getTime() - 86400000));
                date = simpleDateFormat.parse(string3, new ParsePosition(0));
                if (date != null) {
                    l = date.getTime() + 86400000;
                }
            } else if (string.equalsIgnoreCase(NOW)) {
                date = new Date();
                if (date != null) {
                    l = date.getTime();
                }
            } else if (string.equalsIgnoreCase(THE_BIG_CRUNCH)) {
                l = Long.MAX_VALUE;
            }
        } else {
            l = date.getTime() + 86400000;
        }
        return l;
    }

    class DateFilter
    implements FindFilter {
        protected long startTime;
        protected long endTime;

        DateFilter(long l, long l2) {
            this.startTime = -1;
            this.endTime = -1;
            this.startTime = l;
            this.endTime = l2;
        }

        public boolean accept(File file, FindProgressCallback findProgressCallback) {
            if (file == null) {
                return false;
            }
            long l = file.lastModified();
            if (this.startTime >= 0 && l < this.startTime) {
                return false;
            }
            if (this.endTime >= 0 && l > this.endTime) {
                return false;
            }
            return true;
        }
    }

}

