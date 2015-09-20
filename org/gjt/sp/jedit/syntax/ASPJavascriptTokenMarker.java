/*
 * Decompiled with CFR 0_102.
 */
package org.gjt.sp.jedit.syntax;

import javax.swing.text.Segment;
import org.gjt.sp.jedit.syntax.KeywordMap;
import org.gjt.sp.jedit.syntax.MultiModeToken;
import org.gjt.sp.jedit.syntax.MultiModeTokenMarkerWithContext;
import org.gjt.sp.jedit.syntax.TokenMarker;
import org.gjt.sp.jedit.syntax.TokenMarkerContext;
import org.gjt.sp.jedit.syntax.TokenMarkerWithAddToken;

public class ASPJavascriptTokenMarker
extends TokenMarker
implements TokenMarkerWithAddToken,
MultiModeTokenMarkerWithContext {
    private static KeywordMap javaScriptKeywords;
    private KeywordMap keywords;
    private boolean standalone;

    public ASPJavascriptTokenMarker() {
        this(ASPJavascriptTokenMarker.getKeywords(), true);
    }

    public ASPJavascriptTokenMarker(boolean bl) {
        this(ASPJavascriptTokenMarker.getKeywords(), bl);
    }

    public ASPJavascriptTokenMarker(KeywordMap keywordMap) {
        this(keywordMap, true);
    }

    public ASPJavascriptTokenMarker(KeywordMap keywordMap, boolean bl) {
        this.keywords = keywordMap;
        this.standalone = bl;
    }

    public void addToken(int n, byte by) {
        super.addToken(n, by);
    }

    protected byte markTokensImpl(byte by, Segment segment, int n) {
        MultiModeToken multiModeToken;
        TokenMarkerContext tokenMarkerContext = new TokenMarkerContext(segment, n, this, this.lineInfo);
        MultiModeToken multiModeToken2 = MultiModeToken.NULL;
        if (tokenMarkerContext.prevLineInfo != null && tokenMarkerContext.prevLineInfo.obj != null && tokenMarkerContext.prevLineInfo.obj instanceof MultiModeToken) {
            multiModeToken2 = (MultiModeToken)tokenMarkerContext.prevLineInfo.obj;
        }
        tokenMarkerContext.currLineInfo.obj = multiModeToken = this.markTokensImpl(multiModeToken2, tokenMarkerContext);
        return multiModeToken.token;
    }

    public MultiModeToken markTokensImpl(MultiModeToken multiModeToken, TokenMarkerContext tokenMarkerContext) {
        MultiModeToken multiModeToken2 = new MultiModeToken(multiModeToken);
        block20 : while (tokenMarkerContext.hasMoreChars()) {
            boolean bl = false;
            char c = tokenMarkerContext.getChar();
            block0 : switch (multiModeToken2.token) {
                case 0: {
                    if (!this.standalone) {
                        if (multiModeToken2.mode == 11 && tokenMarkerContext.regionMatches(true, "<%")) {
                            tokenMarkerContext.doKeywordToPos(this.keywords);
                            return multiModeToken2;
                        }
                        if (multiModeToken2.mode == 6 && tokenMarkerContext.regionMatches(true, "%>")) {
                            tokenMarkerContext.doKeywordToPos(this.keywords);
                            return multiModeToken2;
                        }
                        if ((multiModeToken2.mode == 11 || multiModeToken2.mode == 10) && tokenMarkerContext.regionMatches(true, "</script>")) {
                            tokenMarkerContext.doKeywordToPos(this.keywords);
                            return multiModeToken2;
                        }
                    }
                    switch (c) {
                        case '\"': {
                            bl = false;
                            tokenMarkerContext.doKeywordToPos(this.keywords);
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            multiModeToken2.token = 3;
                            break block0;
                        }
                        case '\'': {
                            bl = false;
                            tokenMarkerContext.doKeywordToPos(this.keywords);
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            multiModeToken2.token = 4;
                            break block0;
                        }
                        case ':': {
                            if (tokenMarkerContext.lastKeyword != tokenMarkerContext.offset) break block0;
                            ++tokenMarkerContext.pos;
                            tokenMarkerContext.addTokenToPos(5);
                            continue block20;
                        }
                        case '/': {
                            if (tokenMarkerContext.remainingChars() > 0) {
                                switch (tokenMarkerContext.getChar(1)) {
                                    case '*': {
                                        tokenMarkerContext.doKeywordToPos(this.keywords);
                                        tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                                        tokenMarkerContext.pos+=2;
                                        multiModeToken2.token = 1;
                                        continue block20;
                                    }
                                    case '/': {
                                        tokenMarkerContext.doKeywordToPos(this.keywords);
                                        tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                                        tokenMarkerContext.addTokenToEnd(1);
                                        break block20;
                                    }
                                }
                            }
                            tokenMarkerContext.doKeywordToPos(this.keywords);
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            ++tokenMarkerContext.pos;
                            tokenMarkerContext.addTokenToPos(9);
                            continue block20;
                        }
                        case '!': 
                        case '%': 
                        case '&': 
                        case '*': 
                        case '+': 
                        case '-': 
                        case '<': 
                        case '=': 
                        case '>': 
                        case '^': 
                        case '|': 
                        case '~': {
                            tokenMarkerContext.doKeywordToPos(this.keywords);
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            ++tokenMarkerContext.pos;
                            tokenMarkerContext.addTokenToPos(9);
                            continue block20;
                        }
                    }
                    if (Character.isLetterOrDigit(c) || c == '_' || c == '$') break;
                    tokenMarkerContext.doKeywordToPos(this.keywords);
                    break;
                }
                case 1: 
                case 2: {
                    if (!tokenMarkerContext.regionMatches(true, "*/")) break;
                    tokenMarkerContext.pos+=2;
                    tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                    multiModeToken2.token = 0;
                    continue block20;
                }
                case 3: {
                    if (bl) {
                        bl = false;
                        break;
                    }
                    if (c == '\\') {
                        bl = true;
                        break;
                    }
                    if (c != '\"') break;
                    ++tokenMarkerContext.pos;
                    tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                    multiModeToken2.token = 0;
                    continue block20;
                }
                case 4: {
                    if (bl) {
                        bl = false;
                        break;
                    }
                    if (c == '\\') {
                        bl = true;
                        break;
                    }
                    if (c != '\'') break;
                    ++tokenMarkerContext.pos;
                    tokenMarkerContext.addTokenToPos(tokenMarkerContext.pos, multiModeToken2.token);
                    multiModeToken2.token = 0;
                    continue block20;
                }
                default: {
                    throw new InternalError("Invalid state: " + multiModeToken2.token);
                }
            }
            ++tokenMarkerContext.pos;
        }
        if (multiModeToken2.token == 0) {
            tokenMarkerContext.doKeywordToEnd(this.keywords);
        }
        switch (multiModeToken2.token) {
            case 3: 
            case 4: {
                tokenMarkerContext.addTokenToEnd(10);
                multiModeToken2.token = 0;
                break;
            }
            default: {
                tokenMarkerContext.addTokenToEnd(multiModeToken2.token);
            }
        }
        return multiModeToken2;
    }

    public static KeywordMap getKeywords() {
        if (javaScriptKeywords == null) {
            javaScriptKeywords = new KeywordMap(false);
            javaScriptKeywords.add("break", 6);
            javaScriptKeywords.add("continue", 6);
            javaScriptKeywords.add("delete", 6);
            javaScriptKeywords.add("else", 6);
            javaScriptKeywords.add("for", 6);
            javaScriptKeywords.add("function", 6);
            javaScriptKeywords.add("if", 6);
            javaScriptKeywords.add("in", 6);
            javaScriptKeywords.add("new", 6);
            javaScriptKeywords.add("return", 6);
            javaScriptKeywords.add("this", 4);
            javaScriptKeywords.add("typeof", 6);
            javaScriptKeywords.add("var", 6);
            javaScriptKeywords.add("void", 8);
            javaScriptKeywords.add("while", 6);
            javaScriptKeywords.add("with", 6);
            javaScriptKeywords.add("abstract", 6);
            javaScriptKeywords.add("boolean", 8);
            javaScriptKeywords.add("byte", 8);
            javaScriptKeywords.add("case", 6);
            javaScriptKeywords.add("catch", 6);
            javaScriptKeywords.add("char", 8);
            javaScriptKeywords.add("class", 6);
            javaScriptKeywords.add("const", 6);
            javaScriptKeywords.add("debugger", 6);
            javaScriptKeywords.add("default", 6);
            javaScriptKeywords.add("do", 6);
            javaScriptKeywords.add("double", 8);
            javaScriptKeywords.add("enum", 6);
            javaScriptKeywords.add("export", 7);
            javaScriptKeywords.add("extends", 6);
            javaScriptKeywords.add("final", 6);
            javaScriptKeywords.add("finally", 6);
            javaScriptKeywords.add("float", 8);
            javaScriptKeywords.add("goto", 6);
            javaScriptKeywords.add("implements", 6);
            javaScriptKeywords.add("import", 7);
            javaScriptKeywords.add("instanceof", 6);
            javaScriptKeywords.add("int", 8);
            javaScriptKeywords.add("interface", 6);
            javaScriptKeywords.add("long", 8);
            javaScriptKeywords.add("native", 6);
            javaScriptKeywords.add("package", 7);
            javaScriptKeywords.add("private", 6);
            javaScriptKeywords.add("protected", 6);
            javaScriptKeywords.add("public", 6);
            javaScriptKeywords.add("short", 8);
            javaScriptKeywords.add("static", 6);
            javaScriptKeywords.add("super", 4);
            javaScriptKeywords.add("switch", 6);
            javaScriptKeywords.add("synchronized", 6);
            javaScriptKeywords.add("throw", 6);
            javaScriptKeywords.add("throws", 6);
            javaScriptKeywords.add("transient", 6);
            javaScriptKeywords.add("try", 6);
            javaScriptKeywords.add("volatile", 6);
            javaScriptKeywords.add("Array", 8);
            javaScriptKeywords.add("Boolean", 8);
            javaScriptKeywords.add("Date", 8);
            javaScriptKeywords.add("Function", 8);
            javaScriptKeywords.add("Global", 8);
            javaScriptKeywords.add("Math", 8);
            javaScriptKeywords.add("Number", 8);
            javaScriptKeywords.add("Object", 8);
            javaScriptKeywords.add("RegExp", 8);
            javaScriptKeywords.add("String", 8);
            javaScriptKeywords.add("false", 4);
            javaScriptKeywords.add("null", 4);
            javaScriptKeywords.add("true", 4);
            javaScriptKeywords.add("NaN", 4);
            javaScriptKeywords.add("Infinity", 4);
            javaScriptKeywords.add("eval", 4);
            javaScriptKeywords.add("parseInt", 4);
            javaScriptKeywords.add("parseFloat", 4);
            javaScriptKeywords.add("escape", 4);
            javaScriptKeywords.add("unescape", 4);
            javaScriptKeywords.add("isNaN", 4);
            javaScriptKeywords.add("isFinite", 4);
            javaScriptKeywords.add("adOpenForwardOnly", 4);
            javaScriptKeywords.add("adOpenKeyset", 4);
            javaScriptKeywords.add("adOpenDynamic", 4);
            javaScriptKeywords.add("adOpenStatic", 4);
            javaScriptKeywords.add("adHoldRecords", 4);
            javaScriptKeywords.add("adMovePrevious", 4);
            javaScriptKeywords.add("adAddNew", 4);
            javaScriptKeywords.add("adDelete", 4);
            javaScriptKeywords.add("adUpdate", 4);
            javaScriptKeywords.add("adBookmark", 4);
            javaScriptKeywords.add("adApproxPosition", 4);
            javaScriptKeywords.add("adUpdateBatch", 4);
            javaScriptKeywords.add("adResync", 4);
            javaScriptKeywords.add("adNotify", 4);
            javaScriptKeywords.add("adFind", 4);
            javaScriptKeywords.add("adSeek", 4);
            javaScriptKeywords.add("adIndex", 4);
            javaScriptKeywords.add("adLockReadOnly", 4);
            javaScriptKeywords.add("adLockPessimistic", 4);
            javaScriptKeywords.add("adLockOptimistic", 4);
            javaScriptKeywords.add("adLockBatchOptimistic", 4);
            javaScriptKeywords.add("adRunAsync", 4);
            javaScriptKeywords.add("adAsyncExecute", 4);
            javaScriptKeywords.add("adAsyncFetch", 4);
            javaScriptKeywords.add("adAsyncFetchNonBlocking", 4);
            javaScriptKeywords.add("adExecuteNoRecords", 4);
            javaScriptKeywords.add("adAsyncConnect", 4);
            javaScriptKeywords.add("adStateClosed", 4);
            javaScriptKeywords.add("adStateOpen", 4);
            javaScriptKeywords.add("adStateConnecting", 4);
            javaScriptKeywords.add("adStateExecuting", 4);
            javaScriptKeywords.add("adStateFetching", 4);
            javaScriptKeywords.add("adUseServer", 4);
            javaScriptKeywords.add("adUseClient", 4);
            javaScriptKeywords.add("adEmpty", 4);
            javaScriptKeywords.add("adTinyInt", 4);
            javaScriptKeywords.add("adSmallInt", 4);
            javaScriptKeywords.add("adInteger", 4);
            javaScriptKeywords.add("adBigInt", 4);
            javaScriptKeywords.add("adUnsignedTinyInt", 4);
            javaScriptKeywords.add("adUnsignedSmallInt", 4);
            javaScriptKeywords.add("adUnsignedInt", 4);
            javaScriptKeywords.add("adUnsignedBigInt", 4);
            javaScriptKeywords.add("adSingle", 4);
            javaScriptKeywords.add("adDouble", 4);
            javaScriptKeywords.add("adCurrency", 4);
            javaScriptKeywords.add("adDecimal", 4);
            javaScriptKeywords.add("adNumeric", 4);
            javaScriptKeywords.add("adBoolean", 4);
            javaScriptKeywords.add("adError", 4);
            javaScriptKeywords.add("adUserDefined", 4);
            javaScriptKeywords.add("adVariant", 4);
            javaScriptKeywords.add("adIDispatch", 4);
            javaScriptKeywords.add("adIUnknown", 4);
            javaScriptKeywords.add("adGUID", 4);
            javaScriptKeywords.add("adDate", 4);
            javaScriptKeywords.add("adDBDate", 4);
            javaScriptKeywords.add("adDBTime", 4);
            javaScriptKeywords.add("adDBTimeStamp", 4);
            javaScriptKeywords.add("adBSTR", 4);
            javaScriptKeywords.add("adChar", 4);
            javaScriptKeywords.add("adVarChar", 4);
            javaScriptKeywords.add("adLongVarChar", 4);
            javaScriptKeywords.add("adWChar", 4);
            javaScriptKeywords.add("adVarWChar", 4);
            javaScriptKeywords.add("adLongVarWChar", 4);
            javaScriptKeywords.add("adBinary", 4);
            javaScriptKeywords.add("adVarBinary", 4);
            javaScriptKeywords.add("adLongVarBinary", 4);
            javaScriptKeywords.add("adChapter", 4);
            javaScriptKeywords.add("adFileTime", 4);
            javaScriptKeywords.add("adDBFileTime", 4);
            javaScriptKeywords.add("adPropVariant", 4);
            javaScriptKeywords.add("adVarNumeric", 4);
        }
        return javaScriptKeywords;
    }
}

