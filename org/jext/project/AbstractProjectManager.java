/*
 * Decompiled with CFR 0_102.
 */
package org.jext.project;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Vector;
import org.jext.project.ProjectEvent;
import org.jext.project.ProjectListener;
import org.jext.project.ProjectManager;

public abstract class AbstractProjectManager
implements ProjectManager {
    protected Vector listeners = new Vector();

    protected AbstractProjectManager() {
    }

    public void addProjectListener(ProjectListener projectListener) {
        this.listeners.add(projectListener);
    }

    public void removeProjectListener(ProjectListener projectListener) {
        this.listeners.remove(projectListener);
    }

    protected void fireProjectEvent(ProjectEvent projectEvent) {
        ArrayList<ProjectListener> arrayList = new ArrayList<ProjectListener>(this.listeners.size());
        Iterator iterator = this.listeners.iterator();
        while (arrayList.size() < this.listeners.size()) {
            try {
                while (iterator.hasNext()) {
                    ProjectListener projectListener = (ProjectListener)iterator.next();
                    if (arrayList.contains(projectListener)) continue;
                    projectListener.projectEventFired(projectEvent);
                    arrayList.add(projectListener);
                }
                continue;
            }
            catch (ConcurrentModificationException var4_5) {
                iterator = this.listeners.iterator();
                continue;
            }
        }
    }
}

