/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  gnu.regexp.RE
 *  gnu.regexp.REException
 *  gnu.regexp.RESyntax
 */
package org.jext;

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.RESyntax;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.gjt.sp.jedit.syntax.TokenMarker;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.TextAreaHighlight;
import org.gjt.sp.jedit.textarea.TextAreaPainter;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTabbedPane;
import org.jext.JextTextArea;
import org.jext.Mode;
import org.jext.Utilities;
import org.jext.misc.Indent;
import org.jext.misc.Workspaces;
import org.jext.misc.ZipExplorer;
import org.jext.search.LiteralSearchMatcher;
import org.jext.search.Search;
import org.jext.search.SearchHighlight;
import org.jext.xml.XPopupReader;

public class JextTextArea
extends JEditTextArea
implements UndoableEditListener,
DocumentListener {
    private static JPopupMenu popupMenu;
    private JextFrame parent;
    private String mode;
    private long modTime;
    private Position anchor;
    private int fontSize;
    private int fontStyle;
    private String fontName;
    private String currentFile;
    private JComboBox lineTermSelector;
    private boolean undoing;
    private UndoManager undo = new UndoManager();
    private CompoundEdit compoundEdit;
    private CompoundEdit currentEdit = new CompoundEdit();
    private boolean dirty;
    private boolean newf;
    private boolean operation;
    private boolean protectedCompoundEdit;
    private SearchHighlight searchHighlight;
    public static final int BUFFER_SIZE = 32768;
    public static final int DOS_LINE_END = 0;
    public static final int MACOS_LINE_END = 1;
    public static final int UNIX_LINE_END = 2;
    private String myLineTerm = "\n";
    private String origLineTerm = "\n";
    static /* synthetic */ Class class$org$jext$Jext;

    public JextTextArea(JextFrame jextFrame) {
        super(jextFrame);
        this.addCaretListener(new CaretHandler());
        this.addFocusListener(new FocusHandler());
        this.setMouseWheel();
        this.undo.setLimit(1000);
        this.setBorder(null);
        this.getPainter().setInvalidLinesPainted(false);
        this.parent = jextFrame;
        Font font = new Font("Monospaced", 0, 12);
        this.fontName = font.getName();
        this.fontSize = font.getSize();
        this.fontStyle = font.getStyle();
        this.setFont(font);
        this.modTime = -1;
        if (popupMenu == null) {
            new JextTextAreaPopupMenu(this);
        } else {
            this.setRightClickPopup(popupMenu);
        }
        this.newf = true;
        this.setTabSize(8);
        this.resetLineTerm();
        FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
        this.setMinimumSize(new Dimension(40 * fontMetrics.charWidth('m'), 5 * fontMetrics.getHeight()));
        this.setPreferredSize(new Dimension(80 * fontMetrics.charWidth('m'), 15 * fontMetrics.getHeight()));
        this.lineTermSelector = new JComboBox<String>(new String[]{"DOS", "Mac", "UNIX"});
        this.add(LEFT_OF_SCROLLBAR, this.lineTermSelector);
        this.lineTermSelector.setSelectedItem(this.getLineTermName());
        this.lineTermSelector.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent actionEvent) {
                JextTextArea.this.setLineTerm(JextTextArea.this.lineTermSelector.getSelectedIndex());
                JextFrame jextFrame = JextTextArea.this.getJextParent();
                jextFrame.updateStatus(JextTextArea.this);
                if (JextTextArea.this.isDirty()) {
                    jextFrame.getTabbedPane().setDirtyIcon(JextTextArea.this);
                } else {
                    jextFrame.getTabbedPane().setCleanIcon(JextTextArea.this);
                }
                jextFrame.setStatus(JextTextArea.this);
            }
        });
        this.mode = "";
    }

    private void setLineTerm(String string) {
        this.myLineTerm = string;
    }

    private String getLineTerm() {
        return this.myLineTerm;
    }

    private void resetLineTerm() {
        this.myLineTerm = Jext.getProperty("editor.newLine");
        this.storeOrigLineTerm();
    }

    private void storeOrigLineTerm() {
        this.origLineTerm = this.myLineTerm;
    }

    public boolean isLineTermChanged() {
        if (this.myLineTerm != null) {
            return !this.myLineTerm.equals(this.origLineTerm);
        }
        return false;
    }

    void setLineTerm(int n) {
        switch (n) {
            case 2: {
                this.myLineTerm = "\n";
                break;
            }
            case 1: {
                this.myLineTerm = "\r";
                break;
            }
            case 0: {
                this.myLineTerm = "\r\n";
            }
        }
    }

    String getLineTermName() {
        if (this.myLineTerm == null) {
            return "UNIX";
        }
        if (this.myLineTerm.equals("\r")) {
            return "Mac";
        }
        if (this.myLineTerm.equals("\n")) {
            return "UNIX";
        }
        if (this.myLineTerm.equals("\r\n")) {
            return "DOS";
        }
        return "UNIX";
    }

    void rotateLineTerm() {
        if (this.myLineTerm.equals("\r")) {
            this.myLineTerm = "\n";
        } else if (this.myLineTerm.equals("\n")) {
            this.myLineTerm = "\r\n";
        } else if (this.myLineTerm.equals("\r\n")) {
            this.myLineTerm = "\r";
        }
        if (this.isLineTermChanged()) {
            this.parent.setChanged(this);
        } else if (!this.isDirty()) {
            this.parent.setSaved(this);
        }
        this.lineTermSelector.setSelectedItem(this.getLineTermName());
    }

    private void setMouseWheel() {
        if (Utilities.JDK_VERSION.charAt(2) >= '4') {
            try {
                Class class_ = Class.forName("org.jext.JavaSupport");
                Method method = class_.getMethod("setMouseWheel", this.getClass());
                if (method != null) {
                    method.invoke(null, this);
                }
            }
            catch (Exception var1_2) {
                // empty catch block
            }
        }
    }

    public void initSearchHighlight() {
        if (this.searchHighlight == null) {
            this.searchHighlight = new SearchHighlight();
            this.getPainter().addCustomHighlight(this.searchHighlight);
        }
    }

    public SearchHighlight getSearchHighlight() {
        return this.searchHighlight;
    }

    public static JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public String getProperty(String string) {
        return Jext.getProperty("mode." + this.mode + '.' + string);
    }

    public void setDocument(SyntaxDocument syntaxDocument) {
        syntaxDocument.removeUndoableEditListener(this);
        syntaxDocument.removeDocumentListener(this);
        super.setDocument(syntaxDocument);
        syntaxDocument.addDocumentListener(this);
        syntaxDocument.addUndoableEditListener(this);
    }

    public String getFontName() {
        return this.fontName;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public int getFontStyle() {
        return this.fontStyle;
    }

    public void setFontName(String string) {
        this.fontName = string;
        this.changeFont();
    }

    public void setFontSize(int n) {
        this.fontSize = n;
        this.changeFont();
        FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
        this.setMinimumSize(new Dimension(80 * fontMetrics.charWidth('m'), 5 * fontMetrics.getHeight()));
        this.repaint();
    }

    public void setFontStyle(int n) {
        this.fontStyle = n;
        this.changeFont();
        this.repaint();
    }

    private void changeFont() {
        this.getPainter().setFont(new Font(this.fontName, this.fontStyle, this.fontSize));
    }

    public void waitingCursor(boolean bl) {
        if (bl) {
            this.parent.showWaitCursor();
        } else {
            this.parent.hideWaitCursor();
        }
    }

    public static boolean getTabIndent() {
        return Jext.getBooleanProperty("editor.tabIndent");
    }

    public static boolean getEnterIndent() {
        return Jext.getBooleanProperty("editor.enterIndent");
    }

    public static boolean getSoftTab() {
        return Jext.getBooleanProperty("editor.softTab");
    }

    public void beginOperation() {
        this.operation = true;
        this.waitingCursor(true);
    }

    public void endOperation() {
        this.operation = false;
        this.waitingCursor(false);
    }

    public JextFrame getJextParent() {
        return this.parent;
    }

    public boolean getOperation() {
        return this.operation;
    }

    public File getFile() {
        return this.currentFile == null ? null : new File(this.currentFile);
    }

    public String getCurrentFile() {
        return this.currentFile;
    }

    public void setCurrentFile(String string) {
        this.currentFile = string;
    }

    public void filteredPaste() {
        if (this.editable) {
            Clipboard clipboard = this.getToolkit().getSystemClipboard();
            try {
                String string = ((String)clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor)).replace('\r', '\n');
                String string2 = null;
                if (Search.getFindPattern().length() > 0) {
                    if (Jext.getBooleanProperty("useregexp")) {
                        RE rE = new RE((Object)Search.getFindPattern(), (Jext.getBooleanProperty("ignorecase") ? 2 : 0) | 8, RESyntax.RE_SYNTAX_PERL5);
                        if (rE == null) {
                            return;
                        }
                        string2 = rE.substituteAll((Object)string, Search.getReplacePattern());
                    } else {
                        LiteralSearchMatcher literalSearchMatcher = new LiteralSearchMatcher(Search.getFindPattern(), Search.getReplacePattern(), Jext.getBooleanProperty("ignorecase"));
                        string2 = literalSearchMatcher.substitute(string);
                    }
                }
                if (string2 == null) {
                    string2 = string;
                }
                this.setSelectedText(string2);
            }
            catch (Exception var2_3) {
                this.getToolkit().beep();
            }
        }
    }

    public void newFile() {
        this.beginOperation();
        if (this.isDirty() && !this.isEmpty()) {
            Object[] arrobject = new String[]{this.getName()};
            int n = JOptionPane.showConfirmDialog(this.parent, Jext.getProperty("general.save.question", arrobject), Jext.getProperty("general.save.title"), 1, 3);
            switch (n) {
                case 0: {
                    this.saveContent();
                    break;
                }
                case 1: {
                    break;
                }
                case 2: {
                    this.endOperation();
                    return;
                }
                default: {
                    this.endOperation();
                    return;
                }
            }
        }
        this.document.removeUndoableEditListener(this);
        this.document.removeDocumentListener(this);
        this.clean();
        this.discard();
        this.setEditable(true);
        this.setText("");
        this.anchor = null;
        this.modTime = -1;
        this.newf = true;
        this.resetLineTerm();
        this.currentFile = null;
        this.searchHighlight = null;
        this.document.addUndoableEditListener(this);
        this.document.addDocumentListener(this);
        this.parent.setNew(this);
        this.parent.setTextAreaName(this, Jext.getProperty("textarea.untitled"));
        this.parent.fireJextEvent(this, 11);
        this.setParentTitle();
        this.endOperation();
    }

    public void autoSave() {
        if (!this.isNew()) {
            this.saveContent();
        }
    }

    public void insert(String string, int n) {
        this.setCaretPosition(n);
        this.setSelectedText(string);
    }

    public void userInput(char c) {
        String string = this.getProperty("indentOpenBrackets");
        String string2 = this.getProperty("indentCloseBrackets");
        if (string2 != null && string2.indexOf(c) != -1 || string != null && string.indexOf(c) != -1) {
            Indent.indent(this, this.getCaretLine(), false, true);
        }
    }

    public int getTabSize() {
        String string = Jext.getProperty("editor.tabSize");
        if (string == null) {
            return 8;
        }
        Integer n = new Integer(string);
        if (n != null) {
            return n;
        }
        return 8;
    }

    public void setTabSize(int n) {
        this.document.putProperty("tabSize", new Integer(n));
    }

    public void setParentTitle() {
        if (this.currentFile == null) {
            Workspaces workspaces;
            this.parent.setTitle("Jext - " + Jext.getProperty("textarea.untitled") + ((workspaces = this.parent.getWorkspaces()) == null ? "" : new StringBuffer().append(" [").append(workspaces.getName()).append(']').toString()));
            return;
        }
        String string = Jext.getBooleanProperty("full.filename", "off") ? Utilities.getShortStringOf(this.currentFile, 80) : this.getFileName(this.currentFile);
        Workspaces workspaces = this.parent.getWorkspaces();
        this.parent.setTitle("Jext - " + string + (workspaces == null ? "" : new StringBuffer().append(" [").append(workspaces.getName()).append(']').toString()));
    }

    private String getFileName(String string) {
        if (string == null) {
            return Jext.getProperty("textarea.untitled");
        }
        return string.substring(string.lastIndexOf(File.separator) + 1);
    }

    public String getName() {
        return this.getFileName(this.currentFile);
    }

    public void setColorizing(String string) {
        this.enableColorizing(string, Jext.getMode(string).getTokenMarker());
    }

    public void setColorizing(Mode mode) {
        this.enableColorizing(mode.getModeName(), mode.getTokenMarker());
    }

    private void enableColorizing(String string, TokenMarker tokenMarker) {
        if (string == null || tokenMarker == null || string.equals(this.mode)) {
            return;
        }
        this.setTokenMarker(tokenMarker);
        this.mode = string;
        this.getPainter().setBracketHighlightEnabled("on".equals(this.getProperty("bracketHighlight")));
        Jext.setProperty("editor.colorize.mode", string);
        this.parent.fireJextEvent(this, 1);
        this.repaint();
    }

    public void setColorizingMode(String string) {
        this.mode = string;
    }

    public String getColorizingMode() {
        return this.mode;
    }

    public void checkLastModificationTime() {
        if (this.modTime == -1) {
            return;
        }
        File file = this.getFile();
        if (file == null) {
            return;
        }
        long l = file.lastModified();
        if (l > this.modTime) {
            Object[] arrobject;
            String string = this.isDirty() ? "textarea.filechanged.dirty.message" : "textarea.filechanged.focus.message";
            int n = JOptionPane.showConfirmDialog(this.parent, Jext.getProperty(string, arrobject = new Object[]{this.currentFile}), Jext.getProperty("textarea.filechanged.title"), 0, 2);
            if (n == 0) {
                this.open(this.currentFile);
            } else {
                this.modTime = l;
            }
        }
    }

    public void zipContent() {
        if (this.getText().length() == 0) {
            return;
        }
        if (this.isNew()) {
            Utilities.showMessage("Please save your file before zipping it !");
            return;
        }
        String string = Utilities.chooseFile(this.parent, 1);
        if (string != null) {
            if (!string.endsWith(".zip")) {
                string = string + ".zip";
            }
            if (!new File(string).exists()) {
                this.zip(string);
            } else {
                int n = JOptionPane.showConfirmDialog(this.parent, Jext.getProperty("textarea.file.exists", new Object[]{string}), Jext.getProperty("general.save.title"), 0, 3);
                switch (n) {
                    case 0: {
                        this.zip(string);
                        break;
                    }
                    case 1: {
                        break;
                    }
                    default: {
                        return;
                    }
                }
            }
        }
    }

    public void zip(String string) {
        this.waitingCursor(true);
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(string));
            zipOutputStream.putNextEntry(new ZipEntry(new File(this.currentFile).getName()));
            String string2 = this.getLineTerm();
            Element element = this.document.getDefaultRootElement();
            for (int i = 0; i < element.getElementCount(); ++i) {
                Element element2 = element.getElement(i);
                int n = element2.getStartOffset();
                byte[] arrby = (this.getText(n, element2.getEndOffset() - n - 1) + string2).getBytes();
                zipOutputStream.write(arrby, 0, arrby.length);
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
        }
        catch (IOException var2_3) {
            Utilities.showError(Jext.getProperty("textarea.zip.error"));
        }
        this.waitingCursor(false);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void saveContent() {
        if (!this.isEditable()) {
            return;
        }
        if (this.isNew()) {
            String string = Utilities.chooseFile(this.parent, 1);
            if (string == null) return;
            if (!new File(string).exists()) {
                this.save(string);
                return;
            }
            int n = JOptionPane.showConfirmDialog(this.parent, Jext.getProperty("textarea.file.exists", new Object[]{string}), Jext.getProperty("general.save.title"), 0, 3);
            switch (n) {
                case 0: {
                    this.save(string);
                    return;
                }
                case 1: {
                    return;
                }
                default: {
                    return;
                }
            }
        }
        if (!this.isDirty()) return;
        this.save(this.currentFile);
    }

    public void save(String string) {
        this.waitingCursor(true);
        try {
            int n;
            File file = new File(string);
            long l = file.lastModified();
            if (this.modTime != -1 && l > this.modTime && (n = JOptionPane.showConfirmDialog(this.parent, Jext.getProperty("textarea.filechanged.save.message", new Object[]{string}), Jext.getProperty("textarea.filechanged.title"), 0, 2)) != 0) {
                this.waitingCursor(false);
                return;
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(file), Jext.getProperty("editor.encoding", System.getProperty("file.encoding"))), 32768);
            Segment segment = new Segment();
            String string2 = this.getLineTerm();
            Element element = this.document.getDefaultRootElement();
            for (int i = 0; i < element.getElementCount() - 1; ++i) {
                Element element2 = element.getElement(i);
                int n2 = element2.getStartOffset();
                this.document.getText(n2, element2.getEndOffset() - n2 - 1, segment);
                bufferedWriter.write(segment.array, segment.offset, segment.count);
                bufferedWriter.write(string2);
            }
            Element element3 = element.getElement(element.getElementCount() - 1);
            int n3 = element3.getStartOffset();
            this.document.getText(n3, element3.getEndOffset() - n3 - 1, segment);
            bufferedWriter.write(segment.array, segment.offset, segment.count);
            if (Jext.getBooleanProperty("editor.extra_line_feed")) {
                bufferedWriter.write(string2);
            }
            bufferedWriter.close();
            this.storeOrigLineTerm();
            if (!string.equals(this.currentFile)) {
                this.parent.setTextAreaName(this, this.getFileName(string));
                this.parent.saveRecent(string);
                this.currentFile = string;
                this.setParentTitle();
            }
            file = new File(string);
            this.modTime = file.lastModified();
            if (this.isNew()) {
                this.newf = false;
            }
            this.clean();
            this.parent.setSaved(this);
        }
        catch (Exception var2_3) {
            Utilities.showError(Jext.getProperty("textarea.save.error"));
        }
        this.waitingCursor(false);
    }

    public void open(String string) {
        this.open(string, null, 0);
    }

    public void open(String string, boolean bl) {
        this.open(string, null, 0, false, bl);
    }

    public void open(String string, InputStreamReader inputStreamReader, int n) {
        this.open(string, inputStreamReader, n, false, true);
    }

    public void open(String string, InputStreamReader inputStreamReader, int n, boolean bl, boolean bl2) {
        this.beginOperation();
        if (string.endsWith(".zip") || string.endsWith(".jar")) {
            new ZipExplorer(this.parent, this, string);
            this.endOperation();
            return;
        }
        this.document.removeUndoableEditListener(this);
        this.document.removeDocumentListener(this);
        this.clean();
        this.discard();
        this.anchor = null;
        this.modTime = -1;
        try {
            int n2;
            StringBuffer stringBuffer;
            File file;
            InputStreamReader inputStreamReader2;
            if (inputStreamReader == null) {
                file = new File(string);
                if (!file.canWrite()) {
                    this.setEditable(false);
                } else if (!this.isEditable()) {
                    this.setEditable(true);
                }
                stringBuffer = new StringBuffer((int)file.length());
                inputStreamReader2 = new InputStreamReader((InputStream)new FileInputStream(file), Jext.getProperty("editor.encoding", System.getProperty("file.encoding")));
            } else {
                inputStreamReader2 = inputStreamReader;
                if (n == 0) {
                    n = 131072;
                }
                stringBuffer = new StringBuffer(n);
            }
            file = (File)new char[32768];
            boolean bl3 = false;
            boolean bl4 = false;
            boolean bl5 = false;
            boolean bl6 = false;
            while ((n2 = inputStreamReader2.read((char[])file, 0, file.length)) != -1) {
                int n3 = 0;
                block16 : for (int i = 0; i < n2; ++i) {
                    switch (file[i]) {
                        case 13: {
                            if (bl6) {
                                bl5 = true;
                                bl4 = false;
                            } else {
                                bl6 = true;
                            }
                            stringBuffer.append((char[])file, n3, i - n3);
                            stringBuffer.append('\n');
                            n3 = i + 1;
                            continue block16;
                        }
                        case 10: {
                            if (bl6) {
                                bl5 = false;
                                bl4 = true;
                                bl6 = false;
                                n3 = i + 1;
                                continue block16;
                            }
                            bl5 = false;
                            bl4 = false;
                            stringBuffer.append((char[])file, n3, i - n3);
                            stringBuffer.append('\n');
                            n3 = i + 1;
                            continue block16;
                        }
                        default: {
                            if (!bl6) continue block16;
                            bl5 = true;
                            bl4 = false;
                            bl6 = false;
                        }
                    }
                }
                stringBuffer.append((char[])file, n3, n2 - n3);
            }
            inputStreamReader2.close();
            inputStreamReader2 = null;
            this.resetLineTerm();
            if (Jext.getBooleanProperty("editor.line_end.preserve")) {
                if (bl5) {
                    this.setLineTerm("\r");
                } else if (bl4) {
                    this.setLineTerm("\r\n");
                } else {
                    this.setLineTerm("\n");
                }
            }
            this.storeOrigLineTerm();
            this.lineTermSelector.setSelectedItem(this.getLineTermName());
            if (stringBuffer.length() != 0 && stringBuffer.charAt(stringBuffer.length() - 1) == '\n') {
                stringBuffer.setLength(stringBuffer.length() - 1);
            }
            this.document.remove(0, this.getLength());
            this.document.insertString(0, stringBuffer.toString(), null);
            stringBuffer = null;
            this.setCaretPosition(0);
            this.parent.setNew(this);
            if (inputStreamReader == null) {
                this.parent.setTextAreaName(this, this.getFileName(string));
                if (bl2) {
                    this.parent.saveRecent(string);
                }
                this.currentFile = string;
                this.newf = false;
                this.modTime = this.getFile().lastModified();
            } else {
                this.currentFile = !bl ? new File(string).getName() : string.substring(string.lastIndexOf(47) + 1);
                this.parent.setTextAreaName(this, this.currentFile);
                this.newf = true;
                this.setDirty();
                this.parent.setChanged(this);
                inputStreamReader.close();
                inputStreamReader = null;
            }
            this.setParentTitle();
            String string2 = string.toLowerCase();
            boolean bl7 = false;
            for (int i = Jext.modes.size() - 1; i >= 0; --i) {
                Mode mode = (Mode)Jext.modes.get(i);
                if (mode == null) continue;
                String string3 = mode.getModeName();
                if (string3.equals("plain")) continue;
                try {
                    RE rE = new RE((Object)Utilities.globToRE(Jext.getProperty("mode." + string3 + ".fileFilter")), 2);
                    if (!rE.isMatch((Object)string2)) continue;
                    this.setColorizing(string3);
                    bl7 = true;
                    break;
                }
                catch (REException var20_27) {
                    // empty catch block
                }
            }
            if (!bl7) {
                this.setColorizing("plain");
            }
            this.document.addUndoableEditListener(this);
            this.document.addDocumentListener(this);
            this.parent.fireJextEvent(this, 10);
        }
        catch (BadLocationException var6_8) {
            var6_8.printStackTrace();
        }
        catch (FileNotFoundException var6_9) {
            Object[] arrobject = new String[]{string};
            Utilities.showError(Jext.getProperty("textarea.file.notfound", arrobject));
        }
        catch (IOException var6_10) {
            Utilities.showError(var6_10.toString());
        }
        finally {
            this.endOperation();
        }
    }

    public void setNewFlag(boolean bl) {
        this.newf = bl;
        this.resetLineTerm();
        this.lineTermSelector.setSelectedItem(this.getLineTermName());
    }

    public boolean isNew() {
        return this.newf;
    }

    public boolean isEmpty() {
        if (this.getLength() == 0) {
            return true;
        }
        return false;
    }

    public boolean isDirty() {
        return this.dirty || this.isLineTermChanged();
    }

    private void setDirty() {
        this.dirty = true;
    }

    public void clean() {
        this.dirty = false;
    }

    public void discard() {
        this.undo.discardAllEdits();
    }

    public void setAnchor() {
        try {
            this.anchor = this.document.createPosition(this.getCaretPosition());
        }
        catch (BadLocationException var1_1) {
            // empty catch block
        }
    }

    public void gotoAnchor() {
        if (this.anchor == null) {
            this.getToolkit().beep();
        } else {
            this.setCaretPosition(this.anchor.getOffset());
        }
    }

    public int getAnchorOffset() {
        if (this.anchor == null) {
            return -1;
        }
        return this.anchor.getOffset();
    }

    public UndoManager getUndo() {
        return this.undo;
    }

    public void beginCompoundEdit() {
        this.beginCompoundEdit(true);
    }

    public void beginCompoundEdit(boolean bl) {
        if (!(this.compoundEdit != null || this.protectedCompoundEdit)) {
            this.endCurrentEdit();
            this.compoundEdit = new CompoundEdit();
            if (bl) {
                this.waitingCursor(true);
            }
        }
    }

    public void beginProtectedCompoundEdit() {
        if (!this.protectedCompoundEdit) {
            this.beginCompoundEdit(true);
            this.protectedCompoundEdit = true;
        }
    }

    public void endCompoundEdit() {
        this.endCompoundEdit(true);
    }

    public void endCompoundEdit(boolean bl) {
        if (!(this.compoundEdit == null || this.protectedCompoundEdit)) {
            this.compoundEdit.end();
            if (this.compoundEdit.canUndo()) {
                this.undo.addEdit(this.compoundEdit);
            }
            this.compoundEdit = null;
            if (bl) {
                this.waitingCursor(false);
            }
        }
    }

    public void endProtectedCompoundEdit() {
        if (this.protectedCompoundEdit) {
            this.protectedCompoundEdit = false;
            this.endCompoundEdit(true);
        }
    }

    public int getLength() {
        return this.document.getLength();
    }

    public void undoableEditHappened(UndoableEditEvent undoableEditEvent) {
        if (!this.getOperation()) {
            if (this.compoundEdit == null) {
                this.currentEdit.addEdit(undoableEditEvent.getEdit());
            } else {
                this.compoundEdit.addEdit(undoableEditEvent.getEdit());
            }
        }
    }

    public void endCurrentEdit() {
        if (this.currentEdit.isSignificant()) {
            this.currentEdit.end();
            if (this.currentEdit.canUndo()) {
                this.undo.addEdit(this.currentEdit);
            }
            this.currentEdit = new CompoundEdit();
        }
    }

    public void setUndoing(boolean bl) {
        this.undoing = bl;
    }

    public void changedUpdate(DocumentEvent documentEvent) {
        if (!this.getOperation()) {
            if (!this.isDirty()) {
                this.parent.setChanged(this);
            }
            this.setDirty();
        }
        this.parent.fireJextEvent(this, 2);
    }

    public void insertUpdate(DocumentEvent documentEvent) {
        if (!this.getOperation()) {
            if (!this.isDirty()) {
                this.parent.setChanged(this);
            }
            this.setDirty();
        }
        if (this.undoing) {
            if (documentEvent.getLength() == 1) {
                this.setCaretPosition(documentEvent.getOffset() + 1);
            } else {
                this.setCaretPosition(documentEvent.getOffset());
            }
        }
        this.parent.fireJextEvent(this, 3);
    }

    public void removeUpdate(DocumentEvent documentEvent) {
        this.parent.updateStatus(this);
        if (!this.getOperation()) {
            if (!this.isDirty()) {
                this.parent.setChanged(this);
            }
            this.setDirty();
        }
        if (this.undoing) {
            this.setCaretPosition(documentEvent.getOffset());
        }
        this.parent.fireJextEvent(this, 4);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("JextTextArea: ");
        stringBuffer.append("[filename: " + this.getCurrentFile() + ";");
        stringBuffer.append(" filesize: " + this.getLength() + "] -");
        stringBuffer.append(" [is dirty: " + this.isDirty() + ";");
        stringBuffer.append(" is new: " + this.isNew() + ";");
        if (this.anchor != null) {
            stringBuffer.append(" anchor: " + this.anchor.getOffset() + "] -");
        } else {
            stringBuffer.append(" anchor: not defined] -");
        }
        stringBuffer.append(" [font-name: " + this.getFontName() + ";");
        stringBuffer.append(" font-style: " + this.getFontStyle() + ";");
        stringBuffer.append(" font-size: " + this.getFontSize() + "] -");
        stringBuffer.append(" [syntax mode: " + this.mode + "]");
        return stringBuffer.toString();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.parent = null;
        this.mode = null;
        this.anchor = null;
        this.fontName = null;
        this.currentFile = null;
        this.undo = null;
        this.compoundEdit = null;
        this.currentEdit = null;
    }

    class JextTextAreaPopupMenu
    extends Thread {
        private JextTextArea area;

        JextTextAreaPopupMenu(JextTextArea jextTextArea2) {
            super("---Thread:JextTextArea Popup---");
            this.area = jextTextArea2;
            this.start();
        }

        public void run() {
            Class class_ = JextTextArea.class$org$jext$Jext == null ? (JextTextArea.class$org$jext$Jext = JextTextArea.class$("org.jext.Jext")) : JextTextArea.class$org$jext$Jext;
            popupMenu = XPopupReader.read(class_.getResourceAsStream("jext.textarea.popup.xml"), "jext.textarea.popup.xml");
            if (Jext.getFlatMenus()) {
                popupMenu.setBorder(LineBorder.createBlackLineBorder());
            }
            this.area.setRightClickPopup(popupMenu);
        }
    }

    class CaretHandler
    implements CaretListener {
        CaretHandler() {
        }

        public void caretUpdate(CaretEvent caretEvent) {
            JextTextArea.this.parent.updateStatus(JextTextArea.this);
        }
    }

    class FocusHandler
    extends FocusAdapter {
        FocusHandler() {
        }

        public void focusGained(FocusEvent focusEvent) {
            if (!JextTextArea.this.parent.getBatchMode()) {
                SwingUtilities.invokeLater(new Runnable(this){
                    private final /* synthetic */ FocusHandler this$1;

                    public void run() {
                        FocusHandler.access$200(this.this$1).checkLastModificationTime();
                    }
                });
            }
        }

        static /* synthetic */ JextTextArea access$200(FocusHandler focusHandler) {
            return focusHandler.JextTextArea.this;
        }
    }

}

