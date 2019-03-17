import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class EvalVisitor extends CalculatorBaseVisitor<Double> {

/* Storage to save variables */
private HashMap<String, Double> memory = new HashMap<String, Double>();
Scanner sc = new Scanner(System.in);

    @Override
    public  Double visitIntAtom(CalculatorParser.IntAtomContext ctx){
            return Double.valueOf(ctx.getText());
    }

    @Override
    public Double visitMathExpr(CalculatorParser.MathExprContext ctx) {

        Double left = this.visit(ctx.expr(0));
        Double right = this.visit(ctx.expr(1));

        switch (ctx.op.getType()) {
            case (CalculatorParser.ADD):
                System.out.println(left+right);
                return left + right;
            case (CalculatorParser.SUB):
                return left - right;
        }
        return 1.0;
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

