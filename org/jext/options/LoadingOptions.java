/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.Component;
import org.jext.Jext;
import org.jext.gui.AbstractOptionPane;
import org.jext.gui.JextCheckBox;

public class LoadingOptions
extends AbstractOptionPane {
    private JextCheckBox xtreeEnabled;
    private JextCheckBox consoleEnabled;
    private JextCheckBox loadClasses = new JextCheckBox(Jext.getProperty("options.loadClasses.label"));

    public LoadingOptions() {
        super("loading");
        this.addComponent(this.loadClasses);
        this.xtreeEnabled = new JextCheckBox(Jext.getProperty("options.xtreeEnabled.label"));
        this.addComponent(this.xtreeEnabled);
        this.consoleEnabled = new JextCheckBox(Jext.getProperty("options.consoleEnabled.label"));
        this.addComponent(this.consoleEnabled);
        this.load();
    }

    public void load() {
        this.loadClasses.setSelected(Jext.getBooleanProperty("load.classes"));
        this.xtreeEnabled.setSelected(Jext.getBooleanProperty("xtree.enabled"));
        this.consoleEnabled.setSelected(Jext.getBooleanProperty("console.enabled"));
    }

    public void save() {
        Jext.setProperty("load.classes", this.loadClasses.isSelected() ? "on" : "off");
        Jext.setProperty("xtree.enabled", this.xtreeEnabled.isSelected() ? "on" : "off");
        Jext.setProperty("console.enabled", this.consoleEnabled.isSelected() ? "on" : "off");
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.loadClasses = null;
        this.xtreeEnabled = null;
        this.consoleEnabled = null;
    }
}

