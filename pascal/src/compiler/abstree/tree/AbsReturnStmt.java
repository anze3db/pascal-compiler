package compiler.abstree.tree;

import compiler.abstree.AbsVisitor;

public class AbsReturnStmt extends AbsStmt{

	@Override
	public void accept(AbsVisitor visitor) {
		visitor.visit(this);
	}


}
