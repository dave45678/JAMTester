/*
 * Decompiled with CFR 0_102.
 */
package jplagwebstart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import jplagwebstart.JPlagGUI;
import jplagwebstart.JPlagOptions;
import jplagwebstart.Submission;

class SubmissionTree
extends JFrame
implements ActionListener,
WindowListener {
    private DefaultMutableTreeNode root = null;
    private JTree tree;
    private JPlagGUI gui;
    private static Point location = null;
    private static Dimension size = null;

    public void setVisible(boolean bl) {
        boolean bl2 = this.isVisible();
        if (bl != bl2) {
            if (bl) {
                if (location != null && size != null) {
                    this.setSize(size);
                    this.setLocation(location);
                }
            } else {
                size = this.getSize();
                location = this.getLocation();
            }
        }
        super.setVisible(bl);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String string = actionEvent.getActionCommand();
        if (string.equals("expand")) {
            this.tree.expandRow(0);
            for (int i = this.tree.getRowCount() - 1; i > 0; --i) {
                this.tree.expandRow(i);
            }
        } else if (string.equals("collapse")) {
            int n = this.tree.getRowCount();
            for (int i = 1; i < n; ++i) {
                this.tree.collapseRow(i);
            }
        } else {
            this.gui.previewClosed();
            this.setVisible(false);
        }
    }

    public void windowClosing(WindowEvent windowEvent) {
        this.gui.previewClosed();
    }

    public void windowActivated(WindowEvent windowEvent) {
    }

    public void windowDeactivated(WindowEvent windowEvent) {
    }

    public void windowClosed(WindowEvent windowEvent) {
    }

    public void windowDeiconified(WindowEvent windowEvent) {
    }

    public void windowIconified(WindowEvent windowEvent) {
    }

    public void windowOpened(WindowEvent windowEvent) {
    }

    public SubmissionTree(Vector vector, JPlagOptions jPlagOptions, JPlagGUI jPlagGUI) {
        super("Submission Preview");
        this.gui = jPlagGUI;
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.root = new DefaultMutableTreeNode("All Submissions");
        this.makeTree(vector, jPlagOptions);
        this.tree = new JTree(this.root, true);
        this.tree.setEditable(true);
        JScrollPane jScrollPane = new JScrollPane(this.tree);
        jScrollPane.setBackground(Color.WHITE);
        jScrollPane.setPreferredSize(new Dimension(300, 600));
        jPanel.add((Component)jScrollPane, "Center");
        JPanel jPanel2 = new JPanel(new FlowLayout(0));
        JButton jButton = new JButton("Expand All");
        jButton.setActionCommand("expand");
        jButton.addActionListener(this);
        jPanel2.add(jButton);
        jButton = new JButton("Collapse All");
        jButton.setActionCommand("collapse");
        jButton.addActionListener(this);
        jPanel2.add(jButton);
        jButton = new JButton("Close");
        jButton.setActionCommand("close");
        jButton.addActionListener(this);
        jPanel2.add(jButton);
        jPanel.add((Component)jPanel2, "Last");
        jPanel.setBackground(Color.WHITE);
        this.setBackground(Color.WHITE);
        this.setContentPane(jPanel);
        this.pack();
        this.setVisible(true);
        this.addWindowListener(this);
    }

    private void makeTree(Vector vector, JPlagOptions jPlagOptions) {
        if (this.tree != null) {
            this.tree.collapseRow(0);
        }
        this.root.removeAllChildren();
        if (vector == null) {
            return;
        }
        int n = new File(jPlagOptions.getOriginalDir()).getAbsolutePath().length() + 1;
        Iterator iterator = vector.iterator();
        while (iterator.hasNext()) {
            Submission submission = (Submission)iterator.next();
            String string = "\"" + submission.name + "\"";
            int n2 = submission.files.length;
            string = n2 == 0 ? string + " (!no files!)" : (n2 == 1 ? string + " (1 file)" : string + " (" + n2 + " files)");
            DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(string);
            for (int i = 0; i < n2; ++i) {
                File file = new File(submission.dir, submission.files[i]);
                String string2 = new File(file.getParent()).getAbsolutePath();
                String string3 = string2.length() >= n ? file.getAbsolutePath().substring(n + submission.name.length() + 1) : file.getAbsolutePath().substring(n);
                DefaultMutableTreeNode defaultMutableTreeNode2 = new DefaultMutableTreeNode(string3);
                defaultMutableTreeNode2.setAllowsChildren(false);
                defaultMutableTreeNode.add(defaultMutableTreeNode2);
            }
            this.root.add(defaultMutableTreeNode);
        }
        if (this.tree != null) {
            this.tree.expandRow(0);
        }
    }
}

