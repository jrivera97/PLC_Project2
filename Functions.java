import java.util.List;
import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

//import CalculatorParser.antlr4.CalculatorParser.ExpressionContext;
//import ParserRuleContext;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

class Functions {
	private List<TerminalNode> params;
	private CalculatorParser.Function_defContext ctx;

	Functions(List<TerminalNode> params, CalculatorParser.Function_defContext ctx) {
		this.params = params;
		this.ctx = ctx;
	}

	CalculatorParser.Function_defContext getCtx() {
		return this.ctx;
	}


	public Double call(List<TerminalNode> args, HashMap<String, Integer> scopes, int scope, HashMap<String, Double> memory, HashMap<String, Functions> linker) {

    //HashMap<String, Double> function_mem = new HashMap<String, Double>();

    int compare = Integer.compare(args.size(), params.size());
    if (compare < 0) {
      throw new RuntimeException("too few arguments");
    }
    else if (compare > 0) {
      throw new RuntimeException("too many arguments");
    }

		String id = this.ctx.ID().getText();

    EvalVisitor evalVisitor = new EvalVisitor(memory, linker, scopes, scope);
	//	System.out.println("mem in func: "+ memory);
    for (int i = 0; i < params.size(); i++) {
      //System.out.println("helloo");
      Double value = Double.parseDouble(args.get(i).getText());
      //System.out.println("Key: " + params.get(i).getText() + " Value: " + value);
			//scope++;
			if (memory.containsKey(id)) {
					// variable already defined
					memory.replace(params.get(i).getText(), Double.parseDouble(args.get(i).getText()));
			}
			else {
					// variable not defined, create a spot in memory
					memory.put(params.get(i).getText(), Double.parseDouble(args.get(i).getText()));
					scopes.put(params.get(i).getText(), scope);
			}

			//System.out.println("mem in funfor: " + memory);
			//System.out.println("On the Functions side: ");
		//	System.out.println("mem Key: " + params.get(i).getText() + " mem val: " + Double.parseDouble(args.get(i).getText()));
			//System.out.println(scope);

    }
    EvalVisitor evalVisitorNext = new EvalVisitor(memory, linker, scopes, scope);
		//System.out.println("mem before end func" + memory);
    Double ret = evalVisitorNext.visitStat_block(this.ctx.stat_block());

    //System.out.println(ret);
    return ret;
  }
}
