/*
 * Decompiled with CFR 0_102.
 */
package org.jext.project;

import java.io.File;
import org.jext.project.Project;
import org.jext.project.ProjectManager;

public class ProjectEvent {
    public static final int PROJECT_OPENED = 0;
    public static final int PROJECT_CLOSED = 1;
    public static final int PROJECT_SELECTED = 2;
    public static final int FILE_ADDED = 100;
    public static final int FILE_REMOVED = 101;
    public static final int FILE_OPENED = 102;
    public static final int FILE_CLOSED = 103;
    public static final int FILE_SELECTED = 104;
    public static final int FILE_CHANGED = 105;
    public static final int ATTRIBUTE_SET = 201;
    public static final int ATTRIBUTE_UNSET = 202;
    public static final int OTHER = Integer.MAX_VALUE;
    private int event;
    private ProjectManager projectManager;
    private Project project;
    private Object target;

    public ProjectEvent(ProjectManager projectManager, int n) {
        this(projectManager, projectManager.getCurrentProject(), n);
    }

    public ProjectEvent(ProjectManager projectManager, Project project, int n) {
        this(projectManager, project, n, null);
    }

    public ProjectEvent(ProjectManager projectManager, Project project, int n, Object object) {
        if (projectManager == null) {
            throw new IllegalArgumentException("ProjectEvent.<init>:  ProjectManager is null!");
        }
        if (project == null) {
            throw new IllegalArgumentException("ProjectEvent.<init>:  Project is null!");
        }
        this.projectManager = projectManager;
        this.project = project;
        this.event = n;
        switch (n) {
            case 0: 
            case 1: 
            case 2: {
                this.target = project;
                break;
            }
            case 100: 
            case 101: 
            case 102: 
            case 103: 
            case 104: 
            case 105: {
                if (object instanceof File) {
                    this.target = object;
                    break;
                }
                this.target = this.project.getSelectedFile();
                break;
            }
            case 201: 
            case 202: {
                if (!(object instanceof String)) break;
                this.target = object;
            }
        }
    }

    public int getWhat() {
        return this.event;
    }

    public ProjectManager getProjectManager() {
        return this.projectManager;
    }

    public Project getProject() {
        return this.project;
    }

    public Object getTarget() {
        return this.target;
    }

    public String toString() {
        return "ProjectEvent:  " + "projectManager=" + String.valueOf(this.getProjectManager()) + ", " + "project=" + this.getProject().getName() + ", " + "what=" + String.valueOf(this.getWhat()) + ", " + "target=" + String.valueOf(this.getTarget());
    }
}

