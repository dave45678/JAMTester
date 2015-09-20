/*
 * Decompiled with CFR 0_102.
 */
package org.jext.scripting.dawn.functions;

import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.jext.GUIUtilities;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.MenuAction;
import org.jext.dawn.DawnParser;
import org.jext.dawn.DawnRuntimeException;
import org.jext.dawn.Function;
import org.jext.gui.JextMenu;
import org.jext.menus.JextMenuBar;
import org.jext.scripting.dawn.Run;
import org.jext.toolbar.JextToolBar;

public class CreateActionFunction
extends Function {
    public CreateActionFunction() {
        super("createAction");
    }

    public void invoke(DawnParser dawnParser) throws DawnRuntimeException {
        dawnParser.checkArgsNumber(this, 3);
        String string = dawnParser.popString();
        String string2 = dawnParser.popString();
        String string3 = dawnParser.popString();
        DawnAction dawnAction = new DawnAction(string, string3);
        Jext.addAction(dawnAction);
        JextFrame jextFrame = (JextFrame)dawnParser.getProperty("JEXT.JEXT_FRAME");
        JextMenu jextMenu = (JextMenu)jextFrame.getJextToolBar().getClientProperty("DAWN.DAWN_MENU");
        if (jextMenu == null) {
            jextMenu = new JextMenu("Dawn");
            jextFrame.getJextToolBar().putClientProperty("DAWN.DAWN_MENU", jextMenu);
            jextFrame.getJextMenuBar().addMenu(jextMenu, "Tools");
        }
        jextMenu.add(GUIUtilities.loadMenuItem(string2, string, null, true, true));
    }

    class DawnAction
    extends MenuAction {
        private String code;

        DawnAction(String string, String string2) {
            super(string);
            this.code = string2;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            Run.execute(this.code, DawnAction.getJextParent(actionEvent));
        }
    }

}

