/*
 * Decompiled with CFR 0_102.
 */
package jamtester.evalsupp;

import jamtester.Results;
import jamtester.StudentResult;
import jamtester.evalsupp.OtherGradePanel;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Runner {
    public static void main(String[] arrstring) {
        JFrame jFrame = new JFrame("TESTING");
        jFrame.setSize(800, 600);
        Results results = new Results();
        for (int i = 0; i < 100; ++i) {
            results.add(new StudentResult("David Eitan Poll" + i, true, new ArrayList(), results));
        }
        jFrame.getContentPane().add(new OtherGradePanel(results));
        jFrame.show();
    }
}

