import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import tl.antlr4.TLParser.ExpressionContext;


class Functions {
	private List<String> params;
	private ParserRuleContext ctx;

	Functions(List<String> params, ParserRuleContext ctx) {
		this.params = params;
		this.ctx = ctx;
	}

	ParserRuleContext getCtx() {
		return this.ctx();
	}
	// public Double invoke(List<ExpressionContext> params, Map<String, Function> functions, Scope scope) {
  //       if (params.size() != this.params.size()) {
  //           throw new RuntimeException("Illegal Function call");
  //       }
  //       Scope scopeNext = new Scope(null); // create function scope
	//
  //       EvalVisitor evalVisitor = new EvalVisitor(scope, functions);
  //       for (int i = 0; i < this.params.size(); i++) {
  //           TLValue value = evalVisitor.visit(params.get(i));
  //           scopeNext.assignParam(this.params.get(i).getText(), value);
  //       }
  //       EvalVisitor evalVistorNext = new EvalVisitor(scopeNext,functions);
	//
  //       Double ret = 0.0;
  //       try {
  //       	evalVistorNext.visit(this.block);
  //       } catch (Double returnValue) {
  //       	ret = returnValue;
  //       }
  //       return ret;
  //   }
}
