package Lexer;

import java.util.*;
import CompilerSPy.*;

public class Lexer {

    private int dedentCount;
    private boolean flagNEWLINE;
    private boolean flagDENT;
    private int tabbing;
    private int currentIdent;
    private int expectedIdent;
    private String value;

    public Lexer(char[] input, CompilerError error) {
        this.input = input;
        // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
        // number of the current line
        lineNumber = 1;
        tokenPos = 0;
        lastTokenPos = 0;
        beforeLastTokenPos = 0;
        dedentCount = 0;
        tabbing = 4;
        expectedIdent = 0;
        currentIdent = 0;
        flagNEWLINE = true;
        this.error = error;
    }
    private static final int MaxValueInteger = 32767;
    // contains the keywords
    static private Hashtable keywordsTable;

    // this code will be executed only once for each program execution
    static {
        keywordsTable = new Hashtable();

        keywordsTable.put("print", new Integer(Symbol.PRINT));
        keywordsTable.put("break", new Integer(Symbol.BREAK));
        keywordsTable.put("continue", new Integer(Symbol.CONTINUE));
        keywordsTable.put("return", new Integer(Symbol.RETURN));
        keywordsTable.put("if", new Integer(Symbol.IF));
        keywordsTable.put("elif", new Integer(Symbol.ELIF));
        keywordsTable.put("else", new Integer(Symbol.ELSE));
        keywordsTable.put("while", new Integer(Symbol.WHILE));
        keywordsTable.put("for", new Integer(Symbol.FOR));
        keywordsTable.put("range", new Integer(Symbol.RANGE));
        keywordsTable.put("def", new Integer(Symbol.DEF));
        keywordsTable.put("class", new Integer(Symbol.CLASS));
        keywordsTable.put("self", new Integer(Symbol.SELF));
        keywordsTable.put("not", new Integer(Symbol.NOT));
        keywordsTable.put("in", new Integer(Symbol.IN));
        keywordsTable.put("is", new Integer(Symbol.IS));
        keywordsTable.put("and", new Integer(Symbol.AND));
        keywordsTable.put("or", new Integer(Symbol.OR));
        keywordsTable.put("isnot", new Integer(Symbol.ISNOT));
        keywordsTable.put("notin", new Integer(Symbol.NOTIN));

    }

    private void isNumber() {
        String number = new String();
        while (Character.isDigit(input[tokenPos])) {
            number += String.valueOf(input[tokenPos]);
            tokenPos++;
        }
        token = Symbol.NUM;
        try {
            numberValue = Integer.valueOf(number).intValue();
        } catch (NumberFormatException e) {
            error.show("Number out of limits");
        }
        if (numberValue > MaxValueInteger) {
            error.show("Number out of limits");
        }
    }

    public boolean fourSpaces() {
        if (input[tokenPos] == ' ' && input[tokenPos + 1] == ' '
                && input[tokenPos + 2] == ' ' && input[tokenPos + 3] == ' ') {
            tokenPos += 4;
            return true;
        }
        return false;
    }

    public void nextToken() {
        char ch;

        lastTokenPos = tokenPos;
        // inicio copia outro grupo
        currentIdent = 0;
        value = "";
        //Se não tem DEDENT pra pegar na lista
        if (dedentCount == 0) {
            //Ignorando Comentários
            if (input[tokenPos] == '#') {
                while (input[tokenPos] != '\n' && input[tokenPos] != '\0') {
                    tokenPos++;
                }
                nextToken();
                return;
            }
            //Se o último token foi NEWLINE, talvez exista IDENT ou DEDENT a seguir
            if (flagNEWLINE) {
                //Ignorando NEWLINES em sequência
                while (tokenPos < input.length && (input[tokenPos] == '\n' || input[tokenPos] == '\r')) {
                    lineNumber++;
                    tokenPos++;
                }
                //Conta os espaços pra identação
                while (tokenPos < input.length && input[tokenPos] == ' ') {
                    currentIdent = currentIdent + 1;
                    tokenPos++;
                }
                //Mágica pra resolver o que os espaços ou a ausência deles significam
                if (currentIdent % tabbing != 0) {
                    error.show("Identação errada", false);
                } else {
                    if (currentIdent < expectedIdent) {
                        //DEDENT
                        expectedIdent = expectedIdent - tabbing;
                        token = Symbol.DEDENT;
                        while (expectedIdent > currentIdent) {
                            expectedIdent = expectedIdent - tabbing;
                            dedentCount++;
                        }
                        flagDENT = true;
                    } else if (currentIdent > expectedIdent) {
                        //IDENT
                        expectedIdent = currentIdent;
                        token = Symbol.INDENT;
                        flagDENT = true;
                    } else {
                        //SEMDENT
                        flagDENT = false;
                    }
                }
                //Último token não é um NEWLINE
                flagNEWLINE = false;
            } else {
                //Ignorando espaços em branco
                while (tokenPos < input.length && flagNEWLINE == false
                        && (input[tokenPos] == ' ' || input[tokenPos] == '\t' || input[tokenPos] == '\r')) {
                    tokenPos++;
                }
            }
            if (!flagDENT) {
                // FIM copia
                if ((ch = input[tokenPos]) == '\0') {
                    token = Symbol.EOF;
                } else {
                    // skip comment
                    if (input[tokenPos] == '#') {
                        while (input[tokenPos] != '\n' && input[tokenPos] != '\0') {
                            tokenPos++;
                        }
                        nextToken();
                        return;
                    } else {
                        // number found
                        if (Character.isDigit(input[tokenPos])) {
                            isNumber();
                        } else if (Character.isLetter(input[tokenPos])) {
                            String ident = new String();
                            // Se for letra,numero ou underline -> considera como variavel.
                            while (Character.isLetter(input[tokenPos]) || Character.isDigit(input[tokenPos]) || input[tokenPos] == '_') {
                                ident += input[tokenPos];
                                tokenPos++;
                            }

                            if (keywordsTable.get(ident) == null) {
                                token = Symbol.ID;

                                if (!variaveis.contains(ident)) {
                                    variaveis.add(ident);
                                }

                                this.setStringValue(ident);
                            } else {

                                token = new Integer(keywordsTable.get(ident).toString());
                            }

                        } else if (input[tokenPos] == '\'' || input[tokenPos] == '\"') {//STRING+
                            tokenPos++;
                            String text = new String();
                            while (input[tokenPos] != '\'' && input[tokenPos] != '\"') {
                                if (input[tokenPos] == '\0') {
                                    error.show("\" expected!: '");
                                }
                                text += input[tokenPos];
                                tokenPos++;
                            }
                            tokenPos++;
                            this.setStringValue(text);
                            token = Symbol.STRING;
                        } else {
                            tokenPos++;
                            switch (ch) {
                                case '+':
                                    if (input[tokenPos] == '=') {
                                        tokenPos++;
                                        token = Symbol.PLUSASSIGN;
                                    } else {
                                        token = Symbol.PLUS;
                                    }
                                    break;
                                case '-':
                                    if (Character.isDigit(input[tokenPos])) {
                                        isNumber();
                                        numberValue = (-1) * numberValue;
                                    } else if (input[tokenPos] == '=') {
                                        tokenPos++;
                                        token = Symbol.MINUSASSIGN;
                                    } else {
                                        token = Symbol.MINUS;
                                    }
                                    break;
                                case '*':
                                    if (input[tokenPos] == '=') {
                                        tokenPos++;
                                        token = Symbol.MULTIASSIGN;
                                    } else {
                                        token = Symbol.MULT;
                                    }
                                    break;
                                case '/':
                                    if (input[tokenPos] == '/') {
                                        tokenPos++;
                                        token = Symbol.FLOORDIV;
                                    } else if (input[tokenPos] == '=') {
                                        tokenPos++;
                                        token = Symbol.DIVASSIGN;
                                    } else {
                                        token = Symbol.DIV;
                                    }
                                    break;
                                case '%':
                                    if (input[tokenPos] == '=') {
                                        tokenPos++;
                                        token = Symbol.MODASSIGN;
                                    } else {
                                        token = Symbol.MOD;
                                    }
                                    break;
                                case '~':
                                    token = Symbol.INVERTION;
                                    break;
                                case '[':
                                    token = Symbol.LEFTCURBRACKET;
                                    break;
                                case ']':
                                    token = Symbol.RIGHTCURBRACKET;
                                    break;
                                case '&':
                                    if (input[tokenPos] == '=') {
                                        tokenPos++;
                                        token = Symbol.ANDASSIGN;
                                    } else {
                                        token = Symbol.AND;
                                    }
                                    break;
                                case '^':
                                    if (input[tokenPos] == '=') {
                                        tokenPos++;
                                        token = Symbol.XORASSIGN;
                                    } else {
                                        token = Symbol.XOR;
                                    }
                                    break;
                                case '|':
                                    if (input[tokenPos] == '=') {
                                        tokenPos++;
                                        token = Symbol.ORASSIGN;
                                    } else {
                                        token = Symbol.OR;
                                    }
                                case '<':
                                    if (input[tokenPos] == '>') {
                                        tokenPos++;
                                        token = Symbol.NEQ;
                                    } else if (input[tokenPos] == '=') {
                                        tokenPos++;
                                        token = Symbol.LE;
                                    } else {
                                        token = Symbol.LT;
                                    }
                                    break;
                                case '>':
                                    if (input[tokenPos] == '=') {
                                        tokenPos++;
                                        token = Symbol.GE;
                                    } else {
                                        token = Symbol.GT;
                                    }
                                    break;
                                case '=':
                                    if (input[tokenPos] == '=') {
                                        tokenPos++;
                                        token = Symbol.EQ;
                                    } else {
                                        token = Symbol.ASSIGN;
                                    }
                                    break;
                                case ',':
                                    token = Symbol.COMMA;
                                    break;
                                case ';':
                                    token = Symbol.SEMICOLON;
                                    break;
                                case ':':
                                    token = Symbol.COLON;
                                    break;
                                case '(':
                                    token = Symbol.LEFTPAR;
                                    break;
                                case ')':
                                    token = Symbol.RIGHTPAR;
                                    break;
                                case '\n':
                                    token = Symbol.NEWLINE;
                                    lineNumber++;
                                    flagNEWLINE = true;
                                    break;
                                case '.':
                                    error.show("Invalid Character: '" + ch + "'", false);
                                default:
                                    error.show("Invalid Character: '" + ch + "'", false);
                            }
                        }
                    }
                }
                beforeLastTokenPos = lastTokenPos;
            }
            flagDENT = false;
            //Se tem DEDENT pra pegar na lista
        } else {
            token = Symbol.DEDENT;
            dedentCount--;
        }

    }

    // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }

    public int getLineNumberBeforeLastToken() {
        return getLineNumber(lastTokenPos);
    }

    private int getLineNumber(int index) {
        // return the line number in which the character input[index] is
        int i, n, size;
        n = 1;
        i = 0;
        size = input.length;
        while (i < size && i < index) {
            if (input[i] == '\n') {
                n++;
            }
            i++;
        }
        return n;
    }

    public String getCurrentLine() {
        // return getLine(lastTokenPos);
        return getLine(tokenPos);
    }

    public String getLineBeforeLastToken() {
        return getLine(beforeLastTokenPos);
    }

    private String getLine(int index) {
        // get the line that contains input[index]. Assume input[index] is at a
        // token, not
        // a white space or newline

        int i;
        if (input.length <= 1) {
            return "";
        }
        i = index;
        if (i <= 0) {
            i = 1;
        } else if (i >= input.length) {
            i = input.length;
        }

        StringBuffer line = new StringBuffer();
        // go to the beginning of the line
        while (i >= 1 && input[i] != '\n') {
            i--;
        }
        if (input[i] == '\n') {
            i++;
        }
        // go to the end of the line putting it in variable line
        while (input[i] != '\0' && input[i] != '\n' && input[i] != '\r') {
            line.append(input[i]);
            i++;
        }
        return line.toString();
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String value) {
        stringValue = value;
    }

    public int getNumberValue() {
        return numberValue;
    }

    public String getLiteralStringValue() {
        return literalStringValue;
    }
    // current token
    public int token;
    private String stringValue, literalStringValue;
    private int numberValue;
    private int tokenPos;
    // input[lastTokenPos] is the last character of the last token found
    private int lastTokenPos;
    private int lastTab, indent, dedent;
    // input[beforeLastTokenPos] is the last character of the token before the
    // last
    // token found
    private int beforeLastTokenPos;
    // program given as input - source code
    private char[] input;
    // number of current line. Starts with 1
    private int lineNumber;
    private CompilerError error;
    public ArrayList<String> variaveis = new ArrayList<>();
}
