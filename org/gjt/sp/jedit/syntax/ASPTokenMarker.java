/*
 * Decompiled with CFR 0_102.
 * 
 * Could not load the following classes:
 *  gnu.regexp.RE
 *  gnu.regexp.REException
 *  gnu.regexp.REMatch
 */
package org.gjt.sp.jedit.syntax;

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.REMatch;
import java.util.Stack;
import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.ASPJavascriptTokenMarker;
import org.gjt.sp.jedit.syntax.ASPPerlscriptTokenMarker;
import org.gjt.sp.jedit.syntax.ASPStateInfo;
import org.gjt.sp.jedit.syntax.ASPVBScriptTokenMarker;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.MultiModeToken;
import org.gjt.sp.jedit.syntax.MultiModeTokenMarkerWithContext;
import org.gjt.sp.jedit.syntax.TokenMarker;
import org.gjt.sp.jedit.syntax.TokenMarkerContext;
import org.gjt.sp.jedit.syntax.TokenMarkerDebugger;
import org.gjt.sp.jedit.syntax.TokenMarkerWithAddToken;

public class ASPTokenMarker
extends TokenMarker
implements TokenMarkerWithAddToken {
    public static final byte MODE_CHANGE = 100;
    private KeywordMap keywords;
    private ASPStateInfo stateInfo = new ASPStateInfo();
    private byte defaultASPMode = 8;
    private TokenMarkerDebugger debug = new TokenMarkerDebugger();
    private MultiModeTokenMarkerWithContext vbs = new ASPVBScriptTokenMarker(false);
    private MultiModeTokenMarkerWithContext js = new ASPJavascriptTokenMarker(false);
    private MultiModeTokenMarkerWithContext ps = new ASPPerlscriptTokenMarker(false);
    private Stack modes = new Stack();
    private static RE language = null;
    private static RE runat = null;

    public void addToken(int n, byte by) {
        super.addToken(n, by);
    }

    protected byte markTokensImpl(byte by, Segment segment, int n) {
        TokenMarkerContext tokenMarkerContext = new TokenMarkerContext(segment, n, this, this.lineInfo);
        byte by2 = this.defaultASPMode;
        MultiModeToken multiModeToken = MultiModeToken.NULL;
        if (tokenMarkerContext.prevLineInfo != null && tokenMarkerContext.prevLineInfo.obj != null && tokenMarkerContext.prevLineInfo.obj instanceof MultiModeToken) {
            multiModeToken = (MultiModeToken)tokenMarkerContext.prevLineInfo.obj;
        }
        MultiModeToken multiModeToken2 = MultiModeToken.NULL;
        if (tokenMarkerContext.currLineInfo != null && tokenMarkerContext.currLineInfo.obj != null && tokenMarkerContext.currLineInfo.obj instanceof MultiModeToken) {
            multiModeToken2 = (MultiModeToken)tokenMarkerContext.currLineInfo.obj;
        }
        MultiModeToken multiModeToken3 = this.markTokensImpl(multiModeToken, tokenMarkerContext);
        byte by3 = multiModeToken3.token;
        if ((by2 != this.defaultASPMode || multiModeToken2.mode != multiModeToken3.mode) && multiModeToken2.token == multiModeToken3.token) {
            by3 = 100;
        }
        tokenMarkerContext.currLineInfo.obj = multiModeToken3;
        return by3;
    }

    MultiModeToken markTokensImpl(MultiModeToken multiModeToken, TokenMarkerContext tokenMarkerContext) {
        MultiModeToken multiModeToken2 = new MultiModeToken(multiModeToken);
        this.debug.reset();
        block16 : while (tokenMarkerContext.hasMoreChars()) {
            char c = tokenMarkerContext.getChar();
            if (!this.debug.isOK(tokenMarkerContext)) {
                ++tokenMarkerContext.pos;
            }
            if (multiModeToken2.mode != 4 && multiModeToken2.mode != 5 && multiModeToken2.mode != 6 && multiModeToken2.mode != 7 && multiModeToken2.mode != 8 && multiModeToken2.mode != 10 && multiModeToken2.mode != 12 && this.doASP(multiModeToken2, tokenMarkerContext)) continue;
            if (multiModeToken2.mode != 4 && multiModeToken2.mode != 5 && multiModeToken2.mode != 6 && multiModeToken2.mode != 7 && multiModeToken2.mode != 8 && multiModeToken2.mode != 10 && multiModeToken2.mode != 12 && multiModeToken2.mode != 9 && multiModeToken2.mode != 11 && multiModeToken2.mode != 13 && this.doScript(multiModeToken2, tokenMarkerContext)) continue;
            switch (multiModeToken2.mode) {
                REMatch rEMatch;
                case 0: {
                    switch (c) {
                        case '<': {
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            if (tokenMarkerContext.regionMatches(false, "<!--")) {
                                if (tokenMarkerContext.regionMatches(false, "<!--#")) {
                                    tokenMarkerContext.pos+=5;
                                    multiModeToken2.mode = 5;
                                    multiModeToken2.token = 2;
                                    continue block16;
                                }
                                tokenMarkerContext.pos+=4;
                                multiModeToken2.mode = 1;
                                multiModeToken2.token = 1;
                                continue block16;
                            }
                            if (tokenMarkerContext.regionMatches(true, "</")) {
                                tokenMarkerContext.pos+=2;
                                tokenMarkerContext.addTokenToPos(6);
                                multiModeToken2.mode = 3;
                                multiModeToken2.token = 6;
                                continue block16;
                            }
                            ++tokenMarkerContext.pos;
                            tokenMarkerContext.addTokenToPos(6);
                            multiModeToken2.mode = 3;
                            multiModeToken2.token = 6;
                            continue block16;
                        }
                        case '&': {
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            multiModeToken2.mode = 2;
                            multiModeToken2.token = 7;
                        }
                    }
                    break;
                }
                case 3: {
                    if (c != '>') break;
                    tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                    ++tokenMarkerContext.pos;
                    tokenMarkerContext.addTokenToPos(6);
                    multiModeToken2.mode = 0;
                    multiModeToken2.token = 0;
                    continue block16;
                }
                case 7: {
                    if (tokenMarkerContext.regionMatches(true, "%>")) {
                        tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                        tokenMarkerContext.pos+=2;
                        tokenMarkerContext.addTokenToPos(5);
                        this.defaultASPMode = this.stateInfo.toASPMode();
                        multiModeToken2.reset();
                        continue block16;
                    }
                    if (!tokenMarkerContext.regionMatches(true, "language")) break;
                    if (language != null && (rEMatch = tokenMarkerContext.RERegionMatches(language)) != null) {
                        this.stateInfo.language = rEMatch.toString(1).toLowerCase();
                        break;
                    }
                    this.stateInfo.language = "html";
                    break;
                }
                case 4: {
                    if (c == '>') {
                        ++tokenMarkerContext.pos;
                        tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                        if (this.stateInfo.client && !this.modes.empty()) {
                            MultiModeToken multiModeToken3 = (MultiModeToken)this.modes.peek();
                            if (multiModeToken3.mode == 1) {
                                multiModeToken2 = (MultiModeToken)this.modes.pop();
                                continue block16;
                            }
                        }
                        multiModeToken2.mode = this.stateInfo.toASPMode();
                        multiModeToken2.token = 0;
                        continue block16;
                    }
                    if (tokenMarkerContext.regionMatches(true, "language")) {
                        if (language != null && (rEMatch = tokenMarkerContext.RERegionMatches(language)) != null) {
                            this.stateInfo.language = rEMatch.toString(1).toLowerCase();
                            break;
                        }
                        this.stateInfo.language = "html";
                        break;
                    }
                    if (runat == null || (rEMatch = tokenMarkerContext.RERegionMatches(runat)) == null) break;
                    this.stateInfo.client = false;
                    break;
                }
                case 2: {
                    if (c != ';') break;
                    ++tokenMarkerContext.pos;
                    tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                    multiModeToken2.reset();
                    continue block16;
                }
                case 1: 
                case 5: {
                    if (!tokenMarkerContext.regionMatches(false, "-->")) break;
                    tokenMarkerContext.pos+=3;
                    tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                    multiModeToken2.reset();
                    continue block16;
                }
                case 6: {
                    multiModeToken2 = this.defaultASPMode == 10 ? this.js.markTokensImpl(multiModeToken2, tokenMarkerContext) : (this.defaultASPMode == 12 ? this.ps.markTokensImpl(multiModeToken2, tokenMarkerContext) : this.vbs.markTokensImpl(multiModeToken2, tokenMarkerContext));
                    if (!tokenMarkerContext.regionMatches(true, "%>")) break;
                    tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                    tokenMarkerContext.pos+=2;
                    tokenMarkerContext.addTokenToPos(5);
                    if (this.modes.empty()) {
                        multiModeToken2.reset();
                        continue block16;
                    }
                    multiModeToken2 = (MultiModeToken)this.modes.pop();
                    continue block16;
                }
                case 10: 
                case 11: {
                    multiModeToken2 = this.js.markTokensImpl(multiModeToken2, tokenMarkerContext);
                    if (multiModeToken2.mode == 11 && this.doASP(multiModeToken2, tokenMarkerContext)) continue block16;
                    if (!this.doScriptClose(multiModeToken2, tokenMarkerContext)) break;
                    continue block16;
                }
                case 8: 
                case 9: {
                    multiModeToken2 = this.vbs.markTokensImpl(multiModeToken2, tokenMarkerContext);
                    if (multiModeToken2.mode == 9 && this.doASP(multiModeToken2, tokenMarkerContext)) continue block16;
                    if (!this.doScriptClose(multiModeToken2, tokenMarkerContext)) break;
                    continue block16;
                }
                case 12: 
                case 13: {
                    multiModeToken2 = this.ps.markTokensImpl(multiModeToken2, tokenMarkerContext);
                    if (multiModeToken2.mode == 13 && this.doASP(multiModeToken2, tokenMarkerContext)) continue block16;
                    if (!this.doScriptClose(multiModeToken2, tokenMarkerContext)) break;
                    continue block16;
                }
            }
            ++tokenMarkerContext.pos;
        }
        if (multiModeToken2.mode == 2) {
            tokenMarkerContext.addTokenToEnd(10);
            multiModeToken2.mode = 0;
            multiModeToken2.token = 0;
        } else {
            tokenMarkerContext.addTokenToEnd(multiModeToken2.token);
        }
        return multiModeToken2;
    }

    private boolean doASP(MultiModeToken multiModeToken, TokenMarkerContext tokenMarkerContext) {
        if (tokenMarkerContext.regionMatches(false, "<%")) {
            tokenMarkerContext.addTokenToPos(multiModeToken.token);
            if (tokenMarkerContext.regionMatches(false, "<%@")) {
                this.stateInfo.init(false, "vbscript");
                tokenMarkerContext.pos+=3;
                tokenMarkerContext.addTokenToPos(5);
                this.modes.push(new MultiModeToken(multiModeToken));
                multiModeToken.mode = 7;
                multiModeToken.token = 7;
            } else {
                tokenMarkerContext.pos+=2;
                tokenMarkerContext.addTokenToPos(5);
                this.modes.push(new MultiModeToken(multiModeToken));
                multiModeToken.mode = 6;
                multiModeToken.token = 0;
            }
            return true;
        }
        return false;
    }

    private boolean doScript(MultiModeToken multiModeToken, TokenMarkerContext tokenMarkerContext) {
        if (tokenMarkerContext.regionMatches(true, "<script")) {
            this.stateInfo.init(true, "javascript");
            tokenMarkerContext.addTokenToPos(multiModeToken.token);
            tokenMarkerContext.pos+=7;
            this.modes.push(new MultiModeToken(multiModeToken));
            if (multiModeToken.mode == 1) {
                multiModeToken.mode = 4;
            } else {
                multiModeToken.mode = 4;
                multiModeToken.token = 6;
            }
            return true;
        }
        return false;
    }

    private boolean doScriptClose(MultiModeToken multiModeToken, TokenMarkerContext tokenMarkerContext) {
        if (tokenMarkerContext.regionMatches(true, "</script>")) {
            tokenMarkerContext.addTokenToPos(multiModeToken.token);
            byte by = 6;
            if (this.modes.empty()) {
                multiModeToken.reset();
            } else {
                multiModeToken.assign((MultiModeToken)this.modes.pop());
                if (multiModeToken.mode == 1) {
                    by = multiModeToken.token;
                }
            }
            tokenMarkerContext.pos+=9;
            tokenMarkerContext.addTokenToPos(by);
            return true;
        }
        return false;
    }

    static {
        try {
            language = new RE((Object)"^language\\s*=\\s*[\"']?(jscript|javascript|perlscript|vbscript)([0-9]*|[0-9]+(?:\\.[0-9]+){0,2})[\"']?", 2);
        }
        catch (REException var0) {
            // empty catch block
        }
        try {
            runat = new RE((Object)"^runat\\s*=\\s*[\"']?(server)[\"']?", 2);
        }
        catch (REException var0_1) {
            // empty catch block
        }
    }
}

