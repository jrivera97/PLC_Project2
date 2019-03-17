import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class EvalVisitor extends CalculatorBaseVisitor<Double> {

/* Storage to save variables */
private HashMap<String, Double> memory = new HashMap<String, Double>();
Scanner sc = new Scanner(System.in);

    @Override
    public Double visitTopExpr(CalculatorParser.TopExprContext ctx) {
        Double value = this.visit(ctx.expr());
        if (value % 1 == 0) {
            System.out.println(value.intValue());
        }
        else {
            System.out.println(value);
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
        }
        return 1.0;
    }

		@Override
		public Double visitComparisonExpr(CalculatorParser.ComparisonExprContext ctx) {
			Double left = this.visit(ctx.expr(0));
			Double right = this.visit(ctx.expr(1));

			switch (ctx.op.getType()) {
					case CalculatorParser.LESS:
							return left < right ? 1.0 : 0.0;
					case CalculatorParser.LESS_EQ:
							return left <= right ? 1.0 : 0.0;
					case CalculatorParser.GREATER:
							return left > right ? 1.0 : 0.0;
					case CalculatorParser.GREATER_EQ:
							return left >= right ? 1.0 : 0.0;
					case CalculatorParser.COMPARE:
							return left == right ? 1.0 : 0.0;

					default:
							throw new RuntimeException("unknown operator");
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
            System.out.println(value);
            Boolean toEvaluate = (value != 0) ? true : false;

            if(toEvaluate) {
                evaluated = true;
                // evaluate this block whose expr==true
                this.visit(condition.stat_block());
            }
        }

        if(!evaluated && ctx.stat_block() != null) {
            // evaluate the else-stat_block (if present == not null)
            this.visit(ctx.stat_block());
        }

        return 0.0;
    }
/*
    // while override
    @Override
    public Double visitWhile_stat(CalculatorParser.While_statContext ctx) {

        Double value = this.visit(ctx.expr());

        while(value.asBoolean()) {

            // evaluate the code block
            this.visit(ctx.stat_block());

            // evaluate the expression
            value = this.visit(ctx.expr());
        }

        return Double.VOID;
    }
    */
}
