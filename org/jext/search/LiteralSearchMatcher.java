/*
 * Decompiled with CFR 0_102.
 */
package org.jext.search;

public class LiteralSearchMatcher {
    private char[] search;
    private String replace;
    private boolean ignoreCase;

    public LiteralSearchMatcher(String string, String string2, boolean bl) {
        this.search = bl ? string.toUpperCase().toCharArray() : string.toCharArray();
        this.replace = string2;
        this.ignoreCase = bl;
    }

    public int[] nextMatch(String string) {
        return this.nextMatch(string, 0);
    }

    public int[] nextMatch(String string, int n) {
        int n2;
        int n3;
        int n4;
        char[] arrc = string.toCharArray();
        int n5 = arrc.length - (n3 = this.search.length) + 1;
        if (n >= n5) {
            return null;
        }
        n2 = -1;
        if (this.ignoreCase) {
            block0 : for (n4 = n; n4 < n5; ++n4) {
                if (Character.toUpperCase(arrc[n4]) != this.search[0]) continue;
                for (int i = 1; i < n3; ++i) {
                    if (Character.toUpperCase(arrc[n4 + i]) == this.search[i]) continue;
                    n4+=i - 1;
                    continue block0;
                }
                n2 = n4;
                break;
            }
        } else {
            block2 : for (n4 = n; n4 < n5; ++n4) {
                if (arrc[n4] != this.search[0]) continue;
                for (int i = 1; i < n3; ++i) {
                    if (arrc[n4 + i] == this.search[i]) continue;
                    n4+=i - 1;
                    continue block2;
                }
                n2 = n4;
                break;
            }
        }
        if (n2 == -1) {
            return null;
        }
        int[] arrn = new int[]{n2, n2 + n3};
        return arrn;
    }

    public String substitute(String string) {
        StringBuffer stringBuffer = null;
        char[] arrc = string.toCharArray();
        int n = 0;
        int n2 = this.search.length;
        int n3 = arrc.length - n2 + 1;
        boolean bl = false;
        if (this.ignoreCase) {
            int n4 = 0;
            block0 : while (n4 < n3) {
                if (Character.toUpperCase(arrc[n4]) == this.search[0]) {
                    for (int i = 1; i < n2; ++i) {
                        if (Character.toUpperCase(arrc[n4 + i]) == this.search[i]) continue;
                        n4+=i;
                        continue block0;
                    }
                    if (stringBuffer == null) {
                        stringBuffer = new StringBuffer();
                    }
                    if (n4 != n) {
                        stringBuffer.append(arrc, n, n4 - n);
                    }
                    stringBuffer.append(this.replace);
                    n = n4+=n2;
                    bl = true;
                    continue;
                }
                ++n4;
            }
        } else {
            int n5 = 0;
            block2 : while (n5 < n3) {
                if (arrc[n5] == this.search[0]) {
                    for (int i = 1; i < n2; ++i) {
                        if (arrc[n5 + i] == this.search[i]) continue;
                        n5+=i;
                        continue block2;
                    }
                    if (stringBuffer == null) {
                        stringBuffer = new StringBuffer();
                    }
                    if (n5 != n) {
                        stringBuffer.append(arrc, n, n5 - n);
                    }
                    stringBuffer.append(this.replace);
                    n = n5+=n2;
                    bl = true;
                    continue;
                }
                ++n5;
            }
        }
        if (bl) {
            if (n != arrc.length) {
                stringBuffer.append(arrc, n, arrc.length - n);
            }
            return stringBuffer.toString();
        }
        return null;
    }
}

