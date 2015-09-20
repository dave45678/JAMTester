/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.Utilities;
import org.jext.console.Console;
import org.jext.console.commands.Command;

public class HttpCommand
extends Command {
    private static final String COMMAND_NAME = "http://";

    public String getCommandName() {
        return "http://url";
    }

    public String getCommandSummary() {
        return Jext.getProperty("console.http.command.help");
    }

    public boolean handleCommand(Console console, String string) {
        if (string.startsWith("http://")) {
            boolean bl = true;
            JextFrame jextFrame = console.getParentFrame();
            JextTextArea jextTextArea = jextFrame.createFile();
            try {
                URL uRL = new URL(string);
                jextTextArea.open(string, new InputStreamReader(uRL.openStream()), 1024);
                bl = false;
            }
            catch (MalformedURLException var6_7) {
                Utilities.showError(Jext.getProperty("url.malformed"));
            }
            catch (IOException var6_8) {
                Utilities.showError(var6_8.toString());
            }
            if (bl) {
                jextFrame.close(jextTextArea);
            }
            return true;
        }
        return false;
    }
}

