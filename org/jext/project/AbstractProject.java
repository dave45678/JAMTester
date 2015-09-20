/*
 * Decompiled with CFR 0_102.
 */
package org.jext.project;

import java.util.Hashtable;
import java.util.Map;
import org.jext.project.AbstractProjectManager;
import org.jext.project.Project;
import org.jext.project.ProjectEvent;
import org.jext.project.ProjectManager;

public abstract class AbstractProject
implements Project {
    protected final Map attributes;
    protected final AbstractProjectManager manager;
    protected final String name;

    protected AbstractProject(String string, AbstractProjectManager abstractProjectManager) {
        if (abstractProjectManager == null) {
            throw new IllegalArgumentException("Cannot have a null ProjectManager!");
        }
        this.manager = abstractProjectManager;
        this.name = string;
        this.attributes = new Hashtable();
    }

    public String getName() {
        return this.name;
    }

    public Object getAttribute(String string) {
        return this.attributes.get(string);
    }

    public Object getAttribute(String string, Object object) {
        return this.getAttribute(string) == null ? object : this.getAttribute(string);
    }

    public String getAttributeAsString(String string) {
        return String.valueOf(this.attributes.get(string));
    }

    public void setAttribute(String string, Object object) {
        this.attributes.put(string, object);
        this.fireProjectEvent(201, string);
    }

    protected void fireProjectEvent(int n) {
        this.manager.fireProjectEvent(new ProjectEvent(this.manager, this, n));
    }

    protected void fireProjectEvent(int n, Object object) {
        this.manager.fireProjectEvent(new ProjectEvent(this.manager, this, n, object));
    }
}

