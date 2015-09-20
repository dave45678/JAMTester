/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import org.gjt.sp.jedit.syntax.SyntaxDocument;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.gui.AbstractDisposer;
import org.jext.gui.JextHighlightButton;

public class JextMail
extends JDialog
implements ActionListener,
Runnable {
    private int y = 0;
    private JPanel pane;
    private JextFrame parent;
    private Thread mailer;
    private JTextArea tracer;
    private boolean traceState;
    private JScrollPane scroller;
    private JextTextArea textArea;
    private GridBagLayout gridBag;
    private JextHighlightButton send;
    private JextHighlightButton cancel;
    private JextHighlightButton details;
    private JTextField host;
    private JTextField from;
    private JTextField to;
    private JTextField subject;

    protected void addComponent(String string, Component component) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy = this.y++;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.gridx = 0;
        JLabel jLabel = new JLabel(string, 4);
        this.gridBag.setConstraints(jLabel, gridBagConstraints);
        this.pane.add(jLabel);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridwidth = 1;
        this.gridBag.setConstraints(component, gridBagConstraints);
        this.pane.add(component);
    }

    public JextMail(JextTextArea jextTextArea) {
        super(jextTextArea.getJextParent(), Jext.getProperty("mail.title"), false);
        this.textArea = jextTextArea;
        this.parent = jextTextArea.getJextParent();
        this.getContentPane().setLayout(new BorderLayout());
        ((JPanel)this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.pane = new JPanel();
        this.gridBag = new GridBagLayout();
        this.pane.setLayout(this.gridBag);
        this.host = new JTextField(15);
        this.addComponent(Jext.getProperty("mail.host.label"), this.host);
        this.host.setCursor(Cursor.getPredefinedCursor(2));
        this.from = new JTextField(15);
        this.addComponent(Jext.getProperty("mail.from.label"), this.from);
        this.from.setCursor(Cursor.getPredefinedCursor(2));
        this.to = new JTextField(15);
        this.addComponent(Jext.getProperty("mail.to.label"), this.to);
        this.to.setCursor(Cursor.getPredefinedCursor(2));
        this.subject = new JTextField(15);
        this.addComponent(Jext.getProperty("mail.subject.label"), this.subject);
        this.subject.setCursor(Cursor.getPredefinedCursor(2));
        JPanel jPanel = new JPanel();
        this.send = new JextHighlightButton(Jext.getProperty("mail.send.button"));
        jPanel.add(this.send);
        this.send.setToolTipText(Jext.getProperty("mail.send.tip"));
        this.send.setMnemonic(Jext.getProperty("mail.send.mnemonic").charAt(0));
        this.send.addActionListener(this);
        this.getRootPane().setDefaultButton(this.send);
        this.cancel = new JextHighlightButton(Jext.getProperty("general.cancel.button"));
        jPanel.add(this.cancel);
        this.cancel.setMnemonic(Jext.getProperty("general.cancel.mnemonic").charAt(0));
        this.cancel.addActionListener(this);
        this.details = new JextHighlightButton(Jext.getProperty("mail.details.expand.button"));
        jPanel.add(this.details);
        this.details.setMnemonic(Jext.getProperty("mail.details.mnemonic").charAt(0));
        this.details.addActionListener(this);
        this.tracer = new JTextArea(5, 15);
        this.tracer.setEditable(false);
        this.scroller = new JScrollPane(this.tracer, 22, 32);
        this.getContentPane().add((Component)this.pane, "North");
        this.getContentPane().add((Component)jPanel, "Center");
        this.load();
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                JextMail.this.save();
                JextMail.this.parent.hideWaitCursor();
                JextMail.this.dispose();
            }
        });
        this.addKeyListener(new AbstractDisposer(this));
        this.pack();
        this.setResizable(false);
        Utilities.centerComponentChild(this.parent, this);
        this.setVisible(true);
    }

    private void load() {
        this.host.setText(Jext.getProperty("host"));
        this.from.setText(Jext.getProperty("from"));
        this.to.setText(Jext.getProperty("to"));
        this.subject.setText(Jext.getProperty("subject"));
    }

    private void save() {
        Jext.setProperty("host", this.host.getText());
        Jext.setProperty("from", this.from.getText());
        Jext.setProperty("to", this.to.getText());
        Jext.setProperty("subject", this.subject.getText());
    }

    private void wait(boolean bl) {
        this.send.setEnabled(!bl);
        this.host.setEnabled(!bl);
        this.to.setEnabled(!bl);
        this.from.setEnabled(!bl);
        this.subject.setEnabled(!bl);
        if (bl) {
            this.parent.showWaitCursor();
        } else {
            this.parent.hideWaitCursor();
        }
    }

    private void send() {
        if (!this.check()) {
            return;
        }
        this.mailer = new Thread(this);
        this.mailer.setPriority(1);
        this.mailer.setName("JextMail");
        this.mailer.start();
    }

    public void stop() {
        this.mailer = null;
    }

    public void run() {
        if (this.mailer != null) {
            this.wait(true);
            if (this.sendMail(this.host.getText(), this.from.getText(), this.to.getText(), this.subject.getText())) {
                Utilities.showMessage(Jext.getProperty("mail.successfully"));
            } else {
                Utilities.showMessage(Jext.getProperty("mail.cannot"));
            }
            this.wait(false);
        }
        this.stop();
    }

    private boolean check() {
        if (this.host.getText().equals("") || this.host.getText() == null) {
            Utilities.showMessage("Jext Mail", Jext.getProperty("mail.host"));
            return false;
        }
        if (this.from.getText().equals("") || this.from.getText().indexOf(64) == -1 || this.to.getText().equals("") || this.to.getText().indexOf(64) == -1) {
            Utilities.showMessage("Jext Mail", Jext.getProperty("mail.email"));
            return false;
        }
        return true;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if (object == this.send) {
            this.send();
        } else if (object == this.details) {
            this.showDetails();
        } else if (object == this.cancel) {
            this.save();
            this.dispose();
        }
    }

    private void showDetails() {
        if (this.traceState) {
            this.getContentPane().remove(this.scroller);
            this.details.setText(Jext.getProperty("mail.details.expand.button"));
        } else {
            this.getContentPane().add((Component)this.scroller, "South");
            this.details.setText(Jext.getProperty("mail.details.collapse.button"));
        }
        this.pack();
        Utilities.centerComponentChild(this.parent, this);
        this.traceState = !this.traceState;
    }

    private void trace(String string) {
        this.tracer.append(string + "\n");
        this.tracer.setCaretPosition(this.tracer.getDocument().getLength());
    }

    private boolean error(String string) {
        Utilities.showMessage("Jext Mail", Jext.getProperty("mail." + string + ".msg"));
        return false;
    }

    public boolean sendMail(String string, String string2, String string3, String string4) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
        }
        catch (UnknownHostException var9_6) {
            return false;
        }
        this.tracer.setText("");
        int n = string.indexOf(58);
        int n2 = 25;
        if (n != -1) {
            n2 = Integer.parseInt(string.substring(n + 1));
            string = string.substring(0, n);
        }
        try {
            Socket socket = new Socket(string, n2);
            if (socket == null) {
                return false;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            if (bufferedReader == null || outputStreamWriter == null) {
                return false;
            }
            String string5 = bufferedReader.readLine();
            this.trace(string5);
            if (!string5.startsWith("220")) {
                return this.error("serverdown");
            }
            String string6 = "HELO " + inetAddress.getHostName();
            outputStreamWriter.write(string6 + "\r\n");
            outputStreamWriter.flush();
            this.trace(string6);
            string5 = bufferedReader.readLine();
            this.trace(string5);
            if (!string5.startsWith("250")) {
                return this.error("badhost");
            }
            string6 = "MAIL FROM:<" + string2 + ">";
            outputStreamWriter.write(string6 + "\r\n");
            outputStreamWriter.flush();
            this.trace(string6);
            string5 = bufferedReader.readLine();
            this.trace(string5);
            if (!string5.startsWith("250")) {
                return this.error("badsender");
            }
            string6 = "RCPT TO:<" + string3 + ">";
            outputStreamWriter.write(string6 + "\r\n");
            outputStreamWriter.flush();
            this.trace(string6);
            string5 = bufferedReader.readLine();
            this.trace(string5);
            if (!string5.startsWith("250")) {
                return this.error("badrecepient");
            }
            outputStreamWriter.write("DATA\r\n");
            outputStreamWriter.flush();
            this.trace("DATA");
            string5 = bufferedReader.readLine();
            this.trace(string5);
            if (!string5.startsWith("354")) {
                return this.error("badmsg");
            }
            this.trace("[Sending mail...]");
            outputStreamWriter.write("To: <" + string3 + ">");
            outputStreamWriter.write("\r\n");
            outputStreamWriter.write("From: <" + string2 + ">");
            outputStreamWriter.write("\r\n");
            outputStreamWriter.write("Subject: " + string4);
            outputStreamWriter.write("\r\n");
            outputStreamWriter.write("X-Mailer: Jext 03.02.00.03");
            outputStreamWriter.write("\r\n");
            outputStreamWriter.write("\r\n");
            try {
                SyntaxDocument syntaxDocument = this.textArea.getDocument();
                Element element = syntaxDocument.getDefaultRootElement();
                int n3 = element.getElementCount();
                for (int i = 0; i < n3; ++i) {
                    Element element2 = element.getElement(i);
                    int n4 = element2.getStartOffset();
                    int n5 = element2.getEndOffset() - 1;
                    String string7 = syntaxDocument.getText(n4, n5-=n4);
                    if (string7.equals(".")) {
                        string7 = "!";
                    }
                    outputStreamWriter.write(string7);
                    outputStreamWriter.write("\r\n");
                    this.trace("[" + (i + 1) * 100 / n3 + "%]");
                }
            }
            catch (BadLocationException var13_20) {
                // empty catch block
            }
            outputStreamWriter.write(".\r\n");
            outputStreamWriter.flush();
            this.trace(".");
            string5 = bufferedReader.readLine();
            this.trace(string5);
            if (!string5.startsWith("250")) {
                return this.error("badmsg");
            }
            outputStreamWriter.write("QUIT");
            this.trace("QUIT");
            socket.close();
            socket = null;
        }
        catch (IOException var11_13) {
            return false;
        }
        return true;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.pane = null;
        this.parent = null;
        this.mailer = null;
        this.tracer = null;
        this.scroller = null;
        this.textArea = null;
        this.gridBag = null;
        this.send = null;
        this.cancel = null;
        this.details = null;
        this.host = null;
        this.from = null;
        this.to = null;
        this.subject = null;
    }

}

