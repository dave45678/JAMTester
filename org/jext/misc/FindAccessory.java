/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import org.jext.Jext;
import org.jext.misc.FindAccessory;
import org.jext.misc.FindByDate;
import org.jext.misc.FindByName;
import org.jext.misc.FindFilter;
import org.jext.misc.FindFilterFactory;
import org.jext.misc.FindProgressCallback;

public class FindAccessory
extends JPanel
implements Runnable,
ActionListener,
FindProgressCallback {
    public static final String ACCESSORY_NAME = Jext.getProperty("find.accessory.find");
    public static final int DEFAULT_MAX_SEARCH_HITS = 500;
    public static final String ACTION_START = Jext.getProperty("find.accessory.start");
    public static final String ACTION_STOP = Jext.getProperty("find.accessory.stop");
    protected JFileChooser chooser = null;
    protected FindAction actionStart = null;
    protected FindAction actionStop = null;
    protected Thread searchThread = null;
    protected boolean killFind = false;
    protected FindTabs searchTabs = null;
    protected FindControls controlPanel = null;
    protected int total = 0;
    protected int matches = 0;
    protected int maxMatches = 500;

    public FindAccessory() {
        this.setBorder(new TitledBorder(ACCESSORY_NAME));
        this.setLayout(new BorderLayout());
        this.actionStart = new FindAction(ACTION_START, null);
        this.actionStop = new FindAction(ACTION_STOP, null);
        this.searchTabs = new FindTabs();
        this.add((Component)this.searchTabs, "Center");
        this.controlPanel = new FindControls(this.actionStart, this.actionStop, true);
        this.add((Component)this.controlPanel, "South");
        this.setMinimumSize(this.getPreferredSize());
    }

    public FindAccessory(JFileChooser jFileChooser) {
        this();
        this.chooser = jFileChooser;
        this.register(this.chooser);
    }

    public FindAccessory(JFileChooser jFileChooser, int n) {
        this(jFileChooser);
        this.setMaxFindHits(n);
    }

    public void setMaxFindHits(int n) {
        this.maxMatches = n;
    }

    public int getMaxFindHits() {
        return this.maxMatches;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String string = actionEvent.getActionCommand();
        if (string == null) {
            return;
        }
        if (string.equals("ApproveSelection")) {
            this.quit();
        } else if (string.equals("CancelSelection")) {
            this.quit();
        }
    }

    public void goTo(File file) {
        Object object;
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        if (this.chooser == null) {
            return;
        }
        this.chooser.setFileSelectionMode(2);
        FileFilter fileFilter = this.chooser.getFileFilter();
        if (!(fileFilter == null || fileFilter.accept(file))) {
            object = this.chooser.getAcceptAllFileFilter();
            this.chooser.setFileFilter((FileFilter)object);
        }
        if ((object = new File(file.getParent())) != null) {
            this.chooser.setCurrentDirectory((File)object);
        }
        this.chooser.setSelectedFile(null);
        this.chooser.setSelectedFile(file);
        this.chooser.invalidate();
        this.chooser.repaint();
    }

    public synchronized void startThread() {
        if (this.searchTabs != null) {
            this.searchTabs.showFindResults();
        }
        this.killFind = false;
        if (this.searchThread == null) {
            this.searchThread = new Thread(this);
        }
        if (this.searchThread != null) {
            this.searchThread.start();
        }
    }

    public synchronized void stop() {
        this.killFind = true;
    }

    public boolean isRunning() {
        if (this.searchThread == null) {
            return false;
        }
        return this.searchThread.isAlive();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void run() {
        if (this.searchThread == null) {
            return;
        }
        if (Thread.currentThread() != this.searchThread) {
            return;
        }
        try {
            try {
                this.actionStart.setEnabled(false);
                this.actionStop.setEnabled(true);
                this.runFind(this.chooser.getCurrentDirectory(), this.newFind());
            }
            catch (InterruptedException interruptedException) {
                Object var3_2 = null;
                this.actionStart.setEnabled(true);
                this.actionStop.setEnabled(false);
                this.searchThread = null;
                return;
            }
            Object var3_1 = null;
            this.actionStart.setEnabled(true);
            this.actionStop.setEnabled(false);
            this.searchThread = null;
            return;
        }
        catch (Throwable var2_5) {
            Object var3_3 = null;
            this.actionStart.setEnabled(true);
            this.actionStop.setEnabled(false);
            this.searchThread = null;
            throw var2_5;
        }
    }

    protected void runFind(File file, FindFilter[] arrfindFilter) throws InterruptedException {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        if (arrfindFilter == null) {
            return;
        }
        if (this.killFind) {
            return;
        }
        File file2 = null;
        file2 = file.isDirectory() ? file : new File(file.getParent());
        File[] arrfile = file2.listFiles();
        for (int i = 0; i < arrfile.length; ++i) {
            ++this.total;
            if (this.accept(arrfile[i], arrfindFilter)) {
                ++this.matches;
                this.searchTabs.addFoundFile(arrfile[i]);
            }
            this.updateProgress();
            if (this.killFind) {
                return;
            }
            Thread.currentThread();
            Thread.sleep(0);
            if (arrfile[i].isDirectory()) {
                this.runFind(arrfile[i], arrfindFilter);
            }
            if (this.maxMatches <= 0 || this.matches < this.maxMatches) continue;
            return;
        }
    }

    protected boolean accept(File file, FindFilter[] arrfindFilter) {
        if (file == null) {
            return false;
        }
        if (arrfindFilter == null) {
            return false;
        }
        for (int i = 0; i < arrfindFilter.length; ++i) {
            if (arrfindFilter[i].accept(file, this)) continue;
            return false;
        }
        return true;
    }

    public boolean reportProgress(FindFilter findFilter, File file, long l, long l2) {
        return !this.killFind;
    }

    protected FindFilter[] newFind() {
        this.matches = 0;
        this.total = 0;
        this.updateProgress();
        if (this.searchTabs != null) {
            return this.searchTabs.newFind();
        }
        return null;
    }

    protected void updateProgress() {
        this.controlPanel.showProgress(this.matches, this.total);
    }

    protected void register(JFileChooser jFileChooser) {
        if (jFileChooser == null) {
            return;
        }
        jFileChooser.addActionListener(this);
    }

    protected void unregister(JFileChooser jFileChooser) {
        if (jFileChooser == null) {
            return;
        }
        jFileChooser.removeActionListener(this);
    }

    public void quit() {
        this.stop();
        this.unregister(this.chooser);
    }

    public void action(String string) {
        if (string == null) {
            return;
        }
        if (string.equals(ACTION_START)) {
            this.startThread();
        } else if (string.equals(ACTION_STOP)) {
            this.stop();
        }
    }

    class FindResults
    extends JPanel {
        protected DefaultListModel model;
        protected JList fileList;

        FindResults() {
            this.model = null;
            this.fileList = null;
            this.setLayout(new BorderLayout());
            this.model = new DefaultListModel();
            this.fileList = new JList(this.model);
            this.fileList.setSelectionMode(0);
            this.fileList.setCellRenderer(new FindResultsCellRenderer());
            this.add((Component)this.fileList, "Center");
            MouseAdapter mouseAdapter = new MouseAdapter(this){
                private final /* synthetic */ FindResults this$1;

                public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        try {
                            int n = this.this$1.fileList.locationToIndex(mouseEvent.getPoint());
                            FindResults.access$000(this.this$1).goTo((File)this.this$1.model.elementAt(n));
                        }
                        catch (Throwable var2_3) {
                            // empty catch block
                        }
                    }
                }
            };
            this.fileList.addMouseListener(mouseAdapter);
        }

        public void append(File file) {
            if (file == null) {
                return;
            }
            this.model.addElement(file);
        }

        public void clear() {
            if (this.model != null) {
                this.model.removeAllElements();
                this.invalidate();
                this.repaint();
            }
        }

        static /* synthetic */ FindAccessory access$000(FindResults findResults) {
            return findResults.FindAccessory.this;
        }

        class FindResultsCellRenderer
        extends JLabel
        implements ListCellRenderer {
            FindResultsCellRenderer() {
                this.setOpaque(true);
            }

            public Component getListCellRendererComponent(JList jList, Object object, int n, boolean bl, boolean bl2) {
                if (n == -1) {
                    int n2 = jList.getSelectedIndex();
                    if (n2 == -1) {
                        return this;
                    }
                    n = n2;
                }
                this.setOpaque(bl);
                this.setBorder(new EmptyBorder(1, 2, 1, 2));
                File file = (File)FindResults.this.model.elementAt(n);
                this.setText(file.getAbsolutePath());
                if (bl) {
                    this.setBackground(jList.getSelectionBackground());
                    this.setForeground(jList.getSelectionForeground());
                } else {
                    this.setBackground(Color.white);
                    this.setForeground(Color.black);
                }
                return this;
            }
        }

    }

    class FindTabs
    extends JTabbedPane {
        protected String TAB_NAME;
        protected String TAB_DATE;
        protected String TAB_RESULTS;
        protected FindResults resultsPanel;
        protected JScrollPane resultsScroller;

        FindTabs() {
            this.TAB_NAME = Jext.getProperty("find.accessory.name");
            this.TAB_DATE = Jext.getProperty("find.accessory.date");
            this.TAB_RESULTS = Jext.getProperty("find.accessory.found");
            this.resultsPanel = null;
            this.resultsScroller = null;
            this.addTab(this.TAB_NAME, new FindByName());
            this.addTab(this.TAB_DATE, new FindByDate());
            this.resultsPanel = new FindResults();
            this.resultsScroller = new JScrollPane(this.resultsPanel);
            this.resultsPanel.setDoubleBuffered(true);
            this.resultsScroller.setDoubleBuffered(true);
            this.addTab(this.TAB_RESULTS, this.resultsScroller);
        }

        public void addFoundFile(File file) {
            if (this.resultsPanel != null) {
                this.resultsPanel.append(file);
            }
        }

        public void showFindResults() {
            if (this.resultsScroller != null) {
                this.setSelectedComponent(this.resultsScroller);
            }
        }

        public FindFilter[] newFind() {
            if (this.resultsPanel != null) {
                this.resultsPanel.clear();
            }
            Dimension dimension = this.resultsScroller.getSize();
            this.resultsScroller.setMaximumSize(dimension);
            this.resultsScroller.setPreferredSize(dimension);
            Vector<FindFilter> vector = new Vector<FindFilter>();
            for (int i = 0; i < this.getTabCount(); ++i) {
                try {
                    FindFilterFactory findFilterFactory = (FindFilterFactory)this.getComponentAt(i);
                    FindFilter findFilter = findFilterFactory.createFindFilter();
                    if (findFilter == null) continue;
                    vector.addElement(findFilter);
                    continue;
                }
                catch (Throwable var4_6) {
                    // empty catch block
                }
            }
            if (vector.size() == 0) {
                return null;
            }
            FindFilter[] arrfindFilter = new FindFilter[vector.size()];
            for (int j = 0; j < arrfindFilter.length; ++j) {
                arrfindFilter[j] = (FindFilter)vector.elementAt(j);
            }
            return arrfindFilter;
        }
    }

    class FindControls
    extends JPanel {
        protected JLabel searchDirectory;
        protected JLabel progress;

        FindControls(FindAction findAction, FindAction findAction2, boolean bl) {
            this.searchDirectory = null;
            this.progress = null;
            this.setLayout(new BorderLayout());
            JToolBar jToolBar = new JToolBar();
            jToolBar.setFloatable(false);
            FindAccessory.this.actionStart = new FindAction(FindAccessory.ACTION_START, null);
            jToolBar.add(FindAccessory.this.actionStart);
            FindAccessory.this.actionStop = new FindAction(FindAccessory.ACTION_STOP, null);
            jToolBar.add(FindAccessory.this.actionStop);
            this.add((Component)jToolBar, "West");
            this.progress = new JLabel("", 4);
            this.progress.setDoubleBuffered(true);
            this.add((Component)this.progress, "East");
        }

        public void showProgress(int n, int n2) {
            if (this.progress == null) {
                return;
            }
            this.progress.setText(String.valueOf(n) + "/" + String.valueOf(n2));
        }
    }

    class FindAction
    extends AbstractAction {
        FindAction(String string, Icon icon) {
            super(string, icon);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            FindAccessory.this.action(actionEvent.getActionCommand());
        }
    }

}

