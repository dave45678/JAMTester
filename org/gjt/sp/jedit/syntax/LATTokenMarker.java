/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class LATTokenMarker
extends TokenMarker {
    private static KeywordMap latKeywords;
    private KeywordMap keywords = LATTokenMarker.getKeywords();
    private int lastOffset;
    private int lastKeyword;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        int n3 = segment.count + n2;
        block16 : for (int i = n2; i < n3; ++i) {
            int n4 = i + 1;
            char c = arrc[i];
            switch (by) {
                case 0: {
                    switch (c) {
                        case '{': {
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)2;
                            this.lastOffset = this.lastKeyword = i;
                            continue block16;
                        }
                        case '/': {
                            this.doKeyword(segment, i, c);
                            if (n3 - i <= 1 || arrc[n4] != '/') continue block16;
                            this.addToken(i - this.lastOffset, by);
                            this.addToken(n3 - i, 1);
                            this.lastOffset = this.lastKeyword = n3;
                            break block16;
                        }
                        case '\"': {
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)3;
                            this.lastOffset = this.lastKeyword = i;
                            continue block16;
                        }
                        case '\'': {
                            this.doKeyword(segment, i, c);
                            this.addToken(i - this.lastOffset, by);
                            by = (byte)4;
                            this.lastOffset = this.lastKeyword = i;
                            continue block16;
                        }
                        case '#': {
                            this.addToken(i - this.lastOffset, by);
                            this.addToken(1, 7);
                            this.lastOffset = this.lastKeyword = n4;
                            continue block16;
                        }
                    }
                    if (Character.isLetterOrDigit(c) || c == '_') continue block16;
                    this.doKeyword(segment, i, c);
                    continue block16;
                }
                case 2: {
                    if (c != '}') continue block16;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block16;
                }
                case 3: {
                    if (c != '\"') continue block16;
                    this.addToken(n4 - this.lastOffset, by);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block16;
                }
                case 4: {
                    if (c != '\'') continue block16;
                    this.addToken(n4 - this.lastOffset, 3);
                    by = 0;
                    this.lastOffset = this.lastKeyword = n4;
                    continue block16;
                }
                default: {
                    throw new InternalError("Invalid state: " + by);
                }
            }
        }
        if (by == 0) {
            this.doKeyword(segment, n3, '\u0000');
        }
        switch (by) {
            case 3: {
                this.addToken(n3 - this.lastOffset, 10);
                by = 0;
                break;
            }
            default: {
                this.addToken(n3 - this.lastOffset, by);
            }
        }
        return by;
    }

    public static KeywordMap getKeywords() {
        if (latKeywords == null) {
            latKeywords = new KeywordMap(true);
            latKeywords.add("specification", 9);
            latKeywords.add("realisation", 9);
            latKeywords.add("constantes", 9);
            latKeywords.add("types", 9);
            latKeywords.add("<-", 7);
            latKeywords.add("alors", 6);
            latKeywords.add("autrement", 6);
            latKeywords.add("booleen", 8);
            latKeywords.add("boucle", 6);
            latKeywords.add("caractere", 8);
            latKeywords.add("cas", 6);
            latKeywords.add("chaine", 8);
            latKeywords.add("dans", 6);
            latKeywords.add("de", 6);
            latKeywords.add("div", 7);
            latKeywords.add("ecrire", 6);
            latKeywords.add("ecrire_ligne", 6);
            latKeywords.add("enregistrement", 8);
            latKeywords.add("ensemble", 8);
            latKeywords.add("entier", 8);
            latKeywords.add("et", 7);
            latKeywords.add("etpuis", 7);
            latKeywords.add("faire", 6);
            latKeywords.add("fait", 6);
            latKeywords.add("faux", 4);
            latKeywords.add("fin", 6);
            latKeywords.add("finselon", 6);
            latKeywords.add("finsi", 6);
            latKeywords.add("jusqua", 6);
            latKeywords.add("lire", 6);
            latKeywords.add("lire_ligne", 6);
            latKeywords.add("liste", 8);
            latKeywords.add("mod", 7);
            latKeywords.add("non", 7);
            latKeywords.add("ou", 7);
            latKeywords.add("oubien", 7);
            latKeywords.add("pour", 6);
            latKeywords.add("reel", 8);
            latKeywords.add("repeter", 6);
            latKeywords.add("retour", 6);
            latKeywords.add("retourne", 6);
            latKeywords.add("selon", 6);
            latKeywords.add("si", 6);
            latKeywords.add("sinon", 6);
            latKeywords.add("sortirsi", 6);
            latKeywords.add("tableau", 8);
            latKeywords.add("tantque", 6);
            latKeywords.add("vrai", 4);
        }
        return latKeywords;
    }

    private boolean doKeyword(Segment segment, int n, char c) {
        int n2 = n + 1;
        int n3 = n - this.lastKeyword;
        byte by = this.keywords.lookup(segment, this.lastKeyword, n3);
        if (by != 0) {
            if (this.lastKeyword != this.lastOffset) {
                this.addToken(this.lastKeyword - this.lastOffset, 0);
            }
            this.addToken(n3, by);
            this.lastOffset = n;
        }
        this.lastKeyword = n2;
        return false;
    }
}

