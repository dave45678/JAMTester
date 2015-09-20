/*
 * Decompiled with CFR 0_102.
 */
package org.jext.protocol.jextresource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.jext.JARClassLoader;

public class PluginResURLConnection
extends URLConnection {
    private InputStream in;

    public PluginResURLConnection(URL uRL) throws IOException {
        super(uRL);
        String string = uRL.getFile();
        int n = string.indexOf(47, 1);
        if (n == -1) {
            throw new IOException("Invalid plugin resource URL");
        }
        int n2 = string.charAt(0) == '/' ? 1 : 0;
        int n3 = Integer.parseInt(string.substring(n2, n));
        this.in = JARClassLoader.getClassLoader(n3).getResourceAsStream(string.substring(n + 1));
    }

    public void connect() {
    }

    public InputStream getInputStream() throws IOException {
        return this.in;
    }

    public String getHeaderField(String string) {
        if (string.equals("content-type")) {
            String string2 = this.getURL().getFile().toLowerCase();
            if (string2.endsWith(".html")) {
                return "text/html";
            }
            if (string2.endsWith(".txt")) {
                return "text/plain";
            }
            if (string2.endsWith(".rtf")) {
                return "text/rtf";
            }
            if (string2.endsWith(".gif")) {
                return "image/gif";
            }
            if (string2.endsWith(".jpg") || string2.endsWith(".jpeg")) {
                return "image/jpeg";
            }
            return null;
        }
        return null;
    }
}

