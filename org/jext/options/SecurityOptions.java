/*
 * Decompiled with CFR 0_102.
 */
package org.jext.options;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import org.jext.Jext;
import org.jext.gui.AbstractOptionPane;
import org.jext.gui.JextCheckBox;

public class SecurityOptions
extends AbstractOptionPane {
    private JextCheckBox enableServer = new JextCheckBox(Jext.getProperty("options.security.enableServer"));

    public SecurityOptions() {
        super("security");
        this.addComponent(this.enableServer);
        this.load();
    }

    public void load() {
        this.enableServer.setSelected(Jext.isServerEnabled());
    }

    public void save() {
        if (this.enableServer.isSelected() == Jext.isServerEnabled()) {
            return;
        }
        try {
            File file = new File(Jext.SETTINGS_DIRECTORY + ".security");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            String string = new Boolean(this.enableServer.isSelected()).toString();
            bufferedWriter.write(string, 0, string.length());
            bufferedWriter.flush();
            bufferedWriter.close();
            Jext.setServerEnabled(this.enableServer.isSelected());
        }
        catch (Exception var1_2) {
            // empty catch block
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.enableServer = null;
    }
}

