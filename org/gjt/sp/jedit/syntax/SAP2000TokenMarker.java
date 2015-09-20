/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.TokenMarker;

public class SAP2000TokenMarker
extends TokenMarker {
    private static KeywordMap sapKeywords;
    private KeywordMap keywords = SAP2000TokenMarker.getKeywords();
    private int lastOffset;
    private int lastKeyword;

    public byte markTokensImpl(byte by, Segment segment, int n) {
        int n2;
        char[] arrc = segment.array;
        this.lastOffset = n2 = segment.offset;
        this.lastKeyword = n2;
        int n3 = segment.count + n2;
        block9 : for (int i = n2; i < n3; ++i) {
            int n4 = i + 1;
            char c = arrc[i];
            switch (by) {
                case 0: {
                    switch (c) {
                        case ';': {
                            this.addToken(i - this.lastOffset, by);
                            this.addToken(n3 - i, 1);
                            by = 0;
                            this.lastOffset = this.lastKeyword = n3;
                            break block9;
                        }
                        case ' ': {
                            this.doKeyword(segment, i, c);
                            continue block9;
                        }
                    }
                    if (Character.isLetterOrDigit(c)) continue block9;
                    this.doKeyword(segment, i, c);
                    continue block9;
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
            default: 
        }
        this.addToken(n3 - this.lastOffset, by);
        return by;
    }

    public static KeywordMap getKeywords() {
        if (sapKeywords == null) {
            sapKeywords = new KeywordMap(true);
            sapKeywords.add("SYSTEM", 5);
            sapKeywords.add("COORDINATE", 5);
            sapKeywords.add("JOINT", 5);
            sapKeywords.add("JOINTS", 5);
            sapKeywords.add("LOCAL", 5);
            sapKeywords.add("RESTRAINT", 5);
            sapKeywords.add("RESTRAINTS", 5);
            sapKeywords.add("CONSTRAINT", 5);
            sapKeywords.add("CONSTRAINTS", 5);
            sapKeywords.add("WELD", 5);
            sapKeywords.add("PATTERN", 5);
            sapKeywords.add("SPRING", 5);
            sapKeywords.add("MASS", 5);
            sapKeywords.add("MASSES", 5);
            sapKeywords.add("MATERIAL", 5);
            sapKeywords.add("FRAME", 5);
            sapKeywords.add("FRAMES", 5);
            sapKeywords.add("SHELL", 5);
            sapKeywords.add("SECTION", 5);
            sapKeywords.add("SECTIONS", 5);
            sapKeywords.add("NLPROP", 5);
            sapKeywords.add("FRAME", 5);
            sapKeywords.add("SHELL", 5);
            sapKeywords.add("PLANE", 5);
            sapKeywords.add("ASOLID", 5);
            sapKeywords.add("SOLID", 5);
            sapKeywords.add("NLLINK", 5);
            sapKeywords.add("MATTEMP", 5);
            sapKeywords.add("REFTEMP", 5);
            sapKeywords.add("PRESTRESS", 5);
            sapKeywords.add("LOAD", 5);
            sapKeywords.add("LOADS", 5);
            sapKeywords.add("PDFORCE", 5);
            sapKeywords.add("PDELTA", 5);
            sapKeywords.add("MODES", 5);
            sapKeywords.add("FUNCTION", 5);
            sapKeywords.add("SPEC", 5);
            sapKeywords.add("HISTORY", 5);
            sapKeywords.add("LANE", 5);
            sapKeywords.add("VEHICLE", 5);
            sapKeywords.add("VEHICLE", 5);
            sapKeywords.add("CLASS", 5);
            sapKeywords.add("RESPONSE", 5);
            sapKeywords.add("BRIDGE", 5);
            sapKeywords.add("MOVING", 5);
            sapKeywords.add("COMBO", 5);
            sapKeywords.add("OUTPUT", 5);
            sapKeywords.add("END", 5);
            sapKeywords.add("NAME", 6);
            sapKeywords.add("TYPE", 6);
            sapKeywords.add("IDES", 6);
            sapKeywords.add("MAT", 6);
            sapKeywords.add("MATANG", 6);
            sapKeywords.add("TH", 6);
            sapKeywords.add("GEN", 6);
            sapKeywords.add("LGEN", 6);
            sapKeywords.add("FGEN", 6);
            sapKeywords.add("EGEN", 6);
            sapKeywords.add("CGEN", 6);
            sapKeywords.add("DEL", 6);
            sapKeywords.add("ADD", 6);
            sapKeywords.add("REM", 6);
            sapKeywords.add("ELEM", 6);
            sapKeywords.add("FACE", 6);
            sapKeywords.add("CSYS", 6);
            sapKeywords.add("AXDIR", 6);
            sapKeywords.add("PLDIR", 6);
            sapKeywords.add("LOCAL", 6);
            sapKeywords.add("SW", 6);
            sapKeywords.add("DOF", 7);
            sapKeywords.add("LENGTH", 7);
            sapKeywords.add("FORCE", 7);
            sapKeywords.add("UP", 7);
            sapKeywords.add("CYC", 7);
            sapKeywords.add("WARN", 7);
            sapKeywords.add("PAGE", 7);
            sapKeywords.add("LINES", 7);
            sapKeywords.add("LMAP", 7);
            sapKeywords.add("FMAP", 7);
            sapKeywords.add("NLP", 7);
            sapKeywords.add("AXVEC", 7);
            sapKeywords.add("PLVEC", 7);
            sapKeywords.add("ANG", 7);
            sapKeywords.add("ZERO", 7);
            sapKeywords.add("UX", 7);
            sapKeywords.add("UY", 7);
            sapKeywords.add("UZ", 7);
            sapKeywords.add("RX", 7);
            sapKeywords.add("RY\t", 7);
            sapKeywords.add("RZ", 7);
            sapKeywords.add("U1", 7);
            sapKeywords.add("U2", 7);
            sapKeywords.add("U3", 7);
            sapKeywords.add("R1", 7);
            sapKeywords.add("R2", 7);
            sapKeywords.add("R3", 7);
            sapKeywords.add("RD", 7);
            sapKeywords.add("PAT", 7);
            sapKeywords.add("FORCE", 8);
            sapKeywords.add("RESTRAINT", 8);
            sapKeywords.add("SPRING", 8);
            sapKeywords.add("DISPLACEMENT", 8);
            sapKeywords.add("GRAVITY", 8);
            sapKeywords.add("CONCENTRATED", 8);
            sapKeywords.add("SPAN", 8);
            sapKeywords.add("DISTRIBUTED", 8);
            sapKeywords.add("PRESTRESS", 8);
            sapKeywords.add("UNIFORM", 8);
            sapKeywords.add("SURVACE", 8);
            sapKeywords.add("PORE", 8);
            sapKeywords.add("PRESSURE", 8);
            sapKeywords.add("TEMPERATURE", 8);
            sapKeywords.add("ROTATE", 8);
        }
        return sapKeywords;
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

