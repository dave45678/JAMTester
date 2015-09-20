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
import org.gjt.sp.jedit.syntax.TokenMarkerDebugger;
import org.gjt.sp.jedit.syntax.TokenMarkerWithAddToken;

public class ASPPerlscriptTokenMarker
extends TokenMarker
implements TokenMarkerWithAddToken,
MultiModeTokenMarkerWithContext {
    public static final byte S_ONE = 100;
    public static final byte S_TWO = 101;
    public static final byte S_END = 102;
    private KeywordMap keywords;
    private boolean standalone;
    private char matchChar;
    private boolean matchCharBracket;
    private boolean matchSpacesAllowed;
    private TokenMarkerDebugger debug = new TokenMarkerDebugger();
    private static KeywordMap perlKeywords;

    public ASPPerlscriptTokenMarker() {
        this(ASPPerlscriptTokenMarker.getKeywords(), true);
    }

    public ASPPerlscriptTokenMarker(boolean bl) {
        this(ASPPerlscriptTokenMarker.getKeywords(), bl);
    }

    public ASPPerlscriptTokenMarker(KeywordMap keywordMap) {
        this(keywordMap, true);
    }

    public ASPPerlscriptTokenMarker(KeywordMap keywordMap, boolean bl) {
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
        this.matchChar = '\u0000';
        this.matchCharBracket = false;
        this.matchSpacesAllowed = false;
        int n = -1;
        boolean bl = false;
        if (multiModeToken2.token == 3 && multiModeToken2.obj != null) {
            String string = (String)multiModeToken2.obj;
            if (string != null && string.length() == tokenMarkerContext.line.count && tokenMarkerContext.regionMatches(false, string)) {
                tokenMarkerContext.addTokenToEnd(multiModeToken2.token);
                multiModeToken2.token = 0;
                multiModeToken2.obj = null;
                return multiModeToken2;
            }
            tokenMarkerContext.addTokenToEnd(multiModeToken2.token);
            return multiModeToken2;
        }
        boolean bl2 = false;
        this.debug.reset();
        block37 : while (tokenMarkerContext.hasMoreChars()) {
            char c = tokenMarkerContext.getChar();
            if (!this.debug.isOK(tokenMarkerContext)) {
                ++tokenMarkerContext.pos;
            }
            if (c == '\\') {
                bl2 = !bl2;
                ++tokenMarkerContext.pos;
                continue;
            }
            block0 : switch (multiModeToken2.token) {
                case 0: {
                    if (!this.standalone) {
                        if (multiModeToken2.mode == 13 && tokenMarkerContext.regionMatches(true, "<%")) {
                            this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c);
                            return multiModeToken2;
                        }
                        if (multiModeToken2.mode == 6 && tokenMarkerContext.regionMatches(true, "%>")) {
                            this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c);
                            return multiModeToken2;
                        }
                        if ((multiModeToken2.mode == 13 || multiModeToken2.mode == 12) && tokenMarkerContext.regionMatches(true, "</script>")) {
                            this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c);
                            return multiModeToken2;
                        }
                    }
                    switch (c) {
                        int n2;
                        case '#': {
                            if (this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c)) break block0;
                            if (bl2) {
                                bl2 = false;
                                break block0;
                            }
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            tokenMarkerContext.addTokenToEnd(1);
                            break block37;
                        }
                        case '=': {
                            bl2 = false;
                            if (tokenMarkerContext.atFirst()) {
                                multiModeToken2.token = 2;
                                tokenMarkerContext.addTokenToEnd(multiModeToken2.token);
                                break block37;
                            }
                            this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c);
                            break block0;
                        }
                        case '$': 
                        case '%': 
                        case '&': 
                        case '@': {
                            bl2 = false;
                            if (this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c) || tokenMarkerContext.remainingChars() <= 0) break block0;
                            n2 = tokenMarkerContext.getChar(1);
                            if (c == '&' && (n2 == 38 || Character.isWhitespace((char)n2))) {
                                ++tokenMarkerContext.pos;
                                break block0;
                            }
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            multiModeToken2.token = 7;
                            break block0;
                        }
                        case '\"': {
                            if (this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c)) break block0;
                            if (bl2) {
                                bl2 = false;
                                break block0;
                            }
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            multiModeToken2.token = 3;
                            multiModeToken2.obj = null;
                            break block0;
                        }
                        case '\'': {
                            if (bl2) {
                                bl2 = false;
                                break block0;
                            }
                            n2 = tokenMarkerContext.lastKeyword;
                            if (this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c)) break block0;
                            if (tokenMarkerContext.pos != n2) break block0;
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            multiModeToken2.token = 4;
                            break block0;
                        }
                        case '`': {
                            if (this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c)) break block0;
                            if (bl2) {
                                bl2 = false;
                                break block0;
                            }
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            multiModeToken2.token = 9;
                            break block0;
                        }
                        case '<': {
                            String string;
                            if (this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c)) break block0;
                            if (bl2) {
                                bl2 = false;
                                break block0;
                            }
                            if (tokenMarkerContext.remainingChars() <= 1 || tokenMarkerContext.getChar(1) != '<' || Character.isWhitespace(tokenMarkerContext.getChar(2))) break block0;
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            multiModeToken2.token = 3;
                            n2 = tokenMarkerContext.remainingChars() - 1;
                            if (tokenMarkerContext.lastChar() == ';') {
                                --n2;
                            }
                            multiModeToken2.obj = string = this.createReadinString(tokenMarkerContext.array, tokenMarkerContext.pos + 2, n2);
                            tokenMarkerContext.addTokenToEnd(multiModeToken2.token);
                            break block37;
                        }
                        case ':': {
                            bl2 = false;
                            if (this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c)) break block0;
                            if (tokenMarkerContext.lastKeyword != 0) break block0;
                            ++tokenMarkerContext.pos;
                            tokenMarkerContext.addTokenToPos(5);
                            continue block37;
                        }
                        case '-': {
                            bl2 = false;
                            if (this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c) || tokenMarkerContext.pos != tokenMarkerContext.lastKeyword) break block0;
                            if (tokenMarkerContext.remainingChars() < 1) break block0;
                            switch (tokenMarkerContext.getChar(1)) {
                                case 'A': 
                                case 'B': 
                                case 'C': 
                                case 'M': 
                                case 'O': 
                                case 'R': 
                                case 'S': 
                                case 'T': 
                                case 'W': 
                                case 'X': 
                                case 'b': 
                                case 'c': 
                                case 'd': 
                                case 'e': 
                                case 'f': 
                                case 'g': 
                                case 'k': 
                                case 'l': 
                                case 'o': 
                                case 'p': 
                                case 'r': 
                                case 's': 
                                case 't': 
                                case 'u': 
                                case 'w': 
                                case 'x': 
                                case 'z': {
                                    tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                                    ++tokenMarkerContext.pos;
                                    tokenMarkerContext.addTokenToPos(8);
                                    ++tokenMarkerContext.pos;
                                    continue block37;
                                }
                            }
                            break block0;
                        }
                        case '/': 
                        case '?': {
                            if (this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c) || tokenMarkerContext.remainingChars() <= 0) break block0;
                            bl2 = false;
                            n2 = tokenMarkerContext.getChar(1);
                            if (Character.isWhitespace((char)n2)) break block0;
                            this.matchChar = c;
                            this.matchSpacesAllowed = false;
                            tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                            multiModeToken2.token = 100;
                            break block0;
                        }
                    }
                    bl2 = false;
                    if (Character.isLetterOrDigit(c) || c == '_') break;
                    this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c);
                    break;
                }
                case 7: {
                    bl2 = false;
                    if (Character.isLetterOrDigit(c) || c == '_' || c == '#' || c == '\'' || c == ':' || c == '&') break;
                    if (!(tokenMarkerContext.atFirst() || tokenMarkerContext.getChar(-1) != '$')) {
                        ++tokenMarkerContext.pos;
                        tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                        continue block37;
                    }
                    tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                    multiModeToken2.token = 0;
                    continue block37;
                }
                case 100: 
                case 101: {
                    if (bl2) {
                        bl2 = false;
                        break;
                    }
                    if (this.matchChar == '\u0000') {
                        if (Character.isWhitespace(this.matchChar) && !this.matchSpacesAllowed) break;
                        this.matchChar = c;
                        break;
                    }
                    switch (this.matchChar) {
                        case '(': {
                            this.matchChar = 41;
                            this.matchCharBracket = true;
                            break;
                        }
                        case '[': {
                            this.matchChar = 93;
                            this.matchCharBracket = true;
                            break;
                        }
                        case '{': {
                            this.matchChar = 125;
                            this.matchCharBracket = true;
                            break;
                        }
                        case '<': {
                            this.matchChar = 62;
                            this.matchCharBracket = true;
                            break;
                        }
                        default: {
                            this.matchCharBracket = false;
                        }
                    }
                    if (c != this.matchChar) break;
                    if (multiModeToken2.token == 101) {
                        multiModeToken2.token = 100;
                        if (!this.matchCharBracket) break;
                        this.matchChar = '\u0000';
                        break;
                    }
                    multiModeToken2.token = 102;
                    ++tokenMarkerContext.pos;
                    tokenMarkerContext.addTokenToPos(4);
                    continue block37;
                }
                case 102: {
                    bl2 = false;
                    if (Character.isLetterOrDigit(c) || c == '_') break;
                    this.doKeywordToPos(multiModeToken2, tokenMarkerContext, c);
                    break;
                }
                case 2: {
                    bl2 = false;
                    if (!tokenMarkerContext.atFirst()) break;
                    if (tokenMarkerContext.regionMatches(false, "=cut")) {
                        multiModeToken2.token = 0;
                    }
                    tokenMarkerContext.addTokenToEnd(2);
                    break block37;
                }
                case 3: {
                    if (bl2) {
                        bl2 = false;
                        break;
                    }
                    if (c != '\"') break;
                    ++tokenMarkerContext.pos;
                    tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                    multiModeToken2.token = 0;
                    continue block37;
                }
                case 4: {
                    if (bl2) {
                        bl2 = false;
                        break;
                    }
                    if (c != '\'') break;
                    ++tokenMarkerContext.pos;
                    tokenMarkerContext.addTokenToPos(3);
                    multiModeToken2.token = 0;
                    continue block37;
                }
                case 9: {
                    if (bl2) {
                        bl2 = false;
                        break;
                    }
                    if (c != '`') break;
                    ++tokenMarkerContext.pos;
                    tokenMarkerContext.addTokenToPos(multiModeToken2.token);
                    multiModeToken2.token = 0;
                    continue block37;
                }
                default: {
                    throw new InternalError("Invalid state: " + multiModeToken2.token);
                }
            }
            ++tokenMarkerContext.pos;
        }
        if (multiModeToken2.token == 0) {
            this.doKeywordToEnd(multiModeToken2, tokenMarkerContext, '\u0000');
        }
        switch (multiModeToken2.token) {
            case 7: {
                tokenMarkerContext.addTokenToEnd(multiModeToken2.token);
                break;
            }
            case 4: {
                tokenMarkerContext.addTokenToEnd(3);
                break;
            }
            case 102: {
                tokenMarkerContext.addTokenToEnd(4);
                multiModeToken2.token = 0;
                break;
            }
            case 100: 
            case 101: {
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

    private boolean doKeywordToEnd(MultiModeToken multiModeToken, TokenMarkerContext tokenMarkerContext, char c) {
        return this.doKeyword(multiModeToken, tokenMarkerContext, tokenMarkerContext.length, c);
    }

    private boolean doKeywordToPos(MultiModeToken multiModeToken, TokenMarkerContext tokenMarkerContext, char c) {
        return this.doKeyword(multiModeToken, tokenMarkerContext, tokenMarkerContext.pos, c);
    }

    private boolean doKeyword(MultiModeToken multiModeToken, TokenMarkerContext tokenMarkerContext, int n, char c) {
        int n2 = n + 1;
        if (multiModeToken.token == 102) {
            tokenMarkerContext.addTokenToPos(n, 4);
            multiModeToken.token = 0;
            tokenMarkerContext.lastKeyword = n2;
            return false;
        }
        int n3 = n - tokenMarkerContext.lastKeyword;
        byte by = this.keywords.lookup(tokenMarkerContext.line, tokenMarkerContext.lastKeyword, n3);
        if (by == 100 || by == 101) {
            tokenMarkerContext.addTokenToPos(tokenMarkerContext.lastKeyword, 0);
            tokenMarkerContext.addTokenToPos(n, 4);
            tokenMarkerContext.lastKeyword = n2;
            this.matchChar = Character.isWhitespace(c) ? '\u0000' : c;
            this.matchSpacesAllowed = true;
            multiModeToken.token = by;
            return true;
        }
        if (by != 0) {
            tokenMarkerContext.addTokenToPos(tokenMarkerContext.lastKeyword, 0);
            tokenMarkerContext.addTokenToPos(n, by);
        }
        tokenMarkerContext.lastKeyword = n2;
        return false;
    }

    private String createReadinString(char[] arrc, int n, int n2) {
        int n3;
        int n4 = n + n2 - 1;
        for (n3 = n; !(n3 > n4 || Character.isLetterOrDigit(arrc[n3])); ++n3) {
        }
        while (!(n3 > n4 || Character.isLetterOrDigit(arrc[n4]))) {
            --n4;
        }
        return new String(arrc, n3, n4 - n3 + 1);
    }

    private static KeywordMap getKeywords() {
        if (perlKeywords == null) {
            perlKeywords = new KeywordMap(false);
            perlKeywords.add("my", 6);
            perlKeywords.add("our", 6);
            perlKeywords.add("local", 6);
            perlKeywords.add("new", 6);
            perlKeywords.add("if", 6);
            perlKeywords.add("until", 6);
            perlKeywords.add("while", 6);
            perlKeywords.add("elsif", 6);
            perlKeywords.add("else", 6);
            perlKeywords.add("eval", 6);
            perlKeywords.add("unless", 6);
            perlKeywords.add("foreach", 6);
            perlKeywords.add("continue", 6);
            perlKeywords.add("exit", 6);
            perlKeywords.add("die", 6);
            perlKeywords.add("last", 6);
            perlKeywords.add("goto", 6);
            perlKeywords.add("next", 6);
            perlKeywords.add("redo", 6);
            perlKeywords.add("goto", 6);
            perlKeywords.add("return", 6);
            perlKeywords.add("do", 6);
            perlKeywords.add("sub", 6);
            perlKeywords.add("use", 6);
            perlKeywords.add("require", 6);
            perlKeywords.add("package", 6);
            perlKeywords.add("BEGIN", 6);
            perlKeywords.add("END", 6);
            perlKeywords.add("eq", 9);
            perlKeywords.add("ne", 9);
            perlKeywords.add("gt", 9);
            perlKeywords.add("lt", 9);
            perlKeywords.add("le", 9);
            perlKeywords.add("ge", 9);
            perlKeywords.add("not", 9);
            perlKeywords.add("and", 9);
            perlKeywords.add("or", 9);
            perlKeywords.add("cmp", 9);
            perlKeywords.add("xor", 9);
            perlKeywords.add("abs", 8);
            perlKeywords.add("accept", 8);
            perlKeywords.add("alarm", 8);
            perlKeywords.add("atan2", 8);
            perlKeywords.add("bind", 8);
            perlKeywords.add("binmode", 8);
            perlKeywords.add("bless", 8);
            perlKeywords.add("caller", 8);
            perlKeywords.add("chdir", 8);
            perlKeywords.add("chmod", 8);
            perlKeywords.add("chomp", 8);
            perlKeywords.add("chr", 8);
            perlKeywords.add("chroot", 8);
            perlKeywords.add("chown", 8);
            perlKeywords.add("closedir", 8);
            perlKeywords.add("close", 8);
            perlKeywords.add("connect", 8);
            perlKeywords.add("cos", 8);
            perlKeywords.add("crypt", 8);
            perlKeywords.add("dbmclose", 8);
            perlKeywords.add("dbmopen", 8);
            perlKeywords.add("defined", 8);
            perlKeywords.add("delete", 8);
            perlKeywords.add("die", 8);
            perlKeywords.add("dump", 8);
            perlKeywords.add("each", 8);
            perlKeywords.add("endgrent", 8);
            perlKeywords.add("endhostent", 8);
            perlKeywords.add("endnetent", 8);
            perlKeywords.add("endprotoent", 8);
            perlKeywords.add("endpwent", 8);
            perlKeywords.add("endservent", 8);
            perlKeywords.add("eof", 8);
            perlKeywords.add("exec", 8);
            perlKeywords.add("exists", 8);
            perlKeywords.add("exp", 8);
            perlKeywords.add("fctnl", 8);
            perlKeywords.add("fileno", 8);
            perlKeywords.add("flock", 8);
            perlKeywords.add("fork", 8);
            perlKeywords.add("format", 8);
            perlKeywords.add("formline", 8);
            perlKeywords.add("getc", 8);
            perlKeywords.add("getgrent", 8);
            perlKeywords.add("getgrgid", 8);
            perlKeywords.add("getgrnam", 8);
            perlKeywords.add("gethostbyaddr", 8);
            perlKeywords.add("gethostbyname", 8);
            perlKeywords.add("gethostent", 8);
            perlKeywords.add("getlogin", 8);
            perlKeywords.add("getnetbyaddr", 8);
            perlKeywords.add("getnetbyname", 8);
            perlKeywords.add("getnetent", 8);
            perlKeywords.add("getpeername", 8);
            perlKeywords.add("getpgrp", 8);
            perlKeywords.add("getppid", 8);
            perlKeywords.add("getpriority", 8);
            perlKeywords.add("getprotobyname", 8);
            perlKeywords.add("getprotobynumber", 8);
            perlKeywords.add("getprotoent", 8);
            perlKeywords.add("getpwent", 8);
            perlKeywords.add("getpwnam", 8);
            perlKeywords.add("getpwuid", 8);
            perlKeywords.add("getservbyname", 8);
            perlKeywords.add("getservbyport", 8);
            perlKeywords.add("getservent", 8);
            perlKeywords.add("getsockname", 8);
            perlKeywords.add("getsockopt", 8);
            perlKeywords.add("glob", 8);
            perlKeywords.add("gmtime", 8);
            perlKeywords.add("grep", 8);
            perlKeywords.add("hex", 8);
            perlKeywords.add("import", 8);
            perlKeywords.add("index", 8);
            perlKeywords.add("int", 8);
            perlKeywords.add("ioctl", 8);
            perlKeywords.add("join", 8);
            perlKeywords.add("keys", 8);
            perlKeywords.add("kill", 8);
            perlKeywords.add("lcfirst", 8);
            perlKeywords.add("lc", 8);
            perlKeywords.add("length", 8);
            perlKeywords.add("link", 8);
            perlKeywords.add("listen", 8);
            perlKeywords.add("log", 8);
            perlKeywords.add("localtime", 8);
            perlKeywords.add("lstat", 8);
            perlKeywords.add("map", 8);
            perlKeywords.add("mkdir", 8);
            perlKeywords.add("msgctl", 8);
            perlKeywords.add("msgget", 8);
            perlKeywords.add("msgrcv", 8);
            perlKeywords.add("no", 8);
            perlKeywords.add("oct", 8);
            perlKeywords.add("opendir", 8);
            perlKeywords.add("open", 8);
            perlKeywords.add("ord", 8);
            perlKeywords.add("pack", 8);
            perlKeywords.add("pipe", 8);
            perlKeywords.add("pop", 8);
            perlKeywords.add("pos", 8);
            perlKeywords.add("printf", 8);
            perlKeywords.add("print", 8);
            perlKeywords.add("push", 8);
            perlKeywords.add("quotemeta", 8);
            perlKeywords.add("rand", 8);
            perlKeywords.add("readdir", 8);
            perlKeywords.add("read", 8);
            perlKeywords.add("readlink", 8);
            perlKeywords.add("recv", 8);
            perlKeywords.add("ref", 8);
            perlKeywords.add("rename", 8);
            perlKeywords.add("reset", 8);
            perlKeywords.add("reverse", 8);
            perlKeywords.add("rewinddir", 8);
            perlKeywords.add("rindex", 8);
            perlKeywords.add("rmdir", 8);
            perlKeywords.add("scalar", 8);
            perlKeywords.add("seekdir", 8);
            perlKeywords.add("seek", 8);
            perlKeywords.add("select", 8);
            perlKeywords.add("semctl", 8);
            perlKeywords.add("semget", 8);
            perlKeywords.add("semop", 8);
            perlKeywords.add("send", 8);
            perlKeywords.add("setgrent", 8);
            perlKeywords.add("sethostent", 8);
            perlKeywords.add("setnetent", 8);
            perlKeywords.add("setpgrp", 8);
            perlKeywords.add("setpriority", 8);
            perlKeywords.add("setprotoent", 8);
            perlKeywords.add("setpwent", 8);
            perlKeywords.add("setsockopt", 8);
            perlKeywords.add("shift", 8);
            perlKeywords.add("shmctl", 8);
            perlKeywords.add("shmget", 8);
            perlKeywords.add("shmread", 8);
            perlKeywords.add("shmwrite", 8);
            perlKeywords.add("shutdown", 8);
            perlKeywords.add("sin", 8);
            perlKeywords.add("sleep", 8);
            perlKeywords.add("socket", 8);
            perlKeywords.add("socketpair", 8);
            perlKeywords.add("sort", 8);
            perlKeywords.add("splice", 8);
            perlKeywords.add("split", 8);
            perlKeywords.add("sprintf", 8);
            perlKeywords.add("sqrt", 8);
            perlKeywords.add("srand", 8);
            perlKeywords.add("stat", 8);
            perlKeywords.add("study", 8);
            perlKeywords.add("substr", 8);
            perlKeywords.add("symlink", 8);
            perlKeywords.add("syscall", 8);
            perlKeywords.add("sysopen", 8);
            perlKeywords.add("sysread", 8);
            perlKeywords.add("syswrite", 8);
            perlKeywords.add("telldir", 8);
            perlKeywords.add("tell", 8);
            perlKeywords.add("tie", 8);
            perlKeywords.add("tied", 8);
            perlKeywords.add("time", 8);
            perlKeywords.add("times", 8);
            perlKeywords.add("truncate", 8);
            perlKeywords.add("uc", 8);
            perlKeywords.add("ucfirst", 8);
            perlKeywords.add("umask", 8);
            perlKeywords.add("undef", 8);
            perlKeywords.add("unlink", 8);
            perlKeywords.add("unpack", 8);
            perlKeywords.add("unshift", 8);
            perlKeywords.add("untie", 8);
            perlKeywords.add("utime", 8);
            perlKeywords.add("values", 8);
            perlKeywords.add("vec", 8);
            perlKeywords.add("wait", 8);
            perlKeywords.add("waitpid", 8);
            perlKeywords.add("wantarray", 8);
            perlKeywords.add("warn", 8);
            perlKeywords.add("write", 8);
            perlKeywords.add("m", 100);
            perlKeywords.add("q", 100);
            perlKeywords.add("qq", 100);
            perlKeywords.add("qw", 100);
            perlKeywords.add("qx", 100);
            perlKeywords.add("s", 101);
            perlKeywords.add("tr", 101);
            perlKeywords.add("y", 101);
        }
        return perlKeywords;
    }
}

