grammar Calculator;

prog:
    top_stat+ EOF
    ;

top_stat:
    function_def
    | if_stat
    | while_stat
    | for_stat
    | stat
;

function_def: DEFINE ID '(' params ')' stat_block ;

params: ID+ (',' ID+)* ;

if_stat: IF condition_block NL? (ELSE IF condition_block)* NL? (ELSE stat_block)?;

while_stat: WHILE condition_block;

for_stat: FOR '(' stat ';' expr ';' stat ')' stat_block;

condition_block: '(' expr ')' stat_block;

stat_block:
    '{' top_stat* '}'
    | NL? top_stat
;

stat:
    ID ADD ADD                                  #incrementExpr
    | ID '(' args? ')'                          #functionCall
    | expr                                      #topExpr
    | ID '=' expr                               #assignmentStat
    | '"' ID? '"'                               #stringStat
    | COMM NL                                   #comment
    | NL                                        #newLine
    ;

expr:
    '(' expr ')'                                #parenthesisExpr
    | RETURN expr                               #returnStat
    | SUB expr                                  #negateExpr
    | expr op=(MUL | DIV) expr                  #multExpr
    | expr op=(ADD | SUB) expr                  #addExpr
    | expr op=(COMPARE | GREATER | LESS
    | GREATER_EQ | LESS_EQ | NOT_EQ) expr       #comparisonExpr
    | expr op=(AND | OR ) expr                  #booleanExpr
    | op=NOT expr 								#notExpr
    | INT                                       #intAtom
    | ID                                        #idAtom
    | func                                      #functionExpr
    ;

func:
    f=READ '()'                                 #readFunc
    | f=(PRINT | SQRT | SIN
        | COS | EX | LN ) '(' expr ')'          #argumentFunc
    ;

args: INT+ (',' INT+)* ;

fragment DIGIT : [0-9] ;

IF : 'if';
ELSE : 'else';
WHILE : 'while';
FOR : 'for' ;
DEFINE : 'define' ;
RETURN : 'return';

READ : 'read' ;
SQRT : 'sqrt' ;
PRINT : 'print' ;
EX : 'e' ;
COS : 'c' ;
SIN : 's' ;
LN : 'l' ;

MUL : '*' ;
DIV : '/' ;
SUB : '-' ;
ADD : '+' ;
EQ : '=' ;
COMPARE : '==' ;
NOT_EQ : '!=';
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
