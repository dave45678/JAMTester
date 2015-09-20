/*
 * Decompiled with CFR 0_102.
 */
package org.jext.project;

import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.project.DefaultProjectManager;
import org.jext.project.ProjectManagement;
import org.jext.project.ProjectManager;

public class DefaultProjectManagement
implements ProjectManagement {
    private JextFrame parent;
    private ProjectManager pm;

    public DefaultProjectManagement(JextFrame jextFrame) {
        this.parent = jextFrame;
    }

    public String getLabel() {
        return Jext.getProperty("defaultProjectManager.label");
    }

    public ProjectManager getProjectManager() {
        this.pm = this.pm == null ? new DefaultProjectManager(this.parent) : this.pm;
        return this.pm;
    }
}

