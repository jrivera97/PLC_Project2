import java.util.*;

import javax.lang.model.util.ElementScanner6;
import org.antlr.v4.runtime.tree.TerminalNode;

public class EvalVisitor extends CalculatorBaseVisitor<Double> {

/* Storage to save variables */
private HashMap<String, Double> memory = new HashMap<String, Double>();
private HashMap<String, Functions> linker = new HashMap<String, Functions>();
private HashMap<String, Integer> scopes = new HashMap<String, Integer>();
int scope;
EvalVisitor(HashMap<String, Double> memory,  HashMap<String, Functions> linker, HashMap<String, Integer> scopes, int scope) {
	this.scope = scope;
	this.memory = memory;
	this.linker = linker;
	this.scopes = scopes;
}


Scanner sc = new Scanner(System.in);

public void clearScope() {
//	System.out.println(scopes.entrySet());
	for (Map.Entry<String, Integer> entry : scopes.entrySet()) {
	//	System.out.println("HELLO???");
		//System.out.println("cscope: " + scope + " key: " + entry.getKey());
    if(entry.getValue() >= scope) {
			scopes.remove(entry.getKey());
			memory.remove(entry.getKey());
			//System.out.println("mem key: " + entry.getKey() + " val: " + memory.get(entry.getKey()));
		}
// Do things with the list
	}
	scope--;
}

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
		        return 110.0;
		    }

    @Override
    public Double visitReturnStat(CalculatorParser.ReturnStatContext ctx) {
        return this.visit(ctx.expr());
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
			scopes.put(id, scope);
        }

        return 100.0;
    }

    @Override
    public Double visitStringStat(CalculatorParser.StringStatContext ctx) {
        if (ctx.ID() != null) System.out.print(ctx.ID());
        return null;
    }

    @Override
    public Double visitIntAtom(CalculatorParser.IntAtomContext ctx){
        return Double.valueOf(ctx.INT().getText());
    }

    @Override
    public Double visitIdAtom(CalculatorParser.IdAtomContext ctx){

        Double value;
        String id = ctx.ID().getText();
				if(scopes.containsKey(id) && scopes.get(id) > scope)
				{
					throw new RuntimeException("variable not in scope");
				}
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
    public Double visitNegateExpr(CalculatorParser.NegateExprContext ctx) {
        return (-1*this.visit(ctx.expr()));
    }

    @Override
    public Double visitAddExpr(CalculatorParser.AddExprContext ctx) {

        Double left = this.visit(ctx.expr(0));
        Double right = this.visit(ctx.expr(1));

        switch (ctx.op.getType()) {
            case (CalculatorParser.ADD):
                return left + right;
            case (CalculatorParser.SUB):
                return left - right;
            default:
                throw new RuntimeException("unknown operator");
        }
    }


    @Override
    public Double visitMultExpr(CalculatorParser.MultExprContext ctx) {

        Double left = this.visit(ctx.expr(0));
        Double right = this.visit(ctx.expr(1));

        switch (ctx.op.getType()) {
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
						scopes.put(id, scope);
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
                evaluated = true;
                this.visit(condition.stat_block());
                clearScope();
            }

        }

        if(!evaluated && ctx.stat_block() != null) {
            scope++;
            this.visit(ctx.stat_block());
            clearScope();
        }

        return 0.0;
    }


    @Override
    public Double visitWhile_stat(CalculatorParser.While_statContext ctx) {

        scope++;

        // check condition
        Double value = this.visit(ctx.condition_block().expr());
        boolean toEvaluate = (value != 0)? true : false;

        while(toEvaluate) {
            this.visit(ctx.condition_block().stat_block());

            // re-evaluate condition and set boolean
            value = this.visit(ctx.condition_block().expr());
            toEvaluate = (value != 0)? true : false;
        }

        clearScope();
        return 0.0;
    }

    @Override
    public Double visitFor_stat(CalculatorParser.For_statContext ctx) {

        scope++;

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

        clearScope();

        return 0.0;
    }


    @Override
    public Double visitFunction_def(CalculatorParser.Function_defContext ctx)
    {
        List<TerminalNode> params = ctx.params() != null ? ctx.params().ID() : new ArrayList<TerminalNode>();
        Functions funcy = new Functions(params, ctx);
        String funcName = funcy.getCtx().ID().getText();
        linker.put(funcName, funcy);

        return 0.0;
    }

    @Override
    public Double visitFunctionCall(CalculatorParser.FunctionCallContext ctx)
    {
        String funcName = ctx.ID().getText();

        // look up function
        Functions funcy = linker.get(funcName);
        if (funcy == null) {
            throw new RuntimeException("function not found");
        }

        List<TerminalNode> args = ctx.args() != null ? ctx.args().INT() : new ArrayList<TerminalNode>();
      //  System.out.println("scope in eval: " + scope);
				scope++;
				//System.out.println("mem in eval: " + memory);
        funcy.call(args, scopes, scope, memory, linker);

        return 0.0;
    }

    @Override
    public Double visitStat_block(CalculatorParser.Stat_blockContext ctx) {

			List<CalculatorParser.Top_statContext> topstats =  ctx.top_stat();

			Double value = 0.0;
		//	System.out.println(memory);
			//System.out.println("------");
			for(CalculatorParser.Top_statContext topstat : topstats) {
					
							try {
								value = this.visit(topstat);
							} catch (Error e) {

							}

							//return value;

			}

			return 0.0;
    }


}
