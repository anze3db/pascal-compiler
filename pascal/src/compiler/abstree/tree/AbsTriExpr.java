package compiler.abstree.tree;

import compiler.abstree.AbsVisitor;

public class AbsTriExpr extends AbsValExpr {

	
	public AbsValExpr condition;
	public AbsValExpr valueTrue;
	public AbsValExpr valueFalse;

	public AbsTriExpr(AbsValExpr cond, AbsValExpr v1, AbsValExpr v2) {
		condition = cond;
		valueTrue = v1;
		valueFalse = v2;
	}

	@Override
	public void accept(AbsVisitor visitor) {
		visitor.visit(this);
	}

}
