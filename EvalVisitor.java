import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner6;


public class EvalVisitor extends CalculatorBaseVisitor<Double> {

/* Storage to save variables */
private HashMap<String, Double> memory = new HashMap<String, Double>();
private HashMap<String, Double> linker = new HashMap<String, Double>();
private HashMap<String, Integer> scopes = new HashMap<String, Integer>();
int scope = 0; //global scope

Scanner sc = new Scanner(System.in);

    @Override
    public Double visitTopExpr(CalculatorParser.TopExprContext ctx) {
        Double value = this.visit(ctx.expr());
		try{
            if (value % 1 == 0) {
                System.out.println(value.intValue());
            }
            else {
                System.out.println(value);
            }
		} catch (RuntimeException e) {

		}
		        return 0.0;
		    }

    @Override
    public Double visitAssignmentStat(CalculatorParser.AssignmentStatContext ctx) {

        Double value = this.visit(ctx.expr());
        String id = ctx.ID().getText();

        if (memory.containsKey(id)) {
            // variable already defined
            memory.replace(id, value);
        }
        else {
            // variable not defined, create a spot in memory
            memory.put(id, value);
        }

        return 0.0;
    }

    @Override
    public Double visitIntAtom(CalculatorParser.IntAtomContext ctx){
        return Double.valueOf(ctx.INT().getText());
    }

    @Override
    public Double visitIdAtom(CalculatorParser.IdAtomContext ctx){

        Double value;
        String id = ctx.ID().getText();

        if (memory.containsKey(id)) {
            // variable already defined
            value = (memory.get(id));
        }
        else {
            // default value to 0
            value = 0.0;
        }

        return value;
    }

    @Override
    public Double visitParenthesisExpr(CalculatorParser.ParenthesisExprContext ctx){
        return this.visit(ctx.expr());
    }

    @Override
    public Double visitMathExpr(CalculatorParser.MathExprContext ctx) {

        Double left = this.visit(ctx.expr(0));
        Double right = this.visit(ctx.expr(1));

        switch (ctx.op.getType()) {
            case (CalculatorParser.ADD):
                return left + right;
            case (CalculatorParser.SUB):
                return left - right;
            case (CalculatorParser.MUL):
                return left * right;
            case (CalculatorParser.DIV):
                return left / right;
            default:
                throw new RuntimeException("unknown operator");
        }
    }

    @Override
    public Double visitComparisonExpr(CalculatorParser.ComparisonExprContext ctx) {
        Double left = this.visit(ctx.expr(0));
        Double right = this.visit(ctx.expr(1));

        switch (ctx.op.getType()) {
            case CalculatorParser.LESS:
                return (Double.compare(left, right) < 0) ? 1.0 : 0.0;
            case CalculatorParser.LESS_EQ:
                return (Double.compare(left, right) <= 0 ) ? 1.0 : 0.0;
            case CalculatorParser.GREATER:
                return (Double.compare(left, right) > 0) ? 1.0 : 0.0;
            case CalculatorParser.GREATER_EQ:
                return (Double.compare(left, right) >= 0) ? 1.0 : 0.0;
            case CalculatorParser.COMPARE:
                return (Double.compare(left, right) == 0) ? 1.0 : 0.0;
            case CalculatorParser.NOT_EQ:
                return (Double.compare(left, right) != 0) ? 1.0 : 0.0;

            default:
                throw new RuntimeException("unknown operator");
		}
	}

    @Override
    public Double visitBooleanExpr(CalculatorParser.BooleanExprContext ctx) {
        Double left = this.visit(ctx.expr(0));
        Double right = this.visit(ctx.expr(1));

        switch(ctx.op.getType()) {
            case CalculatorParser.AND:
                return ((left != 0 ? true : false) && (right != 0 ? true : false)) ? 1.0 : 0.0;

            case CalculatorParser.OR:
                return ((left != 0 ? true : false) || (right != 0 ? true : false )) ? 1.0 : 0.0;

            default:
                    throw new RuntimeException("unknown comparator");

        }
    }

    @Override
    public Double visitNotExpr(CalculatorParser.NotExprContext ctx) {
        Double left = this.visit(ctx.expr());
        return !(Double.compare(left, 0) != 0 ? true : false) ? 1.0 : 0.0;
    }

    @Override
    public Double visitIncrementExpr(CalculatorParser.IncrementExprContext ctx) {
        
        Double value;
        String id = ctx.ID().getText();
        
        if (memory.containsKey(id)) {
            // variable already defined
            value = (memory.get(id));
            memory.replace(id, value+1);
        }
        else {
            // define variable and increment
            value = 0.0;
            memory.put(id, value+1);
        }
        return value;
    }

    @Override
    public Double visitFunctionExpr(CalculatorParser.FunctionExprContext ctx) {
        return this.visit(ctx.func());
    }

    @Override
    public Double visitReadFunc(CalculatorParser.ReadFuncContext ctx) {
        return Double.parseDouble(sc.next());
    }

    @Override
    public Double visitArgumentFunc(CalculatorParser.ArgumentFuncContext ctx) {
        Double arg = this.visit(ctx.expr());
        switch(ctx.f.getType()) {
            case CalculatorParser.PRINT:
                System.out.println(ctx.expr().getText());
                return null;
            case CalculatorParser.SQRT:
                return Math.sqrt(arg);
            case CalculatorParser.SIN:
                return Math.sin(arg);
            case CalculatorParser.COS:
                return Math.cos(arg);
            case CalculatorParser.EX:
                return Math.exp(arg);
            case CalculatorParser.LN:
                return Math.log(arg);
            default:
                throw new RuntimeException("unknown function");
        }
    }
/*
    @Override
    public Double visitStatExpr(CalculatorParser.StatExprContext ctx) {

        Double value = this.visit(ctx.expr());

        if (value % 1 == 0) {
            System.out.println((int)value);
        }
        else {
            System.out.println(value);
        }
    }
*/
    @Override
    public Double visitIf_stat(CalculatorParser.If_statContext ctx) {

        List<CalculatorParser.Condition_blockContext> conditions =  ctx.condition_block();

        boolean evaluated = false;

        for(CalculatorParser.Condition_blockContext condition : conditions) {

            Double value = this.visit(condition.expr());
            boolean toEvaluate = (value != 0)? true : false;

            if(toEvaluate) {
                scope++;
                System.out.println("scope: " + scope);
                evaluated = true;
                this.visit(condition.stat_block());
                scope--;
                System.out.println("scope: " + scope);
            }

        }

        if(!evaluated && ctx.stat_block() != null) {
            scope++;
            System.out.println("scope: " + scope);
            this.visit(ctx.stat_block());
            scope--;
            System.out.println("scope: " + scope);
        }

        return 0.0;
    }


    @Override
    public Double visitWhile_stat(CalculatorParser.While_statContext ctx) {

        scope++;
        System.out.println("scope: " + scope);

        // check condition
        Double value = this.visit(ctx.condition_block().expr());
        boolean toEvaluate = (value != 0)? true : false;

        while(toEvaluate) {
            this.visit(ctx.condition_block().stat_block());

            // re-evaluate condition and set boolean
            value = this.visit(ctx.condition_block().expr());
            toEvaluate = (value != 0)? true : false;
        }

        scope--;
        System.out.println("scope: " + scope);

        return 0.0;
    }

    @Override
    public Double visitFor_stat(CalculatorParser.For_statContext ctx) {

        scope++;
        System.out.println("scope: " + scope);

        // evaluate initial statement
        this.visit(ctx.stat(0));

        // check condition
        Double conditionValue = this.visit(ctx.expr());
        boolean toEvaluate = (conditionValue != 0)? true : false;

        while(toEvaluate) {

            this.visit(ctx.stat_block());

            // evaluate step statement
            this.visit(ctx.stat(1));

            // re-evaluate condition and set boolean
            conditionValue = this.visit(ctx.expr());
            toEvaluate = (conditionValue != 0)? true : false;
        }

        scope--;
        System.out.println("scope: " + scope);

        return 0.0;
    }

}
