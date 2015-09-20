/*
 * Decompiled with CFR 0_102.
 */
package jamtester.coverage;

import jamtester.coverage.CoverageInfo;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class CoverageCellRenderer
implements TableCellRenderer {
    private CoverageInfo info;

    public CoverageCellRenderer(CoverageInfo coverageInfo) {
        this.info = coverageInfo;
    }

    public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
        if (n2 == 0) {
            return new JLabel(object.toString());
        }
        int n3 = n + 1;
        int n4 = this.info.timesRun(n3);
        JTextField jTextField = new JTextField(object.toString());
        jTextField.setFont(Font.getFont("Courier"));
        jTextField.setBorder(null);
        if (n4 == 0) {
            jTextField.setBackground(Color.red);
            jTextField.setForeground(Color.white);
            jTextField.setToolTipText("Line not covered");
            return jTextField;
        }
        if (n4 == -1) {
            return jTextField;
        }
        jTextField.setBackground(Color.green);
        jTextField.setToolTipText("Times covered: " + n4);
        return jTextField;
    }
}

