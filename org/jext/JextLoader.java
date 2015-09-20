/*
 * Decompiled with CFR 0_102.
 */
package org.jext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;

final class JextLoader
implements Runnable {
    private int port;
    private File auth = new File(Jext.SETTINGS_DIRECTORY + ".auth-key");
    private String key;
    private Thread tServer;
    private ServerSocket server;

    JextLoader() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.auth));
            this.port = Math.abs(new Random().nextInt()) % 16383;
            String string = Integer.toString(this.port);
            this.key = Integer.toString(Math.abs(new Random().nextInt()) % (int)Math.pow(2.0, 30.0));
            bufferedWriter.write(string, 0, string.length());
            bufferedWriter.newLine();
            bufferedWriter.write(this.key, 0, this.key.length());
            bufferedWriter.flush();
            bufferedWriter.close();
            this.server = new ServerSocket(49152 + this.port);
        }
        catch (IOException var1_2) {
            var1_2.printStackTrace();
        }
        this.tServer = new Thread(this);
        this.tServer.start();
    }

    public void stop() {
        this.tServer.interrupt();
        this.tServer = null;
        try {
            if (this.server != null) {
                this.server.close();
            }
            this.auth.delete();
        }
        catch (IOException var1_1) {
            // empty catch block
        }
    }

    public void run() {
        while (this.tServer != null) {
            try {
                ArrayList arrayList;
                Object[] arrobject;
                Object object;
                Socket socket = this.server.accept();
                if (socket == null) continue;
                if (!"127.0.0.1".equals(socket.getLocalAddress().getHostAddress())) {
                    socket.close();
                    Jext.stopServer();
                    this.intrusion();
                    return;
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String string = bufferedReader.readLine();
                if (string == null) continue;
                if (string.startsWith("load_jext:") && string.endsWith(":" + this.key)) {
                    arrayList = new Vector(1);
                    object = new StringTokenizer(string.substring(10, string.length() - (this.key.length() + 1)), "?");
                    while (object.hasMoreTokens()) {
                        arrayList.addElement(object.nextToken());
                    }
                    if (arrayList.size() > 0) {
                        arrobject = new String[arrayList.size()];
                        arrayList.copyInto(arrobject);
                        arrayList = null;
                        if (Jext.getBooleanProperty("jextLoader.newWindow")) {
                            Jext.newWindow((String[])arrobject);
                        } else if (!Jext.isRunningBg()) {
                            ArrayList arrayList2 = Jext.getInstances();
                            if (arrayList2.size() != 0) {
                                JextFrame jextFrame = (JextFrame)arrayList2.get(0);
                                for (int i = 0; i < arrobject.length; ++i) {
                                    jextFrame.open((String)arrobject[i]);
                                }
                            } else {
                                Jext.newWindow((String[])arrobject);
                            }
                        } else {
                            Jext.newWindow((String[])arrobject);
                        }
                    } else {
                        Jext.newWindow();
                    }
                    socket.close();
                    continue;
                }
                if (string.equals("kill:" + this.key)) {
                    if (!Jext.isRunningBg()) continue;
                    object = arrayList = Jext.getInstances();
                    synchronized (object) {
                        arrobject = null;
                        if (!(arrayList.size() != 0 && (arrayList.size() != 1 || (arrobject = (Object[])arrayList.get(0)).isVisible()))) {
                            if (arrayList.size() != 0) {
                                Jext.closeToQuit((JextFrame)arrobject, true);
                            }
                            Jext.finalCleanupAndExit();
                        }
                        continue;
                    }
                }
                socket.close();
                Jext.stopServer();
                this.intrusion();
                return;
            }
            catch (IOException var1_2) {
                continue;
            }
        }
    }

    private void intrusion() {
        JOptionPane.showMessageDialog(null, "An intrusion is attempted against your system !\nJext will close its opened sockets to preserve system integrity.\nYou should warn the network administrator.", "Intrusion attempt...", 2);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.auth = null;
        this.key = null;
        this.tServer = null;
        this.server = null;
    }
}

