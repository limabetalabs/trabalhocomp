package CompilerSPy;

import AST.*;
import java.util.*;
import Lexer.*;
import java.io.*;

//stmt: simple_stmt | compound_stmt
//simple_stmt: small_stmt (';' small_stmt)* NEWLINE
//small_stmt: (expr_stmt | print_stmt  | flow_stmt)
//
//expr_stmt: targetlist augassign listmaker
//augassign: ('=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '|=' | '^=')
//targetlist = target ("," target)* 
//target = NAME
//
//# For normal assignments, additional restrictions enforced by the interpreter
//print_stmt: 'print' ( test (',' test)* )
//flow_stmt: break_stmt | continue_stmt | return_stmt 
//break_stmt: 'break'
//continue_stmt: 'continue'
//return_stmt: 'return' test
//
//compound_stmt: if_stmt | while_stmt | for_stmt | funcdef | classdef
//if_stmt: 'if' test ':' suite ('elif' test ':' suite)* ['else' ':' suite]
//while_stmt: 'while' test ':' suite ['else' ':' suite]
//for_stmt: 'for' exprlist 'in' atom ':' suite ['else' ':' suite] |
//	  'for' exprlist 'in' 'range' '(' NUMBER ',' NUMBER ')' ':' suite ['else' ':' suite] 
//suite: simple_stmt | NEWLINE INDENT stmt+ DEDENT
//
//funcdef: 'def' NAME parameters ':' suite
//parameters: '(' [varargslist] ')'
//varargslist: ([fpdef ['=' test] (',' fpdef ['=' test])* ] )
//fpdef: NAME | '(' fplist ')'
//fplist: fpdef (',' fpdef)* 
//
//classdef: 'class' NAME ['(' [atom [',' atom]* ] ')'] ':' suite
//
//test: or_test ['if' or_test 'else' test]
//or_test: and_test ('or' and_test)*
//and_test: not_test ('and' not_test)*
//not_test: 'not' not_test | comparison
//comparison: expr (comp_op expr)*
//comp_op: '<'|'>'|'=='|'>='|'<='|'<>'|'!='|'in'|'not' 'in'|'is'|'is' 'not'
//expr: xor_expr ('|' xor_expr)*
//xor_expr: and_expr ('^' and_expr)*
//and_expr: arith_expr ('&' arith_expr)*
//arith_expr: term (('+'|'-') term)*
//term: factor (('*'|'/'|'%'|'//') factor)*
//factor: ('+'|'-'|'~') factor | atom
//atom: '[' [listmaker] ']' | NAME | NUMBER | STRING+
//
//listmaker: test (',' test)* 
//
//# o primeiro uso de uma variável deve definir o seu tipo (sem tipagem dinâmica)
//# STRING:  ''' (?)* '''
//# NUMBER: int ou float
//# self é uma palavra-chave e deve ser tratado
//# não é necessário implementar property
//# não vamos trabalhar com herança
public class Compiler {

    private SymbolTable symbolTable;
//    private SymbolTable symbolTableClasses;
//    private SymbolTable symbolTableFuncoes;
    private Lexer lexer;
    private CompilerError error;

    // compile must receive an input with an character less than 
    // p_input.lenght
    public Program compile(char[] input, PrintWriter outError) {

        symbolTable = new SymbolTable();
//        symbolTableClasses = new SymbolTable();
//        symbolTableFuncoes = new SymbolTable();
        error = new CompilerError(lexer, new PrintWriter(outError));
        lexer = new Lexer(input, error);
        error.setLexer(lexer);

        Program p = null;
        ArrayList<Stmt> listStmt = new ArrayList<>();
        try {

            lexer.nextToken();

            if (lexer.token == Symbol.EOF) {
                error.show("Unexpected EOF");
            }
            Stmt s = stmt();
            listStmt.add(s);
            while (lexer.token != Symbol.EOF) {
                s = stmt();
                listStmt.add(s);
                if (lexer.token == Symbol.DEDENT || lexer.token == Symbol.NEWLINE) {
                    lexer.nextToken();
                }
            }
        } catch (Exception e) {
            // the below statement prints the stack of called methods.
            // of course, it should be removed if the compiler were 
            // a production compiler.
//            e.printStackTrace();
        }

        p = new Program(listStmt, symbolTable);

        return p;
    }

    //stmt: simple_stmt | compound_stmt
    public Stmt stmt() {
        CompoundStmt compound = null;
        SimpleStmt simple = null;
        if (lexer.token == Symbol.IF || lexer.token == Symbol.WHILE
                || lexer.token == Symbol.FOR || lexer.token == Symbol.CLASS || lexer.token == Symbol.DEF) {
            compound = compound_stmt();
        } else {
            simple = simple_stmt();
        }
        Stmt s = new Stmt(simple, compound);
        return s;
    }

    //simple_stmt: small_stmt (';' small_stmt)* NEWLINE
    private SimpleStmt simple_stmt() {

        ArrayList<SmallStmt> small_stmt_list = new ArrayList<SmallStmt>();
        while (lexer.token == Symbol.ID || lexer.token == Symbol.PRINT
                || lexer.token == Symbol.CONTINUE || lexer.token == Symbol.BREAK || lexer.token == Symbol.RETURN && lexer.token != Symbol.EOF) {
            SmallStmt s = small_stmt();
            if (s != null) {
                small_stmt_list.add(s);
            }
            if (lexer.token == Symbol.SEMICOLON) {
                lexer.nextToken();
            }
        }
        if (lexer.token == Symbol.NEWLINE) {
            lexer.nextToken();
        } else {
            error.show("Nova linha esperada!(obs ultima linha do arquivo deve ser em branco).", true);
        }
        SimpleStmt smallstmt = new SimpleStmt(small_stmt_list);
        return smallstmt;
    }

    //small_stmt: (expr_stmt | print_stmt | flow_stmt)
    private SmallStmt small_stmt() {
        switch (lexer.token) {
            case Symbol.ID:
                return expr_stmt();
            case Symbol.PRINT:
                return print_stmt();
            case (Symbol.CONTINUE | Symbol.BREAK | Symbol.RETURN):
                return flow_stmt();
            default:
                error.show("Token esperado!", true);
                return null;
        }
    }

    //flow_stmt: break_stmt | continue_stmt | return_stmt 
    private FlowStmt flow_stmt() {
        switch (lexer.token) {
            case Symbol.RETURN:
                return return_stmt();
            case Symbol.BREAK:
                return break_stmt();
            case Symbol.CONTINUE:
                return continue_stmt();
            default:
                error.show("Token esperado!", true);
                return null;
        }
    }

    //expr_stmt: targetlist augassign listmaker
    private ExprStmt expr_stmt() {
        TargetList target_list = target_list();
        AugAssign augassign = augassign();
        ListMaker list_maker = list_maker();

        if (target_list.getSize() != list_maker.getSize()) {
            error.show("Número de argumentos incorreto.", true);
        } else {
            int i = 0;
            String typeTest, typeTarget;
            Target auxTarget;
            Test auxTest;

            while (i < target_list.getSize()) {
                auxTarget = target_list.getElement(i);
                auxTest = list_maker.getElement(i);
                typeTest = auxTest.getType();

                if (typeTest != "int" && typeTest != "string") {
                    typeTest = symbolTable.getInGlobal(lexer.getStringValue()).toString();
                }
                typeTarget = symbolTable.getInGlobal(auxTarget.getName()).toString();
                if (typeTarget == "undefined") {
                    symbolTable.putInGlobal(auxTarget.getName(), typeTest);
                } else {
                    if (typeTarget != typeTest) {
                        error.show("tipos incompativeis");
                    }
                }
                target_list.get(i).setType(typeTest);
                i++;
            }
        }
        return new ExprStmt(target_list, augassign, list_maker);
    }

    //return_stmt: 'return' test
    private ReturnStmt return_stmt() {
        lexer.nextToken();//passa o 'return'
        Test test = test();
        ReturnStmt ret = new ReturnStmt(test);
        return ret;
    }

    private BreakStmt break_stmt() {
        BreakStmt b = new BreakStmt();
        lexer.nextToken();
        return b;
    }

    private ContinueStmt continue_stmt() {
        ContinueStmt c = new ContinueStmt();
        lexer.nextToken();
        return c;
    }

    //compound_stmt: if_stmt | while_stmt | for_stmt | funcdef | classdef
    private CompoundStmt compound_stmt() {
        switch (lexer.token) {
            case Symbol.IF:
                return if_stmt();
            case Symbol.WHILE:
                return while_stmt();
            case Symbol.FOR:
                return for_stmt();
            case Symbol.DEF:
                return funcdef();
            case Symbol.CLASS:
                return classdef();
            default:
                error.show("Esperado um dos: IF, FOR, WHILE, DEF, CLASS", true);
                break;
        }
        return null;
    }

    //if_stmt: 'if' test ':' suite ('elif' test ':' suite)* ['else' ':' suite]
    private IfStmt if_stmt() {
        lexer.nextToken();
        ArrayList<Test> elifConditionArray = new ArrayList<Test>();
        ArrayList<Suite> elifThenPartArray = new ArrayList<Suite>();
        Suite elsePart = null;
        Test condition = test();
        if (lexer.token != Symbol.COLON) {
            error.show("Esperado: ':'");
        }
        lexer.nextToken();
        Suite thenPart = suite();
        lexer.nextToken();
        while (lexer.token == Symbol.ELIF) {
            lexer.nextToken();
            Test elifCondition = test();
            elifConditionArray.add(elifCondition);
            if (lexer.token != Symbol.COLON) {
                error.show("Esperado: ':'", true);
            }
            lexer.nextToken();
            Suite elifThenPart = suite();
            elifThenPartArray.add(elifThenPart);
        }
        if (lexer.token == Symbol.ELSE) {
            lexer.nextToken();
            if (lexer.token != Symbol.COLON) {
                error.show("Esperado: ':'", true);
            }
            lexer.nextToken();
            elsePart = suite();
        }
        IfStmt ifStmt = new IfStmt(condition, thenPart, elifConditionArray, elifThenPartArray, elsePart);
        return ifStmt;
    }

    //while_stmt: 'while' test ':' suite ['else' ':' suite]
    //CORREÇÃO na linguagem: while_stmt: 'while' '(' test ')' ':' suite ['else' ':' suite]
    private WhileStmt while_stmt() {
        lexer.nextToken();
        Suite elsePart = null;
        if (lexer.token != Symbol.LEFTPAR) {
            error.show("Esperado: '('", true);
        }
        lexer.nextToken();
        Test condition = test();
        if (lexer.token != Symbol.RIGHTPAR) {
            error.show("Esperado: ')'", true);
        }
        lexer.nextToken();
        if (lexer.token != Symbol.COLON) {
            error.show("Esperado: ':'", true);
        }
        lexer.nextToken();
        Suite doPart = suite();
        if (lexer.token == Symbol.ELSE) {
            lexer.nextToken();
            if (lexer.token != Symbol.COLON) {
                error.show("Esperado: ':'", true);
            }
            lexer.nextToken();
            elsePart = suite();
        }
        WhileStmt whileStmt = new WhileStmt(condition, doPart, elsePart);
        return whileStmt;
    }

//for_stmt: 'for' exprlist 'in' atom ':' suite ['else' ':' suite] |
//	  'for' exprlist 'in' 'range' '(' NUMBER ',' NUMBER ')' ':' suite ['else' ':' suite]
    private ForStmt for_stmt() {
        lexer.nextToken();
        ArrayList<Expr> exprList = new ArrayList<Expr>();
        Suite elsePart = null;
        Suite elsePart2 = null;
        while (lexer.token != Symbol.IN) {
            Expr expr = expr();
            exprList.add(expr);
        }
        lexer.nextToken();
        if (lexer.token == Symbol.RANGE) {
            lexer.nextToken();
            if (lexer.token != Symbol.LEFTPAR) {
                error.show("Esperado: '('", true);
            }
            lexer.nextToken();
            if (lexer.token != Symbol.NUM) {
                error.show("Esperado: número", true);
            }
            lexer.nextToken();
            if (lexer.token != Symbol.COMMA) {
                error.show("Esperado: ','", true);
            }
            lexer.nextToken();
            if (lexer.token != Symbol.NUM) {
                error.show("Esperado: número", true);
            }
            lexer.nextToken();
            if (lexer.token != Symbol.RIGHTPAR) {
                error.show("Esperado: ')'", true);
            }
            lexer.nextToken();
            if (lexer.token != Symbol.COLON) {
                error.show("Esperado: ':'", true);
            }
            lexer.nextToken();
            Suite suite_range = suite();
            if (lexer.token == Symbol.ELSE) {
                lexer.nextToken();
                if (lexer.token != Symbol.COLON) {
                    error.show("Esperado: ':'", true);
                }
                lexer.nextToken();
                elsePart = suite();
            }
            return new ForStmt(exprList, null, suite_range, elsePart);
        } else {
            Atom atom = atom();
            if (lexer.token != Symbol.COLON) {
                error.show("Esperado: ':'", true);
            }
            Suite doPart = suite();
            if (lexer.token == Symbol.ELSE) {
                lexer.nextToken();
                if (lexer.token != Symbol.COLON) {
                    error.show("Esperado: ':'", true);
                }
                lexer.nextToken();
                elsePart2 = suite();
            }
            return new ForStmt(exprList, atom, doPart, elsePart2);
        }
    }

    private FuncDec funcdef() {
        lexer.nextToken();
        if (lexer.token != Symbol.ID) {
            error.show("Esperado: Identificador", true);
        }
        String name = lexer.getStringValue();
        lexer.nextToken();
        Parameters parameters = parameters();
        if (lexer.token != Symbol.COLON) {
            error.show("Esperado: ':'", true);
        }
        lexer.nextToken();
        Suite suite = suite();
        FuncDec fdec = new FuncDec(name, parameters, suite);
        if (symbolTable.getInGlobal(name) != null) {
            error.show("NAME já utilizado!", true);
        } else {
            symbolTable.putInGlobal(name, "func");
        }
        return fdec;
    }
    // suite: simple_stmt | NEWLINE INDENT stmt+ DEDENT

    private Suite suite() {
        SimpleStmt simpleStmt = null;
        ArrayList<Stmt> stmtList = new ArrayList<Stmt>();
        if (lexer.token == Symbol.NEWLINE) {
            lexer.nextToken();
            if (lexer.token == Symbol.INDENT) {
                lexer.nextToken();
                while (lexer.token != Symbol.DEDENT) {
                    Stmt stmt = stmt();
                    stmtList.add(stmt);
                }
                lexer.nextToken();
            } else {
                error.show("Esperado: INDENT");
            }
        } else {
            simpleStmt = simple_stmt();
        }
        return new Suite(simpleStmt, stmtList);
    }

    //parameters: '(' [varargslist] ')'
    private Parameters parameters() {
        if (lexer.token != Symbol.LEFTPAR) {
            error.show("Esperado: '('");
        }
        lexer.nextToken();
        VarArgsList varArgsList = var_args_list();
        if (lexer.token != Symbol.RIGHTPAR) {
            error.show("Esperado: ')'");
        }
        return new Parameters(varArgsList);
    }
//varargslist: ([fpdef ['=' test] (',' fpdef ['=' test])* ] )

    private VarArgsList var_args_list() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    //classdef: 'class' NAME ['(' [atom [',' atom]* ] ')'] ':' suite
    private ClassDef classdef() {
        //ClassDef aClassDef = new ClassDef();
        ArrayList<Atom> atomList = new ArrayList<Atom>();
        lexer.nextToken();
        if (lexer.token != Symbol.ID) {
            error.show("Nome da classe não declarado!");
            return null;
        }
        String name = lexer.getStringValue();
        if (symbolTable.getInLocal(name) != null) {
            error.show("Nome da classe já utilizado!");
        } else {
            symbolTable.putInGlobal(name, "class");
        }
        lexer.nextToken();
        if (lexer.token == Symbol.LEFTPAR) {
            lexer.nextToken();
            if (lexer.token != Symbol.RIGHTPAR) {
                atomList.add(atom());
                while (lexer.token == Symbol.COMMA) {
                    lexer.nextToken();
                    atomList.add(atom());
                }
                if (lexer.token != Symbol.RIGHTPAR) {
                    error.show("Parênteses esperado");
                }
            }
            lexer.nextToken();
        }
        if (lexer.token != Symbol.COLON) {
            error.show("':' esperado!");
        }
        lexer.nextToken();
        return new ClassDef(name, atomList, suite());
    }

    //print_stmt: 'print' ( test (',' test)* )
    private PrintStmt print_stmt() {
        ArrayList<Test> testlist = new ArrayList<Test>();
        lexer.nextToken();
        Test test = test();
        testlist.add(test);
        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            test = test();
            testlist.add(test);
        }
        PrintStmt print = new PrintStmt(testlist);
        return print;
    }

    //test: or_test ['if' or_test 'else' test]
    private Test test() {
        OrTest first = or_test();
        OrTest second = null;
        Test last = null;
        if (lexer.token == Symbol.IF) {
            if (lexer.token == Symbol.ELSE) {
                last = test();
            } else {
                error.show("ELSE não declarado", true);
            }
        }
        Test test = new Test(first, second, last);
        return test;
    }

    //targetlist = target ("," target)* 
    private TargetList target_list() {
        ArrayList<Target> targetlist = new ArrayList<Target>();
        Target target = target();
        targetlist.add(target);
        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            target = target();
            targetlist.add(target);
        }
        TargetList tlist = new TargetList(targetlist);
        return tlist;
    }

    //or_test: and_test ('or' and_test)*
    private OrTest or_test() {
        ArrayList<AndTest> andtestlist = new ArrayList<AndTest>();
        AndTest andtest = and_test();
        andtestlist.add(andtest);
        while (lexer.token == Symbol.OR) {
            lexer.nextToken();
            andtest = and_test();
            andtestlist.add(andtest);
        }
        OrTest ortest = new OrTest(andtestlist);
        return ortest;
    }

    //and_test: not_test ('and' not_test)*
    private AndTest and_test() {
        ArrayList<NotTest> nottestlist = new ArrayList<NotTest>();
        NotTest nottest = not_test();
        nottestlist.add(nottest);
        while (lexer.token == Symbol.AND) {
            lexer.nextToken();
            nottest = not_test();
            nottestlist.add(nottest);
        }
        AndTest andtest = new AndTest(nottestlist);
        return andtest;
    }

    //not_test: 'not' not_test | comparison
    private NotTest not_test() {
        Comparison comp = null;
        NotTest not = null;
        if (lexer.token == Symbol.NOT) {
            not = not_test();
        } else {
            comp = comparison();
        }
        NotTest nottest = new NotTest(not, comp);
        return nottest;
    }

    //comparison: expr (comp_op expr)*
    private Comparison comparison() {
        ArrayList<Expr> exprlist = new ArrayList<Expr>();
        ArrayList<CompOp> compopList = new ArrayList<CompOp>();
        Expr fisrtExpr = expr();
        while (lexer.token == Symbol.EQ
                || lexer.token == Symbol.NEQ || lexer.token == Symbol.NOT
                || lexer.token == Symbol.GT || lexer.token == Symbol.LT
                || lexer.token == Symbol.GE
                || lexer.token == Symbol.LE || lexer.token == Symbol.IN
                || lexer.token == Symbol.IS) {
            compopList.add(comp_op());
            exprlist.add(expr());
        }
        Comparison comp = new Comparison(fisrtExpr, compopList, exprlist);
        return comp;
    }

    //comp_op: '<'|'>'|'=='|'>='|'<='|'<>'|'!='|'in'|'not' 'in'|'is'|'is' 'not'
    private CompOp comp_op() {
        CompOp compop = new CompOp(lexer.token);
        lexer.nextToken();
        return compop;
    }

    //expr: xor_expr ('|' xor_expr)*
    private Expr expr() {
        ArrayList<XorExpr> xorexprlist = new ArrayList<XorExpr>();
        xorexprlist.add(xor_expr());
        while (lexer.token == Symbol.OR) {
            lexer.nextToken();
            xorexprlist.add(xor_expr());
        }
        Expr expr = new Expr(xorexprlist);
        return expr;
    }

    //xor_expr: and_expr ('^' and_expr)*
    private XorExpr xor_expr() {
        ArrayList<AndExpr> andexprlist = new ArrayList<AndExpr>();
        andexprlist.add(and_expr());
        while (lexer.token == Symbol.XOR) {
            lexer.nextToken();
            andexprlist.add(and_expr());
        }
        XorExpr xorexpr = new XorExpr(andexprlist);
        return xorexpr;
    }

    // and_expr: arith_expr ('&' arith_expr)*
    public AndExpr and_expr() {
        ArrayList<ArithExpr> arithexprlist = new ArrayList<ArithExpr>();
        arithexprlist.add(arith_expr());
        while (lexer.token == Symbol.AND) {
            lexer.nextToken();
            arithexprlist.add(arith_expr());
        }
        AndExpr andexpr = new AndExpr(arithexprlist);
        return andexpr;
    }

    //arith_expr: term (('+'|'-') term)*
    public ArithExpr arith_expr() {
        ArrayList<Integer> operacoes = new ArrayList<Integer>();
        ArrayList<Term> termlist = new ArrayList<Term>();
        Term firstTerm = term();
        while (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS) {
            operacoes.add(lexer.token);
            lexer.nextToken();
            termlist.add(term());
        }
        ArithExpr airth = new ArithExpr(firstTerm, operacoes, termlist);
        return airth;
    }

    //term: factor (('*'|'/'|'%'|'//') factor)*
    public Term term() {
        ArrayList<Factor> factorlist = new ArrayList<Factor>();
        ArrayList<Integer> operList = new ArrayList<Integer>();

        Factor firstFactor = factor();
        while (lexer.token == Symbol.MULT || lexer.token == Symbol.DIV
                || lexer.token == Symbol.MOD || lexer.token == Symbol.FLOORDIV) {
            operList.add(lexer.token);
            lexer.nextToken();
            factorlist.add(factor());
        }
        Term term = new Term(firstFactor, operList, factorlist);
        return term;
    }

    //factor: ('+'|'-'|'~') factor | atom
    public Factor factor() {
        Factor factor = null;
        Atom atom = null;
        int oper = 0;
        if (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS
                || lexer.token == Symbol.INVERTION) {
            oper = lexer.token;
            factor = factor();
        } else {
            atom = atom();
        }
        Factor fac = new Factor(oper, factor, atom);
        return fac;
    }

    //atom: '[' [listmaker] ']' | NAME | NUMBER | STRING+
    //ajuste linguagem => atom: '[' [listmaker] ']' | NAME | NUMBER | STRING+ | func_call | NAME '.' func_call | NAME '.' NAME | '(' test ')'
    public Atom atom() {
        ListMaker listmaker = null;
        String str = null;
        Integer numb = null;
        String name = null;
        if (lexer.token == Symbol.LEFTCURBRACKET) {
            listmaker = list_maker();
        } else if (lexer.token == Symbol.ID) {
            name = lexer.getStringValue();
            lexer.nextToken();
            if (lexer.token == Symbol.LEFTPAR) {
                if (symbolTable.getInGlobal(name) == null) {
                    error.show("undefined variable");
                }
                lexer.nextToken();
                if (lexer.token != Symbol.RIGHTPAR) {
                    error.show(") esperado");
                }
            }
        } else if (lexer.token == Symbol.NUM) {
            numb = lexer.getNumberValue();
        } else if (lexer.token == Symbol.STRING) {
            str = lexer.getStringValue();
        }
        lexer.nextToken();
        Atom at = new Atom(listmaker, name, numb, str);
        return at;
    }

    //augassign: ('=' | '+=' | '-=' | '*=' | '/=' | '%=' | '&=' | '|=' | '^=')
    private AugAssign augassign() {
        if (lexer.token != Symbol.ASSIGN
                && lexer.token != Symbol.PLUSASSIGN
                && lexer.token != Symbol.MINUSASSIGN
                && lexer.token != Symbol.MULTIASSIGN
                && lexer.token != Symbol.DIVASSIGN
                && lexer.token != Symbol.MODASSIGN
                && lexer.token != Symbol.ANDASSIGN
                && lexer.token != Symbol.ORASSIGN
                && lexer.token != Symbol.XORASSIGN) {
            error.show("Assign expected", true);
        }
        AugAssign augassign = new AugAssign(lexer.token);
        lexer.nextToken();
        return augassign;
    }

    //listmaker: test (',' test)* 
    private ListMaker list_maker() {
        ArrayList<Test> testlist = new ArrayList<Test>();
        Test test = test();
        testlist.add(test);
        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            test = test();
            testlist.add(test);
        }
        ListMaker listmaker = new ListMaker(testlist);
        return listmaker;
    }

    //target = NAME
    //alteração linguagem => target = NAME | 'self' '.' NAME | NAME '.' func_call | NAME '.' NAME
    private Target target() {
        String tipo, nome, nome2;
        nome = null;
        if (lexer.token != Symbol.ID) {
            error.show("NAME experado!", true);
        } else {
            if (lexer.token == Symbol.ID) {
                nome = lexer.getStringValue();

                lexer.nextToken();

                if (lexer.token == Symbol.DOT) {
                    lexer.nextToken();

                    if (lexer.token != Symbol.ID) {
                        error.show("NAME esperado!");
                    }

                    nome2 = lexer.getStringValue();

                    lexer.nextToken();

                    if (lexer.token == Symbol.LEFTPAR) {
                        //Função
                    } else {
                        if (this.symbolTable.getInGlobal(nome) == null) {
                            error.show("Atributo não foi encontrado");
                        }

                        String cl = ((Target) this.symbolTable.getInGlobal(nome)).getType();

                        if (this.symbolTable.getInGlobal(cl + "." + nome2) == null) {
                            error.show("Atributo não foi encontrado");
                        }

                        id = new Target(id1, id2);
                        id.setType(((MaxiExpr) this.symbolTable.get(cl + "." + id2)).getType());
                    }
                }
            } else {
                error.show("NAME esperado!");
            }
//            if (symbolTable.getInGlobal(lexer.getStringValue()) != null) {
//                error.show("NAME já utilizado!", true);
//            } else {
//                symbolTable.putInGlobal(lexer.getStringValue(), "undefined");
//            }
        }
//        tipo = symbolTable.getInGlobal(lexer.getStringValue()).toString();
//        Target id = new Target(nome, tipo);
//        lexer.nextToken();
        return id;
    }
}
