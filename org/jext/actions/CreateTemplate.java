/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  gnu.regexp.RE
 *  gnu.regexp.REMatch
 */
package org.jext.actions;

import gnu.regexp.RE;
import gnu.regexp.REMatch;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Set;
import javax.swing.JOptionPane;
import org.jext.Jext;
import org.jext.JextFrame;
import org.jext.JextTextArea;
import org.jext.MenuAction;
import org.jext.Utilities;

public class CreateTemplate
extends MenuAction {
    public CreateTemplate() {
        super("create_template");
    }

    public void actionPerformed(ActionEvent actionEvent) {
        try {
            String string = actionEvent.getActionCommand();
            String string2 = this.loadFile(string);
            JextFrame jextFrame = CreateTemplate.getJextParent(actionEvent);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("____", "__");
            this.addTokensFromInput(jextFrame, string2, hashMap);
            jextFrame.open(this.saveOutput(jextFrame, this.replace(string2, hashMap)));
        }
        catch (Exception var2_3) {
            System.err.println(var2_3);
        }
    }

    private String loadFile(String string) throws Exception {
        String string2;
        File file = new File(string);
        if (!(file.exists() && file.canRead())) {
            throw new Exception("Could not read file " + file.getName());
        }
        StringBuffer stringBuffer = new StringBuffer((int)file.length());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while ((string2 = bufferedReader.readLine()) != null) {
            stringBuffer.append(string2).append('\n');
        }
        bufferedReader.close();
        return stringBuffer.toString();
    }

    private void addTokensFromInput(JextFrame jextFrame, String string, HashMap hashMap) throws Exception {
        String string2 = "__([^_]|_[^_])*__";
        RE rE = new RE((Object)string2);
        REMatch[] arrrEMatch = rE.getAllMatches((Object)string);
        for (int i = 0; i < arrrEMatch.length; ++i) {
            String string3 = arrrEMatch[i].toString();
            String string4 = "";
            if (hashMap.containsKey(string3)) continue;
            String string5 = string3.substring(2, string3.length() - 2);
            string4 = (String)JOptionPane.showInputDialog(jextFrame, Jext.getProperty("templates.input", new String[]{string5}), Jext.getProperty("templates.title"), 3, null, null, string5);
            hashMap.put(string3, string4);
        }
    }

    private String replace(String string, HashMap hashMap) throws Exception {
        String string2 = string;
        String[] arrstring = hashMap.keySet().toArray(new String[0]);
        for (int i = 0; i < arrstring.length; ++i) {
            String string3 = arrstring[i];
            if (string3.equals("____")) continue;
            RE rE = new RE((Object)string3);
            string2 = rE.substituteAll((Object)string2, (String)hashMap.get(string3));
        }
        RE rE = new RE((Object)"____");
        string2 = rE.substituteAll((Object)string2, (String)hashMap.get("__"));
        return string2;
    }

    private String saveOutput(JextFrame jextFrame, String string) throws Exception {
        String string2 = Utilities.chooseFile(jextFrame, 1);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(string2));
        bufferedWriter.write(string, 0, string.length());
        bufferedWriter.flush();
        bufferedWriter.close();
        return string2;
    }
}

