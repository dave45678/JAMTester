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

public class ASPVBScriptTokenMarker
extends TokenMarker
implements TokenMarkerWithAddToken,
MultiModeTokenMarkerWithContext {
    private KeywordMap keywords = ASPVBScriptTokenMarker.getKeywords();
    private boolean standalone;
    private static KeywordMap vbScriptKeywords;

    public ASPVBScriptTokenMarker() {
        this.standalone = true;
    }

    ASPVBScriptTokenMarker(boolean bl) {
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

    /*
     * Enabled aggressive block sorting
     */
    public MultiModeToken markTokensImpl(MultiModeToken multiModeToken, TokenMarkerContext tokenMarkerContext) {
        MultiModeToken multiModeToken2 = new MultiModeToken(multiModeToken);
        block14 : while (tokenMarkerContext.hasMoreChars()) {
            char c = tokenMarkerContext.getChar();
            block0 : switch (multiModeToken2.token) {
                case 0: {
                    if (!this.standalone) {
                        if (multiModeToken2.mode == 9 && tokenMarkerContext.regionMatches(true, "<%")) {
                            this.doKeywordToPos(multiModeToken2, tokenMarkerContext);
                            return multiModeToken2;
                        }
                        if (multiModeToken2.mode == 6 && tokenMarkerContext.regionMatches(true, "%>")) {
                            this.doKeywordToPos(multiModeToken2, tokenMarkerContext);
                            return multiModeToken2;
                        }
                        if ((multiModeToken2.mode == 9 || multiModeToken2.mode == 8) && tokenMarkerContext.regionMatches(true, "</script>")) {
                            this.doKeywordToPos(multiModeToken2, tokenMarkerContext);
                            return multiModeToken2;
                        }
                    }
                    switch (c) {
                        case '\'': {
                            this.doKeywordToPos(multiModeToken2, tokenMarkerContext);
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            tokenMarkerContext.addTokenToEnd(1);
                            break block14;
                        }
                        case '\"': {
                            this.doKeywordToPos(multiModeToken2, tokenMarkerContext);
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            multiModeToken2.token = 3;
                            break block0;
                        }
                        case '&': 
                        case '*': 
                        case '+': 
                        case '-': 
                        case '/': 
                        case '<': 
                        case '=': 
                        case '>': 
                        case '\\': 
                        case '^': {
                            this.doKeywordToPos(multiModeToken2, tokenMarkerContext);
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            ++tokenMarkerContext.pos;
                            tokenMarkerContext.addTokenToPos(9);
                            continue block14;
                        }
                    }
                    if (Character.isLetterOrDigit(c) || c == '_') break;
                    this.doKeywordToPos(multiModeToken2, tokenMarkerContext);
                    break;
                }
                case 3: {
                    if (c != '\"') break;
                    ++tokenMarkerContext.pos;
                    tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                    multiModeToken2.token = 0;
                    continue block14;
                }
                case 1: {
                    tokenMarkerContext.addTokenToEnd(1);
                    break block14;
                }
            }
            ++tokenMarkerContext.pos;
        }
        if (multiModeToken2.token == 0) {
            tokenMarkerContext.doKeywordToEnd(this.keywords);
        }
        switch (multiModeToken2.token) {
            case 3: {
                tokenMarkerContext.addTokenToEnd(10);
                multiModeToken2.token = 0;
                return multiModeToken2;
            }
            case 1: {
                tokenMarkerContext.addTokenToEnd(1);
                multiModeToken2.token = 0;
                return multiModeToken2;
            }
        }
        tokenMarkerContext.addTokenToEnd(multiModeToken2.token);
        return multiModeToken2;
    }

    private byte doKeywordToPos(MultiModeToken multiModeToken, TokenMarkerContext tokenMarkerContext) {
        byte by = tokenMarkerContext.doKeywordToPos(this.keywords);
        if (by == 1) {
            multiModeToken.token = 1;
        }
        return by;
    }

    public static KeywordMap getKeywords() {
        if (vbScriptKeywords == null) {
            vbScriptKeywords = new KeywordMap(true);
            vbScriptKeywords.add("if", 6);
            vbScriptKeywords.add("then", 6);
            vbScriptKeywords.add("else", 6);
            vbScriptKeywords.add("elseif", 6);
            vbScriptKeywords.add("select", 6);
            vbScriptKeywords.add("case", 6);
            vbScriptKeywords.add("for", 6);
            vbScriptKeywords.add("to", 6);
            vbScriptKeywords.add("step", 6);
            vbScriptKeywords.add("next", 6);
            vbScriptKeywords.add("each", 6);
            vbScriptKeywords.add("in", 6);
            vbScriptKeywords.add("do", 6);
            vbScriptKeywords.add("while", 6);
            vbScriptKeywords.add("until", 6);
            vbScriptKeywords.add("loop", 6);
            vbScriptKeywords.add("wend", 6);
            vbScriptKeywords.add("exit", 6);
            vbScriptKeywords.add("end", 6);
            vbScriptKeywords.add("function", 6);
            vbScriptKeywords.add("sub", 6);
            vbScriptKeywords.add("class", 6);
            vbScriptKeywords.add("property", 6);
            vbScriptKeywords.add("get", 6);
            vbScriptKeywords.add("let", 6);
            vbScriptKeywords.add("byval", 6);
            vbScriptKeywords.add("byref", 6);
            vbScriptKeywords.add("const", 6);
            vbScriptKeywords.add("dim", 6);
            vbScriptKeywords.add("redim", 6);
            vbScriptKeywords.add("preserve", 6);
            vbScriptKeywords.add("set", 6);
            vbScriptKeywords.add("with", 6);
            vbScriptKeywords.add("new", 6);
            vbScriptKeywords.add("public", 6);
            vbScriptKeywords.add("default", 6);
            vbScriptKeywords.add("private", 6);
            vbScriptKeywords.add("rem", 1);
            vbScriptKeywords.add("call", 6);
            vbScriptKeywords.add("execute", 6);
            vbScriptKeywords.add("eval", 6);
            vbScriptKeywords.add("on", 6);
            vbScriptKeywords.add("error", 6);
            vbScriptKeywords.add("resume", 6);
            vbScriptKeywords.add("option", 6);
            vbScriptKeywords.add("explicit", 6);
            vbScriptKeywords.add("erase", 6);
            vbScriptKeywords.add("randomize", 6);
            vbScriptKeywords.add("is", 9);
            vbScriptKeywords.add("mod", 9);
            vbScriptKeywords.add("and", 9);
            vbScriptKeywords.add("or", 9);
            vbScriptKeywords.add("not", 9);
            vbScriptKeywords.add("xor", 9);
            vbScriptKeywords.add("imp", 9);
            vbScriptKeywords.add("false", 8);
            vbScriptKeywords.add("true", 8);
            vbScriptKeywords.add("empty", 8);
            vbScriptKeywords.add("nothing", 8);
            vbScriptKeywords.add("null", 8);
            vbScriptKeywords.add("vbcr", 4);
            vbScriptKeywords.add("vbcrlf", 4);
            vbScriptKeywords.add("vbformfeed", 4);
            vbScriptKeywords.add("vblf", 4);
            vbScriptKeywords.add("vbnewline", 4);
            vbScriptKeywords.add("vbnullchar", 4);
            vbScriptKeywords.add("vbnullstring", 4);
            vbScriptKeywords.add("vbtab", 4);
            vbScriptKeywords.add("vbverticaltab", 4);
            vbScriptKeywords.add("vbempty", 4);
            vbScriptKeywords.add("vbempty", 4);
            vbScriptKeywords.add("vbinteger", 4);
            vbScriptKeywords.add("vblong", 4);
            vbScriptKeywords.add("vbsingle", 4);
            vbScriptKeywords.add("vbdouble", 4);
            vbScriptKeywords.add("vbcurrency", 4);
            vbScriptKeywords.add("vbdate", 4);
            vbScriptKeywords.add("vbstring", 4);
            vbScriptKeywords.add("vbobject", 4);
            vbScriptKeywords.add("vberror", 4);
            vbScriptKeywords.add("vbboolean", 4);
            vbScriptKeywords.add("vbvariant", 4);
            vbScriptKeywords.add("vbdataobject", 4);
            vbScriptKeywords.add("vbdecimal", 4);
            vbScriptKeywords.add("vbbyte", 4);
            vbScriptKeywords.add("vbarray", 4);
            vbScriptKeywords.add("array", 7);
            vbScriptKeywords.add("lbound", 7);
            vbScriptKeywords.add("ubound", 7);
            vbScriptKeywords.add("cbool", 7);
            vbScriptKeywords.add("cbyte", 7);
            vbScriptKeywords.add("ccur", 7);
            vbScriptKeywords.add("cdate", 7);
            vbScriptKeywords.add("cdbl", 7);
            vbScriptKeywords.add("cint", 7);
            vbScriptKeywords.add("clng", 7);
            vbScriptKeywords.add("csng", 7);
            vbScriptKeywords.add("cstr", 7);
            vbScriptKeywords.add("hex", 7);
            vbScriptKeywords.add("oct", 7);
            vbScriptKeywords.add("date", 7);
            vbScriptKeywords.add("time", 7);
            vbScriptKeywords.add("dateadd", 7);
            vbScriptKeywords.add("datediff", 7);
            vbScriptKeywords.add("datepart", 7);
            vbScriptKeywords.add("dateserial", 7);
            vbScriptKeywords.add("datevalue", 7);
            vbScriptKeywords.add("day", 7);
            vbScriptKeywords.add("month", 7);
            vbScriptKeywords.add("monthname", 7);
            vbScriptKeywords.add("weekday", 7);
            vbScriptKeywords.add("weekdayname", 7);
            vbScriptKeywords.add("year", 7);
            vbScriptKeywords.add("hour", 7);
            vbScriptKeywords.add("minute", 7);
            vbScriptKeywords.add("second", 7);
            vbScriptKeywords.add("now", 7);
            vbScriptKeywords.add("timeserial", 7);
            vbScriptKeywords.add("timevalue", 7);
            vbScriptKeywords.add("formatcurrency", 7);
            vbScriptKeywords.add("formatdatetime", 7);
            vbScriptKeywords.add("formatnumber", 7);
            vbScriptKeywords.add("formatpercent", 7);
            vbScriptKeywords.add("inputbox", 7);
            vbScriptKeywords.add("loadpicture", 7);
            vbScriptKeywords.add("msgbox", 7);
            vbScriptKeywords.add("atn", 7);
            vbScriptKeywords.add("cos", 7);
            vbScriptKeywords.add("sin", 7);
            vbScriptKeywords.add("tan", 7);
            vbScriptKeywords.add("exp", 7);
            vbScriptKeywords.add("log", 7);
            vbScriptKeywords.add("sqr", 7);
            vbScriptKeywords.add("rnd", 7);
            vbScriptKeywords.add("rgb", 7);
            vbScriptKeywords.add("createobject", 7);
            vbScriptKeywords.add("getobject", 7);
            vbScriptKeywords.add("getref", 7);
            vbScriptKeywords.add("abs", 7);
            vbScriptKeywords.add("int", 7);
            vbScriptKeywords.add("fix", 7);
            vbScriptKeywords.add("round", 7);
            vbScriptKeywords.add("sgn", 7);
            vbScriptKeywords.add("scriptengine", 7);
            vbScriptKeywords.add("scriptenginebuildversion", 7);
            vbScriptKeywords.add("scriptenginemajorversion", 7);
            vbScriptKeywords.add("scriptengineminorversion", 7);
            vbScriptKeywords.add("asc", 7);
            vbScriptKeywords.add("ascb", 7);
            vbScriptKeywords.add("ascw", 7);
            vbScriptKeywords.add("chr", 7);
            vbScriptKeywords.add("chrb", 7);
            vbScriptKeywords.add("chrw", 7);
            vbScriptKeywords.add("filter", 7);
            vbScriptKeywords.add("instr", 7);
            vbScriptKeywords.add("instrb", 7);
            vbScriptKeywords.add("instrrev", 7);
            vbScriptKeywords.add("join", 7);
            vbScriptKeywords.add("len", 7);
            vbScriptKeywords.add("lenb", 7);
            vbScriptKeywords.add("lcase", 7);
            vbScriptKeywords.add("ucase", 7);
            vbScriptKeywords.add("left", 7);
            vbScriptKeywords.add("leftb", 7);
            vbScriptKeywords.add("mid", 7);
            vbScriptKeywords.add("midb", 7);
            vbScriptKeywords.add("right", 7);
            vbScriptKeywords.add("rightb", 7);
            vbScriptKeywords.add("replace", 7);
            vbScriptKeywords.add("space", 7);
            vbScriptKeywords.add("split", 7);
            vbScriptKeywords.add("strcomp", 7);
            vbScriptKeywords.add("string", 7);
            vbScriptKeywords.add("strreverse", 7);
            vbScriptKeywords.add("ltrim", 7);
            vbScriptKeywords.add("rtrim", 7);
            vbScriptKeywords.add("trim", 7);
            vbScriptKeywords.add("isarray", 7);
            vbScriptKeywords.add("isdate", 7);
            vbScriptKeywords.add("isempty", 7);
            vbScriptKeywords.add("isnull", 7);
            vbScriptKeywords.add("isnumeric", 7);
            vbScriptKeywords.add("isobject", 7);
            vbScriptKeywords.add("typename", 7);
            vbScriptKeywords.add("vartype", 7);
            vbScriptKeywords.add("adOpenForwardOnly", 4);
            vbScriptKeywords.add("adOpenKeyset", 4);
            vbScriptKeywords.add("adOpenDynamic", 4);
            vbScriptKeywords.add("adOpenStatic", 4);
            vbScriptKeywords.add("adHoldRecords", 4);
            vbScriptKeywords.add("adMovePrevious", 4);
            vbScriptKeywords.add("adAddNew", 4);
            vbScriptKeywords.add("adDelete", 4);
            vbScriptKeywords.add("adUpdate", 4);
            vbScriptKeywords.add("adBookmark", 4);
            vbScriptKeywords.add("adApproxPosition", 4);
            vbScriptKeywords.add("adUpdateBatch", 4);
            vbScriptKeywords.add("adResync", 4);
            vbScriptKeywords.add("adNotify", 4);
            vbScriptKeywords.add("adFind", 4);
            vbScriptKeywords.add("adSeek", 4);
            vbScriptKeywords.add("adIndex", 4);
            vbScriptKeywords.add("adLockReadOnly", 4);
            vbScriptKeywords.add("adLockPessimistic", 4);
            vbScriptKeywords.add("adLockOptimistic", 4);
            vbScriptKeywords.add("adLockBatchOptimistic", 4);
            vbScriptKeywords.add("adRunAsync", 4);
            vbScriptKeywords.add("adAsyncExecute", 4);
            vbScriptKeywords.add("adAsyncFetch", 4);
            vbScriptKeywords.add("adAsyncFetchNonBlocking", 4);
            vbScriptKeywords.add("adExecuteNoRecords", 4);
            vbScriptKeywords.add("adAsyncConnect", 4);
            vbScriptKeywords.add("adStateClosed", 4);
            vbScriptKeywords.add("adStateOpen", 4);
            vbScriptKeywords.add("adStateConnecting", 4);
            vbScriptKeywords.add("adStateExecuting", 4);
            vbScriptKeywords.add("adStateFetching", 4);
            vbScriptKeywords.add("adUseServer", 4);
            vbScriptKeywords.add("adUseClient", 4);
            vbScriptKeywords.add("adEmpty", 4);
            vbScriptKeywords.add("adTinyInt", 4);
            vbScriptKeywords.add("adSmallInt", 4);
            vbScriptKeywords.add("adInteger", 4);
            vbScriptKeywords.add("adBigInt", 4);
            vbScriptKeywords.add("adUnsignedTinyInt", 4);
            vbScriptKeywords.add("adUnsignedSmallInt", 4);
            vbScriptKeywords.add("adUnsignedInt", 4);
            vbScriptKeywords.add("adUnsignedBigInt", 4);
            vbScriptKeywords.add("adSingle", 4);
            vbScriptKeywords.add("adDouble", 4);
            vbScriptKeywords.add("adCurrency", 4);
            vbScriptKeywords.add("adDecimal", 4);
            vbScriptKeywords.add("adNumeric", 4);
            vbScriptKeywords.add("adBoolean", 4);
            vbScriptKeywords.add("adError", 4);
            vbScriptKeywords.add("adUserDefined", 4);
            vbScriptKeywords.add("adVariant", 4);
            vbScriptKeywords.add("adIDispatch", 4);
            vbScriptKeywords.add("adIUnknown", 4);
            vbScriptKeywords.add("adGUID", 4);
            vbScriptKeywords.add("adDate", 4);
            vbScriptKeywords.add("adDBDate", 4);
            vbScriptKeywords.add("adDBTime", 4);
            vbScriptKeywords.add("adDBTimeStamp", 4);
            vbScriptKeywords.add("adBSTR", 4);
            vbScriptKeywords.add("adChar", 4);
            vbScriptKeywords.add("adVarChar", 4);
            vbScriptKeywords.add("adLongVarChar", 4);
            vbScriptKeywords.add("adWChar", 4);
            vbScriptKeywords.add("adVarWChar", 4);
            vbScriptKeywords.add("adLongVarWChar", 4);
            vbScriptKeywords.add("adBinary", 4);
            vbScriptKeywords.add("adVarBinary", 4);
            vbScriptKeywords.add("adLongVarBinary", 4);
            vbScriptKeywords.add("adChapter", 4);
            vbScriptKeywords.add("adFileTime", 4);
            vbScriptKeywords.add("adDBFileTime", 4);
            vbScriptKeywords.add("adPropVariant", 4);
            vbScriptKeywords.add("adVarNumeric", 4);
        }
        return vbScriptKeywords;
    }
}

