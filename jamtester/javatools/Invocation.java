/*
 * Decompiled with CFR 0_102.
 */
package jamtester.javatools;

import jamtester.GradingToolClassLoader;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

public class Invocation {
    private static ArrayList al;

    public static void main(String[] arrstring) throws Exception {
        al = new ArrayList();
        al.add("1");
        al.add("2");
        al.add("3");
        GradingToolClassLoader gradingToolClassLoader = new GradingToolClassLoader("", new URL[]{new URL("file://" + System.getProperty("user.dir"))});
        FileOutputStream fileOutputStream = new FileOutputStream("TESTING.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        Class class_ = Invocation.class;
        objectOutputStream.writeObject(class_);
    }
}

