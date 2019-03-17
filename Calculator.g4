grammar Calculator;

@header {
    import java.lang.Math; 
    import java.util.HashMap;
    import java.util.Scanner;
}

@members {
    /* Storage to save variables */
    HashMap<String, HashMap<Integer, Double>> memory = new HashMap<String, HashMap<Integer, Double>>();
    Scanner sc = new Scanner(System.in);
}

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
    expr { if ($expr.i % 1 == 0) { System.out.println((int)$expr.i); } else { System.out.println($expr.i); } }
    | ID '=' e=expr {
        if (memory.containsKey($ID.text)) {
            memory.get($ID.text).replace(0, $e.i);
        }
        else {
            HashMap array = new HashMap<Integer, Double>();
            array.put(0, $e.i);
            memory.put($ID.text, array);
        }
    }
    | ID '[' ind=expr ']' '=' e=expr {
        if (memory.containsKey($ID.text)) { // has array
            if (memory.get($ID.text).containsKey($ind.i)) { // has index
                memory.get($ID.text).replace((int)$ind.i, $e.i);
            }
            else {
                memory.get($ID.text).put((int)$ind.i, $e.i);
            }
        }
        else {
            HashMap array = new HashMap<Integer, Double>();
            array.put((int)$ind.i, $e.i);
            memory.put($ID.text, array);
        }
    }
    | '"' ID? '"' { System.out.print($ID.text != null? $ID.text : ""); }
    | COMM NL
    | '}'? NL
    ;

expr returns [double i]: 
    '(' e=expr ')' { $i = $e.i; }
    | SUB expr { $i = -$expr.i; }
    | el=expr op=MUL er=expr { $i = $el.i * $er.i; }
    | el=expr op=DIV er=expr { $i = $el.i / $er.i; }
    | el=expr op=SUB er=expr { $i = $el.i - $er.i; }
    | el=expr op=ADD er=expr { $i = $el.i + $er.i; }
    | el =expr op=COMPARE er=expr { $i = ($el.i == $er.i) ? 1 : 0; }
    | el =expr op=GREATER er=expr { $i = ($el.i > $er.i) ? 1 : 0; }
    | el =expr op=LESS er=expr { $i = ($el.i < $er.i) ? 1 : 0; }
    | el =expr op=GREATER_EQ er=expr { $i = ($el.i >= $er.i) ? 1 : 0; }
    | el =expr op=LESS_EQ er=expr { $i = ($el.i <= $er.i) ? 1 : 0; }
    | el=expr op=AND er=expr { $i = (($el.i != 0 ? true : false) && ($er.i != 0 ? true : false)) ? 1 : 0; }
    | el=expr op=OR er=expr { $i = (($el.i != 0 ? true : false) || ($er.i != 0 ? true : false)) ? 1 : 0; }
    | op=NOT el=expr { $i = (!($el.i != 0 ? true : false) ? 1 : 0); }
    | INT { $i = Double.valueOf($INT.text); }
    | ID '[' e=expr ']' { $i = memory.containsKey($ID.text) ? (memory.get($ID.text)).get((int)$e.i) : -1; }
    | ID { $i = memory.containsKey($ID.text) ? (memory.get($ID.text).get(0)) : -1; }
    | func {$i = $func.i; }
    ;

func returns [double i]:
    f=READ '()' { $i = sc.nextInt(); }
    | f=PRINT '(' a=expr ')' { $i = $a.i; }
    | f=SQRT'(' a=expr ')' { $i = Math.sqrt($a.i); }
    | f=SIN'(' a=expr ')' { $i = Math.sin($a.i); }
    | f=COS'(' a=expr ')' { $i = Math.cos($a.i); }
    | f=EX '(' a=expr ')' { $i = Math.exp($a.i); }
    | f=LN '(' a=expr ')' { $i = Math.log($a.i); }
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
