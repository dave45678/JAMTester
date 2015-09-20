/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.console.ConsoleTextPane;
import org.jext.console.HistoryModel;
import org.jext.console.commands.ChangeDirCommand;
import org.jext.console.commands.ClearCommand;
import org.jext.console.commands.Command;
import org.jext.console.commands.EvalCommand;
import org.jext.console.commands.ExitCommand;
import org.jext.console.commands.FileCommand;
import org.jext.console.commands.HelpCommand;
import org.jext.console.commands.HomeCommand;
import org.jext.console.commands.HttpCommand;
import org.jext.console.commands.JythonCommand;
import org.jext.console.commands.ListCommand;
import org.jext.console.commands.PwdCommand;
import org.jext.console.commands.RunCommand;

public class Console
extends JScrollPane {
    public static final int DOS_PROMPT = 0;
    public static final int JEXT_PROMPT = 1;
    public static final int LINUX_PROMPT = 2;
    public static final int SUNOS_PROMPT = 3;
    public static final String[] DEFAULT_PROMPTS = new String[]{"$p >", "$u@$p >", "$u@$h$$ ", "$h% "};
    private static final String COMPLETION_SEPARATORS = " \t;:/\\\"'";
    private Command currentCmd;
    private Command firstCmd;
    private JextFrame parent;
    private Process process;
    private String processName;
    private StdoutThread stdout;
    private StderrThread stderr;
    private String current;
    private Document outputDocument;
    private ConsoleTextPane textArea;
    private HistoryModel historyModel = new HistoryModel(25);
    private int userLimit = 0;
    private int typingLocation = 0;
    private int index = -1;
    public Color errorColor = Color.red;
    public Color promptColor = Color.blue;
    public Color outputColor = Color.black;
    public Color infoColor = new Color(0, 165, 0);
    private boolean displayPath;
    private String prompt;
    private String hostName;
    private String oldPath = new String();
    private String promptPattern = DEFAULT_PROMPTS[1];
    private Command evalCom = null;
    private Writer writerSTDOUT;
    private Writer writeSTDERR;

    public Console(JextFrame jextFrame) {
        this(jextFrame, false);
    }

    public Console(JextFrame jextFrame, boolean bl) {
        super(22, 31);
        this.writerSTDOUT = new Writer(){

            public void close() {
            }

            public void flush() {
                Console.this.repaint();
            }

            public void write(char[] arrc, int n, int n2) {
            }
        };
        this.writeSTDERR = new Writer(){

            public void close() {
            }

            public void flush() {
                Console.this.repaint();
            }

            public void write(char[] arrc, int n, int n2) {
            }
        };
        this.parent = jextFrame;
        this.load();
        this.textArea = new ConsoleTextPane(this);
        this.textArea.setFont(new Font("Monospaced", 0, 11));
        this.outputDocument = this.textArea.getDocument();
        this.append(Jext.getProperty("console.welcome"), this.infoColor, false, true);
        if (bl) {
            this.displayPrompt();
        }
        this.getViewport().setView(this.textArea);
        FontMetrics fontMetrics = this.getFontMetrics(this.textArea.getFont());
        this.setPreferredSize(new Dimension(40 * fontMetrics.charWidth('m'), 6 * fontMetrics.getHeight()));
        this.setMinimumSize(this.getPreferredSize());
        this.setMaximumSize(this.getPreferredSize());
        this.initCommands();
    }

    public JextFrame getParentFrame() {
        return this.parent;
    }

    public Document getOutputDocument() {
        return this.outputDocument;
    }

    public void addCommand(Command command) {
        if (command == null) {
            return;
        }
        this.currentCmd.next = command;
        this.currentCmd = command;
    }

    private boolean builtInCommand(String string) {
        boolean bl = false;
        Command command = this.firstCmd;
        while (command != null) {
            if (command.handleCommand(this, string)) {
                bl = true;
                break;
            }
            command = command.next;
        }
        return bl;
    }

    private void initCommands() {
        this.firstCmd = this.currentCmd = new ClearCommand();
        this.addCommand(new JythonCommand());
        this.addCommand(new ChangeDirCommand());
        this.addCommand(new ExitCommand());
        this.addCommand(new FileCommand());
        this.addCommand(new HomeCommand());
        this.addCommand(new HttpCommand());
        this.addCommand(new HelpCommand());
        this.addCommand(new ListCommand());
        this.addCommand(new PwdCommand());
        this.addCommand(new RunCommand());
        this.evalCom = new EvalCommand();
        this.addCommand(this.evalCom);
    }

    public void setBgColor(Color color) {
        this.textArea.setBackground(color);
    }

    public void setErrorColor(Color color) {
        this.errorColor = color;
    }

    public void setPromptColor(Color color) {
        this.promptColor = color;
    }

    public void setOutputColor(Color color) {
        this.outputColor = color;
        this.textArea.setForeground(color);
        this.textArea.setCaretColor(color);
    }

    public void setInfoColor(Color color) {
        this.infoColor = color;
    }

    public void setSelectionColor(Color color) {
        this.textArea.setSelectionColor(color);
    }

    public void save() {
        for (int i = 0; i < this.historyModel.getSize(); ++i) {
            Jext.setProperty("console.history." + i, this.historyModel.getItem(i));
        }
    }

    public void load() {
        for (int i = 24; i >= 0; --i) {
            String string = Jext.getProperty("console.history." + i);
            if (string == null) continue;
            this.historyModel.addItem(string);
        }
    }

    public void setPromptPattern(String string) {
        if (string == null) {
            return;
        }
        this.promptPattern = string;
        this.displayPath = false;
        this.buildPrompt();
    }

    public String getPromptPattern() {
        return this.promptPattern;
    }

    public void displayPrompt() {
        if (this.prompt == null || this.displayPath) {
            this.buildPrompt();
        }
        if (Jext.getBooleanProperty("console.jythonMode")) {
            this.append("\n[python] " + this.prompt, this.promptColor);
        } else {
            this.append("" + '\n' + this.prompt, this.promptColor);
        }
        this.typingLocation = this.userLimit = this.outputDocument.getLength();
    }

    private void buildPrompt() {
        if (this.displayPath && this.oldPath.equals(System.getProperty("user.dir"))) {
            return;
        }
        this.displayPath = false;
        StringBuffer stringBuffer = new StringBuffer();
        if (this.hostName == null) {
            try {
                this.hostName = InetAddress.getLocalHost().getHostName();
            }
            catch (UnknownHostException var2_2) {
                // empty catch block
            }
        }
        block11 : for (int i = 0; i < this.promptPattern.length(); ++i) {
            char c = this.promptPattern.charAt(i);
            switch (c) {
                case '$': {
                    if (i == this.promptPattern.length() - 1) {
                        stringBuffer.append(c);
                        continue block11;
                    }
                    switch (this.promptPattern.charAt(++i)) {
                        case 'p': {
                            this.oldPath = System.getProperty("user.dir");
                            stringBuffer.append(this.oldPath);
                            this.displayPath = true;
                            continue block11;
                        }
                        case 'u': {
                            stringBuffer.append(System.getProperty("user.name"));
                            continue block11;
                        }
                        case 'h': {
                            stringBuffer.append(this.hostName);
                            continue block11;
                        }
                        case '$': {
                            stringBuffer.append('$');
                        }
                    }
                    continue block11;
                }
                default: {
                    stringBuffer.append(c);
                }
            }
        }
        this.prompt = stringBuffer.toString();
    }

    private void append(String string, Color color, boolean bl, boolean bl2) {
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        if (color != null) {
            simpleAttributeSet.addAttribute(StyleConstants.Foreground, color);
        }
        StyleConstants.setBold(simpleAttributeSet, bl2);
        StyleConstants.setItalic(simpleAttributeSet, bl);
        try {
            this.outputDocument.insertString(this.outputDocument.getLength(), string, simpleAttributeSet);
        }
        catch (BadLocationException var6_6) {
            // empty catch block
        }
        this.textArea.setCaretPosition(this.outputDocument.getLength());
    }

    public void append(String string, Color color) {
        this.append(string, color, false, false);
    }

    public void addHistory(String string) {
        this.historyModel.addItem(string);
        this.index = -1;
    }

    public void removeChar() {
        try {
            int n = this.textArea.getCaretPosition();
            if (n <= this.typingLocation && n > this.userLimit) {
                this.outputDocument.remove(n - 1, 1);
                --this.typingLocation;
            }
        }
        catch (BadLocationException var1_2) {
            // empty catch block
        }
    }

    public void deleteChar() {
        try {
            int n = this.textArea.getCaretPosition();
            if (n == this.outputDocument.getLength()) {
                return;
            }
            if (n < this.typingLocation && n >= this.userLimit) {
                this.outputDocument.remove(n, 1);
                --this.typingLocation;
            }
        }
        catch (BadLocationException var1_2) {
            // empty catch block
        }
    }

    public void add(String string) {
        try {
            int n = this.textArea.getCaretPosition();
            if (n <= this.typingLocation && n >= this.userLimit) {
                this.outputDocument.insertString(n, string, null);
            }
            this.typingLocation+=string.length();
        }
        catch (BadLocationException var2_3) {
            // empty catch block
        }
    }

    public int getUserLimit() {
        return this.userLimit;
    }

    public int getTypingLocation() {
        return this.typingLocation;
    }

    public void doCompletion() {
        int n;
        String string;
        int n2;
        String string2;
        int n3 = 0;
        int n4 = this.textArea.getCaretPosition() - this.userLimit;
        String string3 = this.getText();
        try {
            string = this.outputDocument.getText(this.userLimit, n4);
        }
        catch (BadLocationException var5_5) {
            return;
        }
        for (int i = string.length() - 1; i >= 0; --i) {
            if (" \t;:/\\\"'".indexOf(string.charAt(i)) == -1) continue;
            if (i == n3) {
                return;
            }
            n3 = i + 1;
            break;
        }
        String string4 = string.substring(n3);
        ArrayList<String> arrayList = new ArrayList<String>();
        String[] arrstring = Utilities.getWildCardMatches("*", true);
        if (arrstring == null) {
            return;
        }
        for (n2 = 0; n2 < arrstring.length; ++n2) {
            if (!arrstring[n2].startsWith(string4)) continue;
            arrayList.add(arrstring[n2]);
        }
        if (arrayList.size() == 0) {
            return;
        }
        n2 = 0;
        int n5 = 0;
        int n6 = 0;
        for (n = 0; n < arrayList.size(); ++n) {
            n5 = ((String)arrayList.get(n)).length();
            int n7 = n2 = n2 < n5 ? n5 : n2;
            if (n2 != n5) continue;
            n6 = n;
        }
        boolean bl = true;
        int n8 = n2;
        String string5 = (String)arrayList.get(n6);
        for (int j = 0; j < n2; ++j) {
            n = string5.charAt(j);
            for (int k = 0; k < arrayList.size(); ++k) {
                if (k == n6 || j >= (string2 = (String)arrayList.get(k)).length()) continue;
                bl = string2.charAt(j) == n;
            }
            if (bl) continue;
            n8 = j;
            break;
        }
        string2 = string.substring(0, n3) + string5.substring(0, n8);
        this.setText(string2 + string3.substring(n4));
        this.textArea.setCaretPosition(this.userLimit + string2.length());
    }

    public void doBackwardSearch() {
        String string = this.getText();
        if (string == null) {
            this.historyPrevious();
            return;
        }
        for (int i = this.index + 1; i < this.historyModel.getSize(); ++i) {
            String string2 = this.historyModel.getItem(i);
            if (!string2.startsWith(string)) continue;
            this.setText(string2);
            this.index = i;
            return;
        }
    }

    public void historyPrevious() {
        if (this.index == this.historyModel.getSize() - 1) {
            this.getToolkit().beep();
        } else if (this.index == -1) {
            this.current = this.getText();
            this.setText(this.historyModel.getItem(0));
            this.index = 0;
        } else {
            int n = this.index + 1;
            this.setText(this.historyModel.getItem(n));
            this.index = n;
        }
    }

    public void historyNext() {
        if (this.index == -1) {
            this.getToolkit().beep();
        } else if (this.index == 0) {
            this.setText(this.current);
        } else {
            int n = this.index - 1;
            this.setText(this.historyModel.getItem(n));
            this.index = n;
        }
    }

    public void setText(String string) {
        try {
            this.outputDocument.remove(this.userLimit, this.typingLocation - this.userLimit);
            this.outputDocument.insertString(this.userLimit, string, null);
            this.typingLocation = this.outputDocument.getLength();
            this.index = -1;
        }
        catch (BadLocationException var2_2) {
            // empty catch block
        }
    }

    public String getText() {
        try {
            return this.outputDocument.getText(this.userLimit, this.typingLocation - this.userLimit);
        }
        catch (BadLocationException var1_1) {
            return null;
        }
    }

    public void output(String string) {
        this.append("" + '\n' + string, this.outputColor, false, false);
    }

    public void help() {
        Command command = this.firstCmd;
        StringBuffer stringBuffer = new StringBuffer();
        while (command != null) {
            stringBuffer.append("   - ").append(command.getCommandName());
            stringBuffer.append(Utilities.createWhiteSpace(30 - command.getCommandName().length())).append('(');
            stringBuffer.append(command.getCommandSummary()).append(')').append('\n');
            command = command.next;
        }
        this.help(Jext.getProperty("console.help", new String[]{stringBuffer.toString()}));
    }

    public void help(String string) {
        this.append("" + '\n' + string, this.infoColor, true, true);
    }

    public void error(String string) {
        this.append("" + '\n' + string, this.errorColor, false, false);
    }

    public void stop() {
        if (this.stdout != null) {
            this.stdout.interrupt();
            this.stdout = null;
        }
        if (this.stderr != null) {
            this.stderr.interrupt();
            this.stderr = null;
        }
        if (this.process != null) {
            this.process.destroy();
            Object[] arrobject = new Object[]{this.processName};
            this.error(Jext.getProperty("console.killed", arrobject));
            this.processName = null;
            this.process = null;
        }
    }

    public String parseCommand(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        block14 : for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            switch (c) {
                case '$': {
                    if (i == string.length() - 1) {
                        stringBuffer.append(c);
                        continue block14;
                    }
                    switch (string.charAt(++i)) {
                        String string2;
                        case 'f': {
                            string2 = this.parent.getTextArea().getCurrentFile();
                            if (string2 == null) break;
                            stringBuffer.append(string2);
                            continue block14;
                        }
                        case 'd': {
                            stringBuffer.append(Utilities.getUserDirectory());
                            continue block14;
                        }
                        case 'p': {
                            stringBuffer.append(this.parent.getTextArea().getName());
                            continue block14;
                        }
                        case 'e': {
                            string2 = this.parent.getTextArea().getName();
                            int n = string2.lastIndexOf(46);
                            if (n != -1 && n + 1 < string2.length()) {
                                stringBuffer.append(string2.substring(0, n));
                                continue block14;
                            }
                            stringBuffer.append(string2);
                            continue block14;
                        }
                        case 'n': {
                            string2 = this.parent.getTextArea().getCurrentFile();
                            if (string2 == null) break;
                            stringBuffer.append(string2.substring(0, string2.lastIndexOf(File.separator)));
                            continue block14;
                        }
                        case 'h': {
                            stringBuffer.append(Utilities.getHomeDirectory());
                            continue block14;
                        }
                        case 'j': {
                            stringBuffer.append(Jext.getHomeDirectory());
                            continue block14;
                        }
                        case 's': {
                            stringBuffer.append(this.parent.getTextArea().getSelectedText());
                            continue block14;
                        }
                        case '$': {
                            stringBuffer.append('$');
                        }
                    }
                    continue block14;
                }
                default: {
                    stringBuffer.append(c);
                }
            }
        }
        return stringBuffer.toString();
    }

    public void execute(String string) {
        int n;
        this.stop();
        if (string.length() == 0 || string == null) {
            return;
        }
        boolean bl = Jext.getBooleanProperty("console.jythonMode");
        if (bl) {
            String string2;
            if (!string.startsWith("!")) {
                if (string.startsWith("?")) {
                    String string3 = string.substring(1);
                    string = "print " + string3;
                } else if (string.startsWith("exit")) {
                    Jext.setProperty("console.jythonMode", "off");
                    this.displayPrompt();
                    return;
                }
                this.evalCom.handleCommand(this, "eval:" + string);
                this.displayPrompt();
                return;
            }
            string = string2 = string.substring(1);
        }
        this.processName = (n = string.indexOf(32)) != -1 ? string.substring(0, n) : string;
        if ((string = this.parseCommand(string)) == null || string.length() == 0) {
            return;
        }
        if (this.builtInCommand(string)) {
            this.displayPrompt();
            return;
        }
        this.append("\n> " + string, this.infoColor);
        try {
            this.process = Utilities.JDK_VERSION.charAt(2) < '3' ? Runtime.getRuntime().exec(string) : Runtime.getRuntime().exec(string, null, new File(System.getProperty("user.dir")));
            this.process.getOutputStream().close();
        }
        catch (IOException var4_6) {
            this.error(Jext.getProperty("console.error"));
            this.displayPrompt();
            return;
        }
        this.stdout = new StdoutThread();
        this.stderr = new StderrThread();
        if (this.process == null) {
            this.displayPrompt();
        }
    }

    public Writer getStdOut() {
        return this.writerSTDOUT;
    }

    public Writer getStdErr() {
        return this.writeSTDERR;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.currentCmd = null;
        this.firstCmd = null;
        this.parent = null;
        this.process = null;
        this.processName = null;
        this.stdout = null;
        this.stderr = null;
        this.current = null;
        this.outputDocument = null;
        this.textArea = null;
        this.historyModel = null;
        this.infoColor = null;
        this.prompt = null;
        this.hostName = null;
        this.oldPath = null;
        this.promptPattern = null;
    }

    class StderrThread
    extends Thread {
        StderrThread() {
            super("----thread: stderr: jext----");
            this.start();
        }

        public void run() {
            try {
                String string;
                if (Console.this.process == null) {
                    return;
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Console.this.process.getErrorStream()));
                while ((string = bufferedReader.readLine()) != null) {
                    Console.this.append("" + '\n' + string, Console.this.errorColor);
                }
                bufferedReader.close();
            }
            catch (IOException var1_2) {
            }
            catch (NullPointerException var1_3) {
                // empty catch block
            }
        }
    }

    class StdoutThread
    extends Thread {
        StdoutThread() {
            super("----thread: stout: jext----");
            this.start();
        }

        public void run() {
            try {
                String string;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Console.this.process.getInputStream()));
                while ((string = bufferedReader.readLine()) != null) {
                    Console.this.output(string);
                }
                bufferedReader.close();
                int n = Console.this.process.waitFor();
                Object[] arrobject = new Object[]{Console.this.processName, new Integer(n)};
                Console.this.append("" + '\n' + Jext.getProperty("console.exited", arrobject), Console.this.infoColor);
                Thread.sleep(500);
                Console.this.process.destroy();
                Console.this.processName = null;
                Console.this.process = null;
                Console.this.displayPrompt();
            }
            catch (IOException var1_2) {
            }
            catch (InterruptedException var1_3) {
            }
            catch (NullPointerException var1_4) {
                // empty catch block
            }
        }
    }

}

