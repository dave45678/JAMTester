/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

public class PluginAuthor {
    private String name;
    private String email;
    private String content;

    public PluginAuthor(String string) {
        this.content = string;
    }

    public PluginAuthor(String string, String string2) {
        this.name = string;
        this.email = string2;
    }

    public String toString() {
        if (this.content != null) {
            return this.content;
        }
        this.content = "<a href=\"mailto:" + this.email + "\">" + this.name + "</a>";
        return this.content;
    }
}

