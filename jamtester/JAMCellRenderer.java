/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import jamtester.CompileTestResult;
import jamtester.JAMTestFailure;
import jamtester.TestResult;
import jamtester.UntestedTestResult;
import jamtester.coverage.CoverageInfo;
import jamtester.coverage.CoverageTestResult;
import java.awt.Color;
import java.awt.Component;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

class JAMCellRenderer
implements TableCellRenderer {
    JAMCellRenderer() {
    }

    public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n, int n2) {
        Component component = null;
        if (object instanceof String) {
            component = this.handleString(object);
        } else if (object instanceof CoverageTestResult) {
            component = this.handleCoverage(object);
        } else if (object instanceof TestResult) {
            component = this.handleTest(object);
        }
        return component;
    }

    public Component handleString(Object object) {
        JLabel jLabel = new JLabel((String)object);
        return jLabel;
    }

    public Component handleCoverage(Object object) {
        CoverageTestResult coverageTestResult = (CoverageTestResult)object;
        System.out.println(coverageTestResult.coverageInfos());
        JProgressBar jProgressBar = new JProgressBar();
        jProgressBar.setMinimum(0);
        jProgressBar.setMaximum(1000);
        jProgressBar.setValue((int)coverageTestResult.mainInfo().getPercentageCovered() * 10);
        jProgressBar.setForeground(Color.green);
        jProgressBar.setBackground(Color.red);
        jProgressBar.setBorderPainted(false);
        return jProgressBar;
    }

    public Component handleTest(Object object) {
        TestResult testResult = (TestResult)object;
        String string = testResult.toString();
        JTextField jTextField = new JTextField(string);
        jTextField.setEditable(false);
        jTextField.setBorder(null);
        jTextField.setHorizontalAlignment(0);
        JAMTestFailure jAMTestFailure = testResult.exception();
        if (jAMTestFailure != null) {
            jTextField.setToolTipText(jAMTestFailure.toString());
        }
        if (testResult.succeded()) {
            jTextField.setBackground(Color.green);
            jTextField.setForeground(Color.green);
        } else if (testResult instanceof UntestedTestResult) {
            jTextField.setToolTipText("Untested");
            jTextField.setBackground(Color.yellow);
            jTextField.setForeground(Color.black);
        } else if (testResult instanceof CompileTestResult) {
            jTextField.setToolTipText("Compile Error");
            jTextField.setBackground(Color.white);
            jTextField.setForeground(Color.black);
        } else {
            jTextField.setBackground(Color.red);
            jTextField.setForeground(Color.white);
        }
        return jTextField;
    }
}

