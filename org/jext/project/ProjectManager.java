/*
 * Decompiled with CFR 0_102.
 */
package org.jext.project;

import javax.swing.JComponent;
import org.jext.project.Project;
import org.jext.project.ProjectListener;

public interface ProjectManager {
    public void addProjectListener(ProjectListener var1);

    public void removeProjectListener(ProjectListener var1);

    public Project[] getProjects();

    public Project getCurrentProject();

    public void newProject();

    public void openProject(Object var1);

    public void closeProject(Project var1);

    public void saveProject(Project var1);

    public JComponent getUI();
}

