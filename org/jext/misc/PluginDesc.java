/*
 * Decompiled with CFR 0_102.
 */
package org.jext.misc;

import java.net.URL;

public class PluginDesc {
    private URL srcUrl;
    private URL binUrl;
    private int srcSize;
    private int binSize;
    private String desc;
    private String name;
    private String displayName;
    private String release;
    private String[] deps;

    public PluginDesc() {
    }

    public PluginDesc(String string, String string2, String string3) {
        this.name = string;
        this.release = string2;
        this.displayName = string3;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void setDisplayName(String string) {
        this.displayName = string;
    }

    public void setRelease(String string) {
        this.release = string;
    }

    public void setDesc(String string) {
        this.desc = string;
    }

    public void setSrcUrl(URL uRL, int n) {
        this.srcUrl = uRL;
        this.srcSize = n;
    }

    public void setBinUrl(URL uRL, int n) {
        this.binUrl = uRL;
        this.binSize = n;
    }

    public URL getSrcUrl() {
        return this.srcUrl;
    }

    public URL getBinUrl() {
        return this.binUrl;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getRelease() {
        return this.release;
    }
}

