/*
 * Decompiled with CFR 0_102.
 */
package org.jext.protocol.jextresource;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import org.jext.protocol.jextresource.PluginResURLConnection;

public class Handler
extends URLStreamHandler {
    public URLConnection openConnection(URL uRL) throws IOException {
        PluginResURLConnection pluginResURLConnection = new PluginResURLConnection(uRL);
        pluginResURLConnection.connect();
        return pluginResURLConnection;
    }
}

