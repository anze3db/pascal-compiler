package compiler.abstree.tree;

import compiler.abstree.AbsVisitor;

/**
 * Deklaracije: ime.
 */
public class AbsDeclName extends AbsDecl {

	public boolean positive = false;
	
	/* Ime. */
	public String name;
	
	public AbsDeclName(String name) {
		this.name = name;
	}
	
	public void accept(AbsVisitor visitor) {
		visitor.visit(this);
	}

}
