/*
 * Decompiled with CFR 0_102.
 */
package org.jext.gui;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import org.jext.gui.Skin;

public class GenericSkin
extends Skin {
    private String name;
    private String intName;
    protected String lafClassName = null;
    protected LookAndFeel laf;
    protected ClassLoader skLoader = null;

    public GenericSkin(String string, String string2, String string3) {
        this(string, string2, string3, null);
    }

    public GenericSkin(String string, String string2, String string3, ClassLoader classLoader) {
        this.name = string;
        this.intName = string2;
        this.lafClassName = string3;
        this.skLoader = classLoader;
    }

    public GenericSkin(String string, String string2, LookAndFeel lookAndFeel) {
        this(string, string2, lookAndFeel, null);
    }

    public GenericSkin(String string, String string2, LookAndFeel lookAndFeel, ClassLoader classLoader) {
        this.name = string;
        this.intName = string2;
        this.laf = lookAndFeel;
        this.skLoader = classLoader;
    }

    public boolean isAvailable() {
        return true;
    }

    public String getSkinName() {
        return this.name;
    }

    public String getSkinInternName() {
        return this.intName;
    }

    public void apply() throws Throwable {
        if (this.skLoader != null) {
            UIManager.put("ClassLoader", this.skLoader);
        }
        if (this.lafClassName != null) {
            UIManager.setLookAndFeel(this.lafClassName);
        } else {
            UIManager.setLookAndFeel(this.laf);
        }
    }
}

