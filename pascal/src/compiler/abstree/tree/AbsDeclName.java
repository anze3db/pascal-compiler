package compiler.abstree.tree;

import compiler.abstree.AbsVisitor;

/**
 * Deklaracije: ime.
 */
public class AbsDeclName extends AbsDecl {

	/* Ime. */
	public String name;
	public boolean single = false;
	
	public AbsDeclName(String name) {
		this.name = name;
	}
	
	public void accept(AbsVisitor visitor) {
		visitor.visit(this);
	}

}
