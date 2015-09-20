/*
 * Decompiled with CFR 0_102.
 */
package jamtester.studenttool;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class StudentHelpDialog {
    public StudentHelpDialog(Component component) {
        JLabel jLabel = new JLabel();
        jLabel.setAlignmentX(0.5f);
        jLabel.setText("Welcome to the JAM*Tester Student Version!\n\nThe tool will automatically create a project for you once you open a directory using the File menu.\nMake sure that any .jar or .class files you need to make your program run are in a +libs directory in that directory.\nOnce you have done this, start exploring.  Changes will be saved automatically.\n\n\nPlease, check here in later versions for further updates.  I know this isn't too comprehensive at the moment, but it will hopefully become more useful in time.\n");
        JOptionPane.showMessageDialog(component, jLabel.getText(), "JAM*Tester Student Version Instructions...", 1);
    }
}

