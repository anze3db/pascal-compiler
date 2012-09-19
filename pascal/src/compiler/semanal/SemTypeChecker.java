package compiler.semanal;

import compiler.abstree.AbsVisitor;
import compiler.abstree.tree.AbsAlloc;
import compiler.abstree.tree.AbsArrayType;
import compiler.abstree.tree.AbsAssignStmt;
import compiler.abstree.tree.AbsAtomConst;
import compiler.abstree.tree.AbsAtomType;
import compiler.abstree.tree.AbsBinExpr;
import compiler.abstree.tree.AbsBlockStmt;
import compiler.abstree.tree.AbsCallExpr;
import compiler.abstree.tree.AbsConstDecl;
import compiler.abstree.tree.AbsDecl;
import compiler.abstree.tree.AbsDeclName;
import compiler.abstree.tree.AbsDecls;
import compiler.abstree.tree.AbsExprStmt;
import compiler.abstree.tree.AbsForStmt;
import compiler.abstree.tree.AbsFunDecl;
import compiler.abstree.tree.AbsIfStmt;
import compiler.abstree.tree.AbsNilConst;
import compiler.abstree.tree.AbsPointerType;
import compiler.abstree.tree.AbsProcDecl;
import compiler.abstree.tree.AbsProgram;
import compiler.abstree.tree.AbsRecordType;
import compiler.abstree.tree.AbsStmt;
import compiler.abstree.tree.AbsStmts;
import compiler.abstree.tree.AbsTypeDecl;
import compiler.abstree.tree.AbsTypeName;
import compiler.abstree.tree.AbsUnExpr;
import compiler.abstree.tree.AbsValExpr;
import compiler.abstree.tree.AbsValExprs;
import compiler.abstree.tree.AbsValName;
import compiler.abstree.tree.AbsVarDecl;
import compiler.abstree.tree.AbsWhileStmt;
import compiler.report.Report;
import compiler.semanal.type.SemArrayType;
import compiler.semanal.type.SemAtomType;
import compiler.semanal.type.SemPointerType;
import compiler.semanal.type.SemRecordType;
import compiler.semanal.type.SemSubprogramType;
import compiler.semanal.type.SemType;

public class SemTypeChecker implements AbsVisitor {

	public boolean error;

	@Override
	public void visit(AbsAlloc acceptor) {
		acceptor.type.accept(this);
		SemType semType = SemDesc.getActualType(acceptor.type);
		if (semType != null
				&& !semType.coercesTo(new SemAtomType(SemAtomType.INT))){
			Report.warning("Type does not coerce to integer.",
					acceptor.begLine, acceptor.begColumn);
			error = true;
		}

		SemDesc.setActualType(acceptor, semType);
	}

	@Override
	public void visit(AbsArrayType acceptor) {
		acceptor.type.accept(this);
		acceptor.hiBound.accept(this);
		acceptor.loBound.accept(this);
		SemType semType = SemDesc.getActualType(acceptor.type);
		
		if (semType == null) {
			error = true; Report.warning("Array type failed", acceptor.begLine,
					acceptor.endLine);
			return;
		}
		try {
			int lb = SemDesc.getActualConst(acceptor.loBound);
			int hb = SemDesc.getActualConst(acceptor.hiBound);
			
			if (lb <= 0 || hb <= 0){
				error = true;
				Report.warning("Negative array bound", acceptor.begLine,
						acceptor.endLine);
			}
			else if (lb > hb){
				error = true;
				Report.warning("Lowerbound greater than higherbound",
						acceptor.begLine, acceptor.endLine);
			}
			SemType rt = new SemArrayType(semType, lb, hb);
			SemDesc.setActualType(acceptor, rt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(AbsAssignStmt acceptor) {
		acceptor.dstExpr.accept(this);
		acceptor.srcExpr.accept(this);

		SemType ltype = SemDesc.getActualType(acceptor.dstExpr);
		SemType rtype = SemDesc.getActualType(acceptor.srcExpr);

		if (ltype instanceof SemPointerType) {
			ltype = ((SemPointerType) ltype).type;
			if (rtype instanceof SemPointerType) {
				rtype = ((SemPointerType) rtype).type;
			} else {
				error = true; Report.warning("Left and right types do not match",
						acceptor.begLine, acceptor.begColumn);
				return;
			}
		}
	}

	@Override
	public void visit(AbsAtomConst acceptor) {
		switch (acceptor.type) {
		case AbsAtomConst.BOOL:
			SemDesc.setActualType(acceptor, new SemAtomType(SemAtomType.BOOL));
			break;
		case AbsAtomConst.CHAR:
			SemDesc.setActualType(acceptor, new SemAtomType(SemAtomType.CHAR));
			break;
		case AbsAtomConst.INT:
			SemDesc.setActualType(acceptor, new SemAtomType(SemAtomType.INT));
			break;
		}
	}

	@Override
	public void visit(AbsAtomType acceptor) {
		switch (acceptor.type) {
		case AbsAtomType.BOOL:
			SemDesc.setActualType(acceptor, new SemAtomType(SemAtomType.BOOL));
			break;
		case AbsAtomType.CHAR:
			SemDesc.setActualType(acceptor, new SemAtomType(SemAtomType.CHAR));
			break;
		case AbsAtomType.INT:
			SemDesc.setActualType(acceptor, new SemAtomType(SemAtomType.INT));
			break;
		}

	}

	@Override
	public void visit(AbsBinExpr acceptor) {
		SemType ltype = null, rtype = null;
		switch (acceptor.oper) {
		case AbsBinExpr.ARRACCESS:
			
			acceptor.fstExpr.accept(this);
			acceptor.sndExpr.accept(this);
			ltype = SemDesc.getActualType(acceptor.fstExpr);
			rtype = SemDesc.getActualType(acceptor.sndExpr);
			if (ltype != null && ltype instanceof SemArrayType) {
				ltype = ((SemArrayType) ltype).type;
			}
			if (rtype == null
					|| !rtype.coercesTo(new SemAtomType(SemAtomType.INT))){
				error = true; Report.warning("Wrong index type.", acceptor.begLine,
						acceptor.endColumn);
			}
			SemDesc.setActualType(acceptor, ltype);
			break;
		case AbsBinExpr.RECACCESS:
			acceptor.fstExpr.accept(this);
			String fieldName = null;
			
			if(acceptor.sndExpr instanceof AbsValName)
				fieldName = ((AbsValName)acceptor.sndExpr).name;
			
			ltype = SemDesc.getActualType(acceptor.fstExpr);
			if(ltype != null && ltype instanceof SemRecordType){	
				
				SemRecordType rt = (SemRecordType)ltype;
				rtype = rt.getFieldType(fieldName);
			}			
							
			SemDesc.setActualType(acceptor.sndExpr, rtype);
			SemDesc.setActualType(acceptor, rtype);
			break;
		default:
			acceptor.fstExpr.accept(this);
			acceptor.sndExpr.accept(this);
			ltype = SemDesc.getActualType(acceptor.fstExpr);
			rtype = SemDesc.getActualType(acceptor.sndExpr);


			if (ltype != null && !ltype.coercesTo(rtype)){
				error = true; Report.warning("Uncompatible types.", acceptor.begLine,
						acceptor.endColumn);
			}
			switch (acceptor.oper) {
			case AbsBinExpr.ADD:
			case AbsBinExpr.SUB:
			case AbsBinExpr.MUL:
			case AbsBinExpr.DIV:
				
				if (ltype == null || rtype == null
						|| !ltype.coercesTo(new SemAtomType(SemAtomType.INT))
						|| !rtype.coercesTo(new SemAtomType(SemAtomType.INT))){
					error = true; Report.warning("Arithmetic arguments not of type integer.",
							acceptor.begLine, acceptor.endColumn);
				}
				SemDesc.setActualType(acceptor, ltype);
				break;
			case AbsBinExpr.AND:
			case AbsBinExpr.OR:
				if (ltype == null || rtype == null
						|| !ltype.coercesTo(new SemAtomType(SemAtomType.BOOL))
						|| !rtype.coercesTo(new SemAtomType(SemAtomType.BOOL))){
					error = true; Report.warning("Arithmetic arguments not of type integer.",
							acceptor.begLine, acceptor.endColumn);
				}
				SemDesc.setActualType(acceptor, ltype);
				break;
			case AbsBinExpr.EQU:
			case AbsBinExpr.NEQ:
			case AbsBinExpr.LTH:
			case AbsBinExpr.GTH:
			case AbsBinExpr.LEQ:
			case AbsBinExpr.GEQ:
				if (ltype instanceof SemPointerType) {
					ltype = ((SemPointerType) ltype).type;
					if (rtype instanceof SemPointerType) {
						rtype = ((SemPointerType) rtype).type;
					} else {
						error = true; Report.warning("Type mismatch.", acceptor.begLine,
								acceptor.endColumn);
						break;
					}
				}

				if (ltype != null && !ltype.coercesTo(rtype)){
					error = true; Report.warning("Type mismatch.", acceptor.begLine,
							acceptor.endColumn);
				}
				SemDesc.setActualType(acceptor, new SemAtomType(
						SemAtomType.BOOL));
				break;
			default:
				SemDesc.setActualType(acceptor, ltype);
				break;
			}
		}

	}

	@Override
	public void visit(AbsBlockStmt acceptor) {
		acceptor.stmts.accept(this);
	}

	@Override
	public void visit(AbsCallExpr acceptor) {
		acceptor.name.accept(this);
		acceptor.args.accept(this);
		SemType st = SemDesc.getActualType(acceptor.name);
		if (st != null && st instanceof SemSubprogramType) {
			SemSubprogramType spt = (SemSubprogramType) st;
			SemDesc.setActualType(acceptor, spt.getResultType());
		}
	}

	@Override
	public void visit(AbsConstDecl acceptor) {
		acceptor.value.accept(this);
		SemType st = SemDesc.getActualType(acceptor.value);
		SemDesc.setActualType(acceptor, st);

	}

	@Override
	public void visit(AbsDeclName acceptor) {

	}

	@Override
	public void visit(AbsDecls acceptor) {
		for (AbsDecl decl : acceptor.decls)
			decl.accept(this);

	}

	@Override
	public void visit(AbsExprStmt acceptor) {
		acceptor.expr.accept(this);

	}

	@Override
	public void visit(AbsForStmt acceptor) {
		acceptor.name.accept(this);
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);

		SemType cType1 = SemDesc.getActualType(acceptor.loBound);
		SemType cType2 = SemDesc.getActualType(acceptor.hiBound);
		if (cType1 == null || cType2 == null
				|| !cType1.coercesTo(new SemAtomType(SemAtomType.INT))
				|| !cType2.coercesTo(new SemAtomType(SemAtomType.INT))){
			error = true; Report.warning("For bounds must be integers.", acceptor.begLine,
					acceptor.begColumn);
		}
		acceptor.stmt.accept(this);

	}

	@Override
	public void visit(AbsFunDecl acceptor) {
		acceptor.name.accept(this);
		acceptor.pars.accept(this);
		acceptor.type.accept(this);
		acceptor.decls.accept(this);
		SemType ft = SemDesc.getActualType(acceptor.type);
		SemSubprogramType spt = new SemSubprogramType(ft);
		for (AbsDecl d : acceptor.pars.decls) {
			SemType pt = SemDesc.getActualType(d);
			spt.addParType(pt);
		}
		SemDesc.setActualType(acceptor, spt);
		acceptor.stmt.accept(this);

	}

	@Override
	public void visit(AbsIfStmt acceptor) {
		acceptor.cond.accept(this);

		SemType cType = SemDesc.getActualType(acceptor.cond);
		if (cType != null
				&& !cType.coercesTo(new SemAtomType(SemAtomType.BOOL))){
			error = true; Report.warning("If expression not a bolean.", acceptor.begLine,
					acceptor.begColumn);
		}
		acceptor.thenStmt.accept(this);
		acceptor.elseStmt.accept(this);

	}

	@Override
	public void visit(AbsNilConst acceptor) {
		SemDesc.setActualType(acceptor, new SemAtomType(SemAtomType.VOID));
	}

	@Override
	public void visit(AbsPointerType acceptor) {
		acceptor.type.accept(this);
		SemType st = SemDesc.getActualType(acceptor.type);
		SemDesc.setActualType(acceptor, new SemPointerType(st));
	}

	@Override
	public void visit(AbsProcDecl acceptor) {
		acceptor.pars.accept(this);
		acceptor.decls.accept(this);

		SemSubprogramType spt = new SemSubprogramType(new SemAtomType(
				SemAtomType.VOID));
		for (AbsDecl d : acceptor.pars.decls) {
			SemType pt = SemDesc.getActualType(d);
			spt.addParType(pt);
		}
		SemDesc.setActualType(acceptor, spt);

		acceptor.stmt.accept(this);

	}

	@Override
	public void visit(AbsProgram acceptor) {
		SemConstEvaluator constVisitor = new SemConstEvaluator();
		SemPrivChecker pc = new SemPrivChecker();
		acceptor.accept(pc);
		acceptor.accept(constVisitor);
		acceptor.decls.accept(this);
		acceptor.stmt.accept(this);
	}

	@Override
	public void visit(AbsRecordType acceptor) {
		SemRecordType rt = new SemRecordType();
		SemDesc.setActualType(acceptor, rt);
		acceptor.fields.accept(this);

		for (AbsDecl d : acceptor.fields.decls) {
			SemType semType = SemDesc.getActualType(d);
			AbsDeclName name = getDeclName(d);

			if (semType == null) {
				error = true; Report.warning(String.format("Declaration '%s' has no type",
						name));
			} else {
				rt.addField(name, semType);
			}
		}

	}

	@Override
	public void visit(AbsStmts acceptor) {
		for (AbsStmt stmt : acceptor.stmts) {
			stmt.accept(this);
		}

	}

	@Override
	public void visit(AbsTypeDecl acceptor) {
		acceptor.type.accept(this);
		SemType t = SemDesc.getActualType(acceptor.type);
		SemDesc.setActualType(acceptor, t);
	}

	@Override
	public void visit(AbsTypeName acceptor) {
		if (acceptor.error)
			return;

		AbsDecl decl = SemDesc.getNameDecl(acceptor);
		SemType semType = SemDesc.getActualType(decl);
		SemDesc.setActualType(acceptor, semType);
	}

	@Override
	public void visit(AbsUnExpr acceptor) {
		acceptor.expr.accept(this);
		SemType type = SemDesc.getActualType(acceptor.expr);

		switch (acceptor.oper) {
		case AbsUnExpr.VAL:
			try {
				SemPointerType spt = (SemPointerType) type;
				SemDesc.setActualType(acceptor, spt.type);
			} catch (Exception e) {
			}
			break;
		case AbsUnExpr.MEM:
			SemPointerType spt = new SemPointerType(type);
			SemDesc.setActualType(acceptor, spt);
			break;
		case AbsUnExpr.NOT:
			SemDesc.setActualType(acceptor, type);
			break;
		default:
			SemDesc.setActualType(acceptor, type);
			break;
		}

	}

	@Override
	public void visit(AbsValExprs acceptor) {
		for (AbsValExpr expr : acceptor.exprs)
			expr.accept(this);
	}

	@Override
	public void visit(AbsValName acceptor) {
		AbsDecl decl = SemDesc.getNameDecl(acceptor);
		if(decl == null){			
			return;
		}
		SemType semType = SemDesc.getActualType(decl);
		if (semType == null) {
			decl.accept(this);
			semType = SemDesc.getActualType(decl);
		}

		SemDesc.setActualType(acceptor, semType);

	}

	@Override
	public void visit(AbsVarDecl acceptor) {
		acceptor.type.accept(this);
		SemType st = SemDesc.getActualType(acceptor.type);
		SemDesc.setActualType(acceptor, st);
	}

	@Override
	public void visit(AbsWhileStmt acceptor) {
		acceptor.cond.accept(this);
		acceptor.stmt.accept(this);

	}

	private AbsDeclName getDeclName(AbsDecl d) {

		if (d instanceof AbsConstDecl)
			return ((AbsConstDecl) d).name;
		else if (d instanceof AbsFunDecl)
			return ((AbsFunDecl) d).name;
		else if (d instanceof AbsProcDecl)
			return ((AbsProcDecl) d).name;
		else if (d instanceof AbsTypeDecl)
			return ((AbsTypeDecl) d).name;
		else if (d instanceof AbsVarDecl)
			return ((AbsVarDecl) d).name;
		else
			return null;
	}
}
