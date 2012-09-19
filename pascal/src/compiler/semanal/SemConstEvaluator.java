package compiler.semanal;

import compiler.abstree.*;
import compiler.abstree.tree.*;
import compiler.report.Report;

public class SemConstEvaluator implements AbsVisitor{

	public boolean error;
	
	@Override
	public void visit(AbsAlloc acceptor) {
	}

	@Override
	public void visit(AbsArrayType acceptor) {
		if (acceptor.error)	
			return; 
		
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);
		
	}

	@Override
	public void visit(AbsAssignStmt acceptor) {
		if (acceptor.error)
			return; 
		
		acceptor.dstExpr.accept(this);
		acceptor.srcExpr.accept(this);
		
	}

	@Override
	public void visit(AbsAtomConst acceptor) {
		if (acceptor.error)
			return; 
		
		switch (acceptor.type) {
		case AbsAtomConst.BOOL:
			int value = 0;
			if(acceptor.value.equals("true"))
				value = 1;
			SemDesc.setActualConst(acceptor, value);
			break;
		case AbsAtomConst.CHAR:
			SemDesc.setActualConst(acceptor, (int)acceptor.value.charAt(0));
			break;
		case AbsAtomConst.INT:
			SemDesc.setActualConst(acceptor, Integer.parseInt(acceptor.value));
			break;
		}
		
		
		
	}

	@Override
	public void visit(AbsAtomType acceptor) {
		
	}

	@Override
	public void visit(AbsBinExpr acceptor) {
		if (acceptor.error)
			return; 
		
		acceptor.fstExpr.accept(this);
		acceptor.sndExpr.accept(this);		

		Integer fst = null, snd = null;
		try{
			if(acceptor.fstExpr instanceof AbsValName){
				AbsConstDecl cd = (AbsConstDecl) SemDesc.getNameDecl(acceptor.fstExpr);
				fst = SemDesc.getActualConst(cd);
			}
			else
				fst = SemDesc.getActualConst(acceptor.fstExpr);
			
			if(acceptor.sndExpr instanceof AbsValName){
				AbsConstDecl cd = (AbsConstDecl) SemDesc.getNameDecl(acceptor.sndExpr);
				snd = SemDesc.getActualConst(cd);
			}
			else
				snd = SemDesc.getActualConst(acceptor.sndExpr);
		}
		catch(Exception e){
			Report.warning("Napaka pri evaluaciji izraza!");
		}
		int val = 0;
		
		if(fst == null || snd == null)
			return;
		
		switch (acceptor.oper) {
		case AbsBinExpr.ADD:
			val = fst + snd;
			break;
		case AbsBinExpr.SUB:
			val = fst - snd;
			break;
		case AbsBinExpr.MUL:
			val = fst * snd;
			break;
		case AbsBinExpr.DIV:
			val = fst / snd;
			break;
		case AbsBinExpr.EQU:
			if(fst == snd)
				val = 1;
			else
				val = 0;
			break;
		case AbsBinExpr.NEQ:
			if(fst != snd)
				val = 1;
			else
				val = 0;
			break;
		case AbsBinExpr.LTH:
			if(fst < snd)
				val = 1;
			else
				val = 0;
			break;
		case AbsBinExpr.GTH:
			if(fst > snd)
				val = 1;
			else
				val = 0;
			break;
		case AbsBinExpr.LEQ:
			if(fst <= snd)
				val = 1;
			else
				val = 0;
			break;
		case AbsBinExpr.GEQ:
			if(fst >= snd)
				val = 1;
			else
				val = 0;
			break;
		case AbsBinExpr.AND:
			if(fst == 1 && snd == 1)
				val = 1;
			else
				val = 0;
			break;
		case AbsBinExpr.OR:
			if(fst == 1 || snd == 1)
				val = 1;
			else
				val = 0;
			break;
		}
		SemDesc.setActualConst(acceptor, val);
		
	}

	@Override
	public void visit(AbsBlockStmt acceptor) {
		if(acceptor.error){
			return;
		}
	
		acceptor.stmts.accept(this);
		
	}

	@Override
	public void visit(AbsCallExpr acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsConstDecl acceptor) {
		if (acceptor.error)
			return; 
				
		Integer val = SemDesc.getActualConst(acceptor.value);
		if(val == null){			
			acceptor.value.accept(this);
			val = SemDesc.getActualConst(acceptor.value);
		}			
		SemDesc.setActualConst(acceptor, val);
	
		
	}

	@Override
	public void visit(AbsDeclName acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsDecls acceptor) {
		if (acceptor.error)
			return; 
		
		for (AbsDecl decl : acceptor.decls) decl.accept(this);
		
	}

	@Override
	public void visit(AbsExprStmt acceptor) {
		if(acceptor.error){
			return;
		}
	
		acceptor.expr.accept(this);
		
	}

	@Override
	public void visit(AbsForStmt acceptor) {
		if(acceptor.error){
			return;
		}
	
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);
		acceptor.stmt.accept(this);
		
	}

	@Override
	public void visit(AbsFunDecl acceptor) {
	
		
		acceptor.pars.accept(this);
		acceptor.decls.accept(this);
		//acceptor.stmt.accept(this);
	
		acceptor.type.accept(this);
		
	}

	@Override
	public void visit(AbsIfStmt acceptor) {
		acceptor.cond.accept(this);
		acceptor.thenStmt.accept(this);
		acceptor.elseStmt.accept(this);
		
	}

	@Override
	public void visit(AbsNilConst acceptor) {
		if (acceptor.error)
			return; 
		
		SemDesc.setActualConst(acceptor, 0);
		
	}

	@Override
	public void visit(AbsPointerType acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsProcDecl acceptor) {
		if (acceptor.error)
			return;
		
		acceptor.pars.accept(this);
		acceptor.decls.accept(this);		
		//acceptor.stmt.accept(this);
		
	}

	@Override
	public void visit(AbsProgram acceptor) {
		if (acceptor.error)
			return; 
		
		//acceptor.name.accept(this);
		acceptor.decls.accept(this);
		//acceptor.stmt.accept(this);
		
	}

	@Override
	public void visit(AbsRecordType acceptor) {
		if(acceptor.error){
			return;
		}	
						
		acceptor.fields.accept(this);		
	}

	@Override
	public void visit(AbsStmts acceptor) {
		if(acceptor.error){
			return;
		}	
		/*
		for (AbsStmt s: acceptor.stmts) 			
			s.accept(this);	
		*/
	}

	@Override
	public void visit(AbsTypeDecl acceptor) {
		if (acceptor.error)	
			return; 
		
		acceptor.type.accept(this);	
		
		
		/*if (SemDesc.getActualConst(acceptor.type) == null) {
			acceptor.type.accept(this);
		}
		SemDesc.setActualConst(acceptor, SemDesc.getActualConst(acceptor.type) );
		
		if(acceptor.error){
			return;
		}*/	
		
	}

	@Override
	public void visit(AbsTypeName acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsUnExpr acceptor) {
		if (acceptor.error)
			return; 
		
		acceptor.expr.accept(this);		

		Integer fst = null;
		try{
			if(acceptor.expr instanceof AbsValName){
				AbsConstDecl cd = (AbsConstDecl) SemDesc.getNameDecl(acceptor.expr);
				fst = SemDesc.getActualConst(cd);
			}
			else
				fst = SemDesc.getActualConst(acceptor.expr);
			
		}
		catch(Exception e){
			Report.warning("Napaka pri evaluaciji izraza!");
		}
		int val = 0;
		
		if(fst == null)
			return;
		
		switch (acceptor.oper) {
		case AbsUnExpr.ADD:
			val = +fst;
			break;
		case AbsUnExpr.SUB:
			val = -fst;
			break;
		case AbsUnExpr.NOT:
			val = (fst + 1) % 2;
			break;
		}
		SemDesc.setActualConst(acceptor, val);
		
	}

	@Override
	public void visit(AbsValExprs acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsValName acceptor) {
		if (acceptor.error)
			return; 
		
		Integer val = null;
		AbsConstDecl cd = null;
		
		if(SemDesc.getNameDecl(acceptor) instanceof AbsConstDecl)
			cd = (AbsConstDecl)SemDesc.getNameDecl(acceptor);
		else{
			Report.warning("Vrednost konstante mora biti konstanta!");
			return;
		}
		
		val = SemDesc.getActualConst(cd);
	
		if(cd == null)
			Report.warning("Napaka pri evaluaciji izraza!");
		else if(val == null){
			cd.accept(this);
			val = SemDesc.getActualConst(cd);
		}
			
		
		SemDesc.setActualConst(acceptor, val);
		
	}

	@Override
	public void visit(AbsVarDecl acceptor) {
		/*if (acceptor.error)	return; 
		
		if (SemDesc.getActualConst(acceptor.type) == null) {
			acceptor.type.accept(this);
		}
		SemDesc.setActualConst(acceptor, SemDesc.getActualConst(acceptor.type) );*/
		
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsWhileStmt acceptor) {
		acceptor.cond.accept(this);
		acceptor.stmt.accept(this);
		
	}

	@Override
	public void visit(AbsTriExpr acceptor) {
		acceptor.condition.accept(this);
		acceptor.valueFalse.accept(this);
		acceptor.valueTrue.accept(this);
	}

}
