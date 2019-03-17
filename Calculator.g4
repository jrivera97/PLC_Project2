grammar Calculator;

prog:
    top_stat+ EOF
    ;

top_stat:
    if_stat
    | while_stat
    | stat
;

if_stat: IF condition_block (ELSE IF condition_block)* (ELSE stat_block)?;

while_stat: WHILE condition_block;

condition_block: expr stat_block;

stat_block:
    '{' stat*
    | expr
;

stat:
    expr                                        #topExpr
    | ID '=' e=expr                             #assignmentStat
    | '"' ID? '"'                               #stringStat
    | COMM NL                                   #comment
    | '}'? NL                                   #newLine
    ;

expr: 
    '(' expr ')'                                #parenthesisExpr
    | SUB expr                                  #negateExpr
    | expr op=(MUL | DIV | ADD | SUB) expr      #mathExpr
    | expr op=(COMPARE | GREATER | LESS
                | GREATER_EQ | LESS_EQ) expr    #comparisonExpr
    | expr op=(AND | OR | NOT) expr             #booleanExpr
    | INT                                       #intAtom 
    | ID                                        #idAtom
    | func                                      #functionExpr
    ;

func:
    f=READ '()'
    | f=PRINT '(' a=expr ')'
    | f=SQRT'(' a=expr ')'
    | f=SIN'(' a=expr ')'
    | f=COS'(' a=expr ')'
    | f=EX '(' a=expr ')'
    | f=LN '(' a=expr ')'
    ;

fragment DIGIT : [0-9] ;

IF : 'if';
ELSE : 'else';
WHILE : 'while';

READ : 'read' ;
SQRT : 'sqrt' ;
PRINT : 'print' ;
EX : 'e';
COS : 'c';
SIN : 's';
LN : 'l';

MUL : '*' ;
DIV : '/' ;
SUB : '-' ;
ADD : '+' ;
EQ : '=' ;
COMPARE : '==' ;
GREATER : '>' ;
LESS : '<' ;
GREATER_EQ : '>=' ;
LESS_EQ : '<=' ;

AND : '&&' ;
OR : '||' ;
NOT : '!' ;


COMM : '/*' (.)*? '*/' ;

ID : [_A-Za-z]+ ;
INT : DIGIT+ ('.' DIGIT+)? ;

NL : ( '\r' )? '\n' ;
WS : ( ' ' | '\t' )+ -> skip ;
