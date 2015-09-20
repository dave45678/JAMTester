/*
 * Decompiled with CFR 0_102.
 */
package jamtester.studenttool;

import jamtester.studenttool.StudentToolTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.undo.UndoManager;
import org.gjt.sp.jedit.textarea.DefaultInputHandler;
import org.gjt.sp.jedit.textarea.InputHandler;
import org.jext.Jext;

public class StudentToolInputHandler
extends DefaultInputHandler {
    public StudentToolInputHandler() {
        super.addDefaultKeyBindings();
        this.addKeyBinding("C+V", new paste());
        this.addKeyBinding("C+C", new copy());
        this.addKeyBinding("C+X", new cut());
        this.addKeyBinding("C+A", new selall());
        this.addKeyBinding("C+Z", new undo());
        this.addKeyBinding("C+Y", new redo());
        Jext.addJextKeyBindings(this);
    }

    public static class selall
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            StudentToolTextArea studentToolTextArea = (StudentToolTextArea)InputHandler.getTextArea(actionEvent);
            studentToolTextArea.selectAll();
        }
    }

    public static class cut
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            StudentToolTextArea studentToolTextArea = (StudentToolTextArea)InputHandler.getTextArea(actionEvent);
            studentToolTextArea.cut();
        }
    }

    public static class paste
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            StudentToolTextArea studentToolTextArea = (StudentToolTextArea)InputHandler.getTextArea(actionEvent);
            studentToolTextArea.paste();
        }
    }

    public static class copy
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            StudentToolTextArea studentToolTextArea = (StudentToolTextArea)InputHandler.getTextArea(actionEvent);
            studentToolTextArea.copy();
        }
    }

    public static class undo
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            StudentToolTextArea studentToolTextArea = (StudentToolTextArea)InputHandler.getTextArea(actionEvent);
            if (!(studentToolTextArea.isEditable() && studentToolTextArea.getUndo().canUndo())) {
                return;
            }
            studentToolTextArea.getUndo().undo();
        }
    }

    public static class redo
    implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            StudentToolTextArea studentToolTextArea = (StudentToolTextArea)InputHandler.getTextArea(actionEvent);
            if (!(studentToolTextArea.isEditable() && studentToolTextArea.getUndo().canRedo())) {
                return;
            }
            studentToolTextArea.getUndo().redo();
        }
    }

}

