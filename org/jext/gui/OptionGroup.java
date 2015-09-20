/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import java.util.ArrayList;
import org.jext.gui.OptionPane;

public class OptionGroup {
    private String name;
    private ArrayList members;

    public OptionGroup(String string) {
        this.name = string;
        this.members = new ArrayList();
    }

    public String getName() {
        return this.name;
    }

    public void addOptionGroup(OptionGroup optionGroup) {
        if (this.members.indexOf(optionGroup) != -1) {
            return;
        }
        this.members.add(optionGroup);
    }

    public void addOptionPane(OptionPane optionPane) {
        if (this.members.indexOf(optionPane) != -1) {
            return;
        }
        this.members.add(optionPane);
    }

    public ArrayList getMembers() {
        return this.members;
    }

    public Object getMember(int n) {
        return n >= 0 && n < this.members.size() ? this.members.get(n) : null;
    }

    public int getMemberIndex(Object object) {
        return this.members.indexOf(object);
    }

    public int getMemberCount() {
        return this.members.size();
    }

    public void save() {
        for (int i = 0; i < this.members.size(); ++i) {
            Object e = this.members.get(i);
            try {
                if (e instanceof OptionPane) {
                    ((OptionPane)e).save();
                    continue;
                }
                if (!(e instanceof OptionGroup)) continue;
                ((OptionGroup)e).save();
                continue;
            }
            catch (Throwable var3_3) {
                // empty catch block
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.members.clear();
        this.members = null;
        this.name = null;
    }
}

