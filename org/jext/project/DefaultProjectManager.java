/*
 * Decompiled with CFR 0_102.
 */
package org.jext.project;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTabbedPane;
import org.jext.JextTextArea;
import org.jext.event.JextEvent;
import org.jext.event.JextListener;
import org.jext.misc.ProjectPanel;
import org.jext.misc.Workspaces;
import org.jext.project.AbstractProject;
import org.jext.project.AbstractProjectManager;
import org.jext.project.Project;
import org.jext.project.ProjectEvent;
import org.jext.project.ProjectManager;

public class DefaultProjectManager
extends AbstractProjectManager
implements JextListener {
    private ProjectPanel ui;
    private Vector projectNames;
    private Hashtable projects;
    private Project currentProject;
    private JextFrame parent;

    public DefaultProjectManager(JextFrame jextFrame) {
        if (jextFrame == null) {
            throw new IllegalArgumentException("Parent is null!");
        }
        this.parent = jextFrame;
        jextFrame.addJextListener(this);
        this.ui = new ProjectPanel(jextFrame);
        this.projectNames = new Vector();
        this.projects = new Hashtable();
        this.loadFromWorkspaces();
        jextFrame.getWorkspaces().getList().addListDataListener(new ListDataListener(){

            public void contentsChanged(ListDataEvent listDataEvent) {
                DefaultProjectManager.this.loadFromWorkspaces();
            }

            public void intervalAdded(ListDataEvent listDataEvent) {
                for (int i = listDataEvent.getIndex0(); i <= listDataEvent.getIndex1(); ++i) {
                    Workspaces.WorkspaceElement workspaceElement = (Workspaces.WorkspaceElement)DefaultProjectManager.this.parent.getWorkspaces().getList().get(i);
                    DefaultProjectManager.this.projectNames.add(i, workspaceElement.getName());
                    DefaultProjectManager.this.projects.put(workspaceElement.getName(), new DefaultProject(workspaceElement));
                }
            }

            public void intervalRemoved(ListDataEvent listDataEvent) {
                for (int i = listDataEvent.getIndex0(); i <= listDataEvent.getIndex1(); ++i) {
                    DefaultProjectManager.this.fireProjectEvent(new ProjectEvent(DefaultProjectManager.this, (Project)DefaultProjectManager.this.projects.remove(DefaultProjectManager.this.projectNames.remove(i)), 1));
                }
            }
        });
        this.setCurrentProjectFromWorkspace();
    }

    private void loadFromWorkspaces() {
        DefaultListModel defaultListModel = this.parent.getWorkspaces().getList();
        ArrayList<String> arrayList = new ArrayList<String>(defaultListModel.size());
        HashMap<String, DefaultProject> hashMap = new HashMap<String, DefaultProject>(defaultListModel.size());
        for (int i = 0; i < defaultListModel.size(); ++i) {
            Workspaces.WorkspaceElement workspaceElement = (Workspaces.WorkspaceElement)defaultListModel.get(i);
            arrayList.add(workspaceElement.getName());
            hashMap.put(workspaceElement.getName(), this.projectNames.indexOf(workspaceElement.getName()) < 0 ? new DefaultProject(workspaceElement) : this.projects.get(workspaceElement.getName()));
        }
        this.projectNames.removeAll(arrayList);
        Iterator iterator = this.projectNames.iterator();
        while (iterator.hasNext()) {
            this.fireProjectEvent(new ProjectEvent(this, (Project)this.projects.remove(this.projectNames.remove(this.projectNames.indexOf(String.valueOf(iterator.next())))), 1));
        }
        this.projects.clear();
        this.projectNames.addAll(arrayList);
        this.projects.putAll(hashMap);
    }

    public void jextEventFired(JextEvent jextEvent) {
        if (this.parent.getProjectManager() == this) {
            switch (jextEvent.getWhat()) {
                case 77: {
                    if (this.currentProject == null || this.parent.getWorkspaces().getName() != this.currentProject.getName()) {
                        this.setCurrentProjectFromWorkspace();
                    }
                    this.fireProjectEvent(new ProjectEvent(this, 104));
                    break;
                }
                case 2: 
                case 3: 
                case 4: {
                    this.fireProjectEvent(new ProjectEvent(this, 105));
                }
            }
        }
    }

    private void setCurrentProjectFromWorkspace() {
        this.currentProject = (Project)this.projects.get(this.parent.getWorkspaces().getName());
        this.fireProjectEvent(new ProjectEvent(this, 2));
    }

    public Project[] getProjects() {
        Project[] arrproject = new Project[this.projectNames.size()];
        for (int i = 0; i < arrproject.length; ++i) {
            arrproject[i] = (Project)this.projects.get(this.projectNames.get(i));
        }
        return arrproject;
    }

    public Project getCurrentProject() {
        return this.currentProject;
    }

    public void newProject() {
        String string = JOptionPane.showInputDialog(this.parent, Jext.getProperty("ws.new.msg"), Jext.getProperty("ws.new.title"), 3);
        if (string != null && string.length() > 0) {
            this.openProject(string);
        }
    }

    public void openProject(Object object) {
        this.parent.getWorkspaces().selectWorkspaceOfNameOrCreate(String.valueOf(object));
    }

    public void closeProject(Project project) {
        DefaultListModel defaultListModel = this.parent.getWorkspaces().getList();
        for (int i = 0; i < defaultListModel.size(); ++i) {
            if (!((Workspaces.WorkspaceElement)defaultListModel.get(i)).getName().equals(project.getName())) continue;
            defaultListModel.remove(i);
        }
    }

    public void saveProject(Project project) {
        this.parent.getWorkspaces().save();
    }

    public JComponent getUI() {
        return this.ui;
    }

    private class DefaultProject
    extends AbstractProject {
        private Workspaces.WorkspaceElement ws;

        public DefaultProject(Workspaces.WorkspaceElement workspaceElement) {
            super(workspaceElement.getName(), DefaultProjectManager.this);
            this.ws = workspaceElement;
            this.fireProjectEvent(0);
        }

        public synchronized File[] getFiles() {
            File[] arrfile;
            ArrayList<File> arrayList = new ArrayList<File>(this.ws.contents.size());
            for (int i = 0; i < this.ws.contents.size(); ++i) {
                File file;
                if (!(this.ws.contents.get(i) instanceof JextTextArea) || (file = ((JextTextArea)this.ws.contents.get(i)).getFile()) == null) continue;
                arrayList.add(file);
            }
            try {
                arrfile = arrayList.toArray(new File[arrayList.size()]);
            }
            catch (ArrayStoreException var3_5) {
                arrfile = null;
                var3_5.printStackTrace(System.out);
                Iterator iterator = arrayList.iterator();
                while (iterator.hasNext()) {
                    System.out.println(iterator.next());
                }
            }
            return arrfile;
        }

        public void openFile(File file) {
            if (!DefaultProjectManager.this.parent.getWorkspaces().getName().equals(this.name)) {
                DefaultProjectManager.this.parent.getWorkspaces().selectWorkspaceOfName(this.name);
            }
            DefaultProjectManager.this.parent.open(file.getAbsolutePath());
        }

        public void closeFile(File file) {
            if (!DefaultProjectManager.this.parent.getWorkspaces().getName().equals(this.name)) {
                DefaultProjectManager.this.parent.getWorkspaces().selectWorkspaceOfName(this.name);
            }
            Iterator iterator = this.ws.contents.iterator();
            while (iterator.hasNext()) {
                JextTextArea jextTextArea = (JextTextArea)iterator.next();
                if (!jextTextArea.getFile().equals(file)) continue;
                DefaultProjectManager.this.parent.getWorkspaces().removeFile(jextTextArea);
            }
        }

        public void selectFile(File file) {
            int n = -1;
            for (int i = 0; i < this.ws.contents.size() && n < 0; ++i) {
                if (!((JextTextArea)this.ws.contents.get(i)).getFile().equals(file)) continue;
                n = i;
            }
            if (n < 0) {
                this.openFile(file);
            } else {
                DefaultProjectManager.this.parent.getTabbedPane().setSelectedIndex(n);
                this.ws.setSelectedIndex(n);
            }
        }

        public File getSelectedFile() {
            return ((JextTextArea)this.ws.contents.get(this.ws.getSelectedIndex())).getFile();
        }

        public boolean equals(Object object) {
            return object instanceof DefaultProject && ((DefaultProject)object).name.equals(this.name);
        }

        public int hashCode() {
            return this.name.hashCode();
        }

        public String toString() {
            return "DefaultProject " + this.name;
        }
    }

}

