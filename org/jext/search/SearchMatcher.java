/*
 * Decompiled with CFR 0_102.
 */
package org.jext.search;

import javax.swing.text.Segment;

public interface SearchMatcher {
    public int[] nextMatch(Segment var1);

    public String substitute(String var1) throws Exception;
}

