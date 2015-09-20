/*
 * Decompiled with CFR 0_102.
 */
package org.jext.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.MenuAction;
import org.jext.Utilities;

public class OpenUrl
extends MenuAction {
    public OpenUrl() {
        super("open_url");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JextFrame jextFrame = OpenUrl.getJextParent(actionEvent);
        JextTextArea jextTextArea = jextFrame.createFile();
        String string = JOptionPane.showInputDialog(OpenUrl.getJextParent(actionEvent), Jext.getProperty("openurl.label"), Jext.getProperty("openurl.title"), 3);
        boolean bl = true;
        if (string != null) {
            try {
                URL uRL = new URL(string);
                jextTextArea.open(string, new InputStreamReader(uRL.openStream()), 1024, true, false);
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
        } else {
            jextFrame.close(jextTextArea);
        }
    }
}

