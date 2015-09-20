/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  gnu.regexp.RE
 *  gnu.regexp.REMatch
 *  gnu.regexp.RESyntax
 */
package org.jext.search;

import gnu.regexp.RE;
import gnu.regexp.REMatch;
import gnu.regexp.RESyntax;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Position;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.gui.AbstractDisposer;
import org.jext.gui.JextHighlightButton;
import org.jext.gui.ModifiedCellRenderer;
import org.jext.search.LiteralSearchMatcher;
import org.jext.search.SearchHighlight;
import org.jext.search.SearchResult;

public class FindAllDialog
extends JFrame
implements ActionListener {
    private JList results;
    private JextFrame parent;
    private JComboBox fieldSearch;
    private DefaultListModel resultModel;
    private JTextField fieldSearchEditor;
    private JextHighlightButton find;
    private JextHighlightButton cancel;
    private JCheckBox useRegexp;
    private JCheckBox ignoreCase;
    private JCheckBox highlight;

    public FindAllDialog(JextFrame jextFrame) {
        String string;
        super(Jext.getProperty("find.all.title"));
        this.parent = jextFrame;
        this.getContentPane().setLayout(new BorderLayout());
        this.fieldSearch = new JComboBox();
        this.fieldSearch.setRenderer(new ModifiedCellRenderer());
        this.fieldSearch.setEditable(true);
        this.fieldSearchEditor = (JTextField)this.fieldSearch.getEditor().getEditorComponent();
        this.fieldSearchEditor.addKeyListener(new KeyHandler());
        JPanel jPanel = new JPanel();
        jPanel.add(new JLabel(Jext.getProperty("find.all.label")));
        jPanel.add(this.fieldSearch);
        this.getContentPane().add((Component)jPanel, "North");
        JPanel jPanel2 = new JPanel();
        this.ignoreCase = new JCheckBox(Jext.getProperty("find.ignorecase.label"), Jext.getBooleanProperty("ignorecase.all"));
        jPanel2.add(this.ignoreCase);
        this.useRegexp = new JCheckBox(Jext.getProperty("find.useregexp.label"), Jext.getBooleanProperty("useregexp.all"));
        jPanel2.add(this.useRegexp);
        this.highlight = new JCheckBox(Jext.getProperty("find.all.highlight.label"), Jext.getBooleanProperty("highlight.all"));
        jPanel2.add(this.highlight);
        this.find = new JextHighlightButton(Jext.getProperty("find.all.button"));
        jPanel2.add(this.find);
        this.find.setMnemonic(Jext.getProperty("find.all.mnemonic").charAt(0));
        this.find.setToolTipText(Jext.getProperty("find.all.tip"));
        this.cancel = new JextHighlightButton(Jext.getProperty("general.cancel.button"));
        jPanel2.add(this.cancel);
        this.cancel.setMnemonic(Jext.getProperty("general.cancel.mnemonic").charAt(0));
        this.getContentPane().add((Component)jPanel2, "Center");
        this.resultModel = new DefaultListModel();
        this.results = new JList();
        this.results.setCellRenderer(new ModifiedCellRenderer());
        this.results.setVisibleRowCount(10);
        FontMetrics fontMetrics = this.getFontMetrics(this.results.getFont());
        this.results.addListSelectionListener(new ListHandler());
        this.results.setModel(this.resultModel);
        JScrollPane jScrollPane = new JScrollPane(this.results, 22, 32);
        this.getContentPane().add((Component)jScrollPane, "South");
        this.find.addActionListener(this);
        this.cancel.addActionListener(this);
        fontMetrics = this.getFontMetrics(this.fieldSearch.getFont());
        this.fieldSearch.setPreferredSize(new Dimension(30 * fontMetrics.charWidth('m'), this.fieldSearch.getPreferredSize().height));
        for (int i = 0; i < 25 && (string = Jext.getProperty("search.all.history." + i)) != null; ++i) {
            this.fieldSearch.addItem(string);
        }
        JextTextArea jextTextArea = jextFrame.getTextArea();
        if (!Jext.getBooleanProperty("use.selection")) {
            string = Jext.getProperty("find.all");
            this.addSearchHistory(string);
            this.fieldSearch.setSelectedItem(string);
        } else if (jextTextArea.getSelectedText() != null) {
            string = jextTextArea.getSelectedText();
            this.addSearchHistory(string);
            this.fieldSearch.setSelectedItem(string);
        }
        this.getRootPane().setDefaultButton(this.find);
        this.addKeyListener(new AbstractDisposer(this));
        this.setDefaultCloseOperation(0);
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                FindAllDialog.this.exit();
            }
        });
        this.setIconImage(GUIUtilities.getJextIconImage());
        this.pack();
        this.setResizable(false);
        Utilities.centerComponent(this);
        this.setVisible(true);
    }

    private void exit() {
        int n;
        Jext.setProperty("find.all", this.fieldSearchEditor.getText());
        for (n = 0; n < this.fieldSearch.getItemCount(); ++n) {
            Jext.setProperty("search.all.history." + n, (String)this.fieldSearch.getItemAt(n));
        }
        for (n = this.fieldSearch.getItemCount(); n < 25; ++n) {
            Jext.unsetProperty("search.all.history." + n);
        }
        Jext.setProperty("useregexp.all", this.useRegexp.isSelected() ? "on" : "off");
        Jext.setProperty("ignorecase.all", this.ignoreCase.isSelected() ? "on" : "off");
        Jext.setProperty("highlight.all", this.highlight.isSelected() ? "on" : "off");
        JextTextArea[] arrjextTextArea = this.parent.getTextAreas();
        for (int i = 0; i < arrjextTextArea.length; ++i) {
            SearchHighlight searchHighlight = arrjextTextArea[i].getSearchHighlight();
            if (searchHighlight == null) continue;
            searchHighlight.disable();
        }
        this.parent.getTextArea().repaint();
        this.dispose();
    }

    private void addSearchHistory() {
        this.addSearchHistory(this.fieldSearchEditor.getText());
    }

    private void addSearchHistory(String string) {
        int n;
        if (string == null) {
            return;
        }
        for (n = 0; n < this.fieldSearch.getItemCount(); ++n) {
            if (!((String)this.fieldSearch.getItemAt(n)).equals(string)) continue;
            return;
        }
        this.fieldSearch.insertItemAt(string, 0);
        if (this.fieldSearch.getItemCount() > 25) {
            for (n = 24; n < this.fieldSearch.getItemCount(); ++n) {
                this.fieldSearch.removeItemAt(n);
            }
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.cancel) {
            this.exit();
        } else if (object == this.find) {
            this.findAll();
        }
    }

    private void findAll() {
        JextTextArea jextTextArea;
        boolean bl;
        ArrayList<SearchResult> arrayList;
        String string = this.fieldSearchEditor.getText();
        if (string == null || string.length() == 0) {
            return;
        }
        Utilities.setCursorOnWait(this, true);
        this.addSearchHistory();
        this.resultModel.removeAllElements();
        jextTextArea = this.parent.getTextArea();
        arrayList = new ArrayList<SearchResult>();
        SyntaxDocument syntaxDocument = jextTextArea.getDocument();
        Element element = syntaxDocument.getDefaultRootElement();
        int n = element.getElementCount();
        bl = this.highlight.isSelected();
        boolean bl2 = this.useRegexp.isSelected();
        LiteralSearchMatcher literalSearchMatcher = null;
        if (!bl2) {
            literalSearchMatcher = new LiteralSearchMatcher(string, null, this.ignoreCase.isSelected());
        }
        try {
            for (int i = 1; i <= n; ++i) {
                int[] arrn;
                Element element2 = element.getElement(i - 1);
                int n2 = element2.getStartOffset();
                String string2 = syntaxDocument.getText(n2, element2.getEndOffset() - n2 - 1);
                int n3 = 0;
                do {
                    if ((arrn = bl2 ? this.nextMatch(string2, n3) : literalSearchMatcher.nextMatch(string2, n3)) == null) continue;
                    SearchResult searchResult = new SearchResult(jextTextArea, syntaxDocument.createPosition(n2 + arrn[0]), syntaxDocument.createPosition(n2 + arrn[1]));
                    this.resultModel.addElement(searchResult);
                    if (bl) {
                        arrayList.add(searchResult);
                    }
                    n3 = arrn[1];
                } while (arrn != null);
            }
        }
        catch (BadLocationException var10_11) {}
        finally {
            Utilities.setCursorOnWait(this, false);
        }
        if (this.resultModel.isEmpty()) {
            jextTextArea.getToolkit().beep();
        }
        this.results.setModel(this.resultModel);
        if (bl) {
            jextTextArea.initSearchHighlight();
            SearchHighlight searchHighlight = jextTextArea.getSearchHighlight();
            searchHighlight.trigger(true);
            searchHighlight.setMatches(arrayList);
        } else {
            SearchHighlight searchHighlight = jextTextArea.getSearchHighlight();
            if (searchHighlight != null) {
                searchHighlight.trigger(false);
                searchHighlight.setMatches(null);
            }
        }
        this.pack();
        jextTextArea.repaint();
    }

    private int[] nextMatch(String string, int n) {
        try {
            if (string.equals("") || string == null) {
                return null;
            }
            RE rE = new RE((Object)((String)this.fieldSearch.getSelectedItem()), this.ignoreCase.isSelected() ? 2 : 0, RESyntax.RE_SYNTAX_PERL5);
            if (rE == null) {
                this.getToolkit().beep();
                return null;
            }
            REMatch rEMatch = rE.getMatch((Object)string, n);
            if (rEMatch != null) {
                int[] arrn = new int[]{rEMatch.getStartIndex(), rEMatch.getEndIndex()};
                return arrn;
            }
        }
        catch (Exception var4_4) {
            // empty catch block
        }
        return null;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.results = null;
        this.parent = null;
        this.fieldSearch = null;
        this.resultModel = null;
        this.fieldSearchEditor = null;
        this.find = null;
        this.cancel = null;
        this.useRegexp = null;
        this.ignoreCase = null;
        this.highlight = null;
    }

    class KeyHandler
    extends KeyAdapter {
        KeyHandler() {
        }

        public void keyPressed(KeyEvent keyEvent) {
            switch (keyEvent.getKeyCode()) {
                case 10: {
                    FindAllDialog.this.findAll();
                    break;
                }
                case 27: {
                    FindAllDialog.this.exit();
                }
            }
        }
    }

    class ListHandler
    implements ListSelectionListener {
        ListHandler() {
        }

        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if (FindAllDialog.this.results.isSelectionEmpty() || listSelectionEvent.getValueIsAdjusting()) {
                return;
            }
            SearchResult searchResult = (SearchResult)FindAllDialog.this.results.getSelectedValue();
            int[] arrn = searchResult.getPos();
            searchResult.getTextArea().select(arrn[0], arrn[1]);
        }
    }

}

