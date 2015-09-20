/*
 * Decompiled with CFR 0_102.
 */
package jamtester;

import jamtester.GradingToolGui;
import jamtester.Results;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.filechooser.FileFilter;

public class ResultsActionListener
implements ActionListener {
    private Results r;
    private boolean csv;

    public ResultsActionListener(Results results, boolean bl) {
        this.r = results;
        this.csv = bl;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (!this.csv) {
            this.save();
        } else {
            this.saveAsCsv();
        }
    }

    public void save() {
        File file = GradingToolGui.showSaveDialog(GradingToolGui.jamFF, ".jam");
        try {
            this.r.save(file);
        }
        catch (Exception var2_2) {
            var2_2.printStackTrace(System.err);
        }
    }

    public void saveAsCsv() {
        File file = GradingToolGui.showSaveDialog(GradingToolGui.csvFF, ".csv");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            PrintStream printStream = new PrintStream(fileOutputStream);
            printStream.println(this.r.toCsv());
            fileOutputStream.close();
        }
        catch (Exception var2_3) {
            var2_3.printStackTrace(System.err);
        }
    }
}

