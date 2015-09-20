/*
 * Decompiled with CFR 0_102.
 */
package org.jext.event;

import org.jext.JextFrame;
import org.jext.JextTextArea;

public class JextEvent {
    public static final int PROPERTIES_CHANGED = 0;
    public static final int SYNTAX_MODE_CHANGED = 1;
    public static final int CHANGED_UPDATE = 2;
    public static final int INSERT_UPDATE = 3;
    public static final int REMOVE_UPDATE = 4;
    public static final int FILE_OPENED = 10;
    public static final int FILE_CLEARED = 11;
    public static final int BATCH_MODE_SET = 20;
    public static final int BATCH_MODE_UNSET = 21;
    public static final int TEXT_AREA_FOCUS_GAINED = 76;
    public static final int TEXT_AREA_SELECTED = 77;
    public static final int TEXT_AREA_OPENED = 78;
    public static final int TEXT_AREA_CLOSED = 79;
    public static final int OPENING_WINDOW = 98;
    public static final int CLOSING_WINDOW = 99;
    public static final int KILLING_JEXT = 101;
    private int event;
    private JextFrame parent;
    private JextTextArea textArea;

    public JextEvent(JextFrame jextFrame, int n) {
        this.parent = jextFrame;
        this.textArea = jextFrame.getTextArea();
        this.event = n;
    }

    public JextEvent(JextFrame jextFrame, JextTextArea jextTextArea, int n) {
        this.parent = jextFrame;
        this.textArea = jextTextArea;
        this.event = n;
    }

    public int getWhat() {
        return this.event;
    }

    public JextFrame getJextFrame() {
        return this.parent;
    }

    public JextTextArea getTextArea() {
        return this.textArea;
    }
}

