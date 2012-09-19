package compiler.imcode;

import java.io.*;

import compiler.report.Report;

public class ImcMOVE extends ImcStmt {

	/** Ponor.  */
	public ImcExpr dst;

	/** Izvor.  */
	public ImcExpr src;

	public boolean single;
	public boolean accessed = false;
	
	public ImcMOVE(ImcExpr dst, ImcExpr src) {
		this.dst = dst;
		this.src = src;
		this.single = false;
	}
	
	public ImcMOVE(ImcExpr dst, ImcExpr src, boolean single2) {
		this.dst = dst;
		this.src = src;
		this.single = single2;
	}

	@Override
	public void toXML(PrintStream xml) {
		String single = "";
		if(this.single){
			single = " TRUE";
		}
		xml.print("<imcnode kind=\"MOVE "+single+"\">\n");
		dst.toXML(xml);
		src.toXML(xml);
		xml.print("</imcnode>\n");
	}

	@Override
	public ImcSEQ linear() {
		ImcSEQ lin = new ImcSEQ();
		ImcESEQ dst = this.dst.linear();
		ImcESEQ src = this.src.linear();
		lin.stmts.addAll(((ImcSEQ)dst.stmt).stmts);
		lin.stmts.addAll(((ImcSEQ)src.stmt).stmts);
		if(this.single && this.accessed){
			Report.error("Variable assigned twice", 1);
		}
		this.accessed = true;
		lin.stmts.add(new ImcMOVE(dst.expr, src.expr));
		return lin;
	}

}
