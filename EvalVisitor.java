import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class EvalVisitor extends CalculatorBaseVisitor<Double> {

/* Storage to save variables */
HashMap<String, HashMap<Integer, Double>> memory = new HashMap<String, HashMap<Integer, Double>>();
Scanner sc = new Scanner(System.in);

    @Override
    public Double visitIf_stat(CalculatorParser.If_statContext ctx) {

        List<CalculatorParser.Condition_blockContext> conditions =  ctx.condition_block();

        boolean evaluated = false;

        for(CalculatorParser.Condition_blockContext condition : conditions) {

            this.visit(condition.expr());
            System.out.println(condition.expr().i);
            Boolean toEvaluate = (condition.expr().i != 0) ? true : false;

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
    
        return 1.0;
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

