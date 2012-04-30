package compiler.semanal;
import compiler.abstree.*;
import compiler.abstree.tree.*;
import compiler.report.Report;


public class SemNameResolver1 implements AbsVisitor {

	public boolean error;

	@Override
	public void visit(AbsAlloc acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsArrayType acceptor) {
		if(acceptor.error){
			return;
		}
	
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);		
		
	}

	@Override
	public void visit(AbsAssignStmt acceptor) {
		if(acceptor.error){
			return;
		}
	
		acceptor.dstExpr.accept(this);
		acceptor.srcExpr.accept(this);
		
	}

	@Override
	public void visit(AbsAtomConst acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsAtomType acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsBinExpr acceptor) {
		if(acceptor.error){
			return;
		}
	
			
		acceptor.fstExpr.accept(this);
		
		if(acceptor.oper != AbsBinExpr.RECACCESS)	
			acceptor.sndExpr.accept(this);
		
		
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
		if(acceptor.error){
			return;
		}
		acceptor.name.accept(this);
		acceptor.args.accept(this);		
	}

	@Override
	public void visit(AbsConstDecl acceptor) {		
		if(acceptor.error){
			return;
		}
		
		acceptor.name.accept(this);
		acceptor.value.accept(this);
		
	}

	@Override
	public void visit(AbsDeclName acceptor) {
		if(acceptor.error){
			return;
		}
		
		AbsDecl decl = SemTable.fnd(acceptor.name);
		
		if(decl != null){
			SemDesc.setNameDecl(acceptor, decl);			
		}
		else
			Report.warning("Nedeklariran identifikator: " + acceptor.name);	
		
		
	}

	@Override
	public void visit(AbsDecls acceptor) {
		if(acceptor.error){
			return;
		}
		//1. prelet - napolni SemTable
		for (AbsDecl d: acceptor.decls) {
			String name = getDeclName(d);
			
			try {			
				SemTable.ins(name, d);								
			}
			catch (Exception e) {	
				String type = getDeclType(d);
				Report.warning("Napaèna dvojna deklaracija " + type + ": " + name);
			}				
		}
			
		//2. prelet - povezovanje deklaracij z uporabo
		for (AbsDecl d: acceptor.decls) {
			d.accept(this);		
		}
	
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
	
		acceptor.name.accept(this);
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);
		acceptor.stmt.accept(this);
		
	}

	@Override
	public void visit(AbsIfStmt acceptor) {
		if(acceptor.error){
			return;
		}
	
		acceptor.cond.accept(this);
		acceptor.thenStmt.accept(this);
		acceptor.elseStmt.accept(this);
		
	}

	@Override
	public void visit(AbsNilConst acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsPointerType acceptor) {
		if(acceptor.error){
			return;
		}
		acceptor.type.accept(this);
		
	}

	@Override
	public void visit(AbsProcDecl acceptor) {
		if(acceptor.error){
			return;
		}
		acceptor.name.accept(this);
		
		SemTable.newScope();
		acceptor.pars.accept(this);
		acceptor.decls.accept(this);		
		acceptor.stmt.accept(this);
		SemTable.oldScope();
		
	}

	@Override
	public void visit(AbsFunDecl acceptor){
		if(acceptor.error){
			return;
		}	
		acceptor.name.accept(this);
		
		SemTable.newScope();
		acceptor.pars.accept(this);
		acceptor.decls.accept(this);
		acceptor.stmt.accept(this);
		SemTable.oldScope();
	
		acceptor.type.accept(this);
		
	}
	
	@Override
	public void visit(AbsProgram acceptor) {
		
		if(acceptor.error){
			return;
		}		
			
		String progName = acceptor.name.name;
		
		try{		
			SemTable.ins(progName, acceptor.name);
		}
		catch(Exception ex){
			Report.warning("Ime programa ne sme biti enako imenom vgrajenih funkcij: " + progName, 1, 1);
		}
		
		SemTable.newScope();
		//1. prelet
		acceptor.decls.accept(this);
		//2. prelet
		acceptor.stmt.accept(this);
		SemTable.oldScope();
		
		
		
	}

	@Override
	public void visit(AbsRecordType acceptor) {
		if(acceptor.error){
			return;
		}	
						
		SemTable.newScope();
		acceptor.fields.accept(this);
		SemTable.oldScope();		
		
		for(AbsDecl d: acceptor.fields.decls)
			SemDesc.setScope(d, 0);
		
	}

	@Override
	public void visit(AbsStmts acceptor) {
		
		if(acceptor.error){
			return;
		}	
		
		for (AbsStmt s: acceptor.stmts) {
			
			s.accept(this);	
			/*try {	
						
				
				}
			catch (Exception e) {			
				String declType = getDeclType(d);			
				Report.warning("Napaèna dvojna deklaracija " + declType + ": " + declName, d);
			}
			*/
			
		}
		
		
	}

	@Override
	public void visit(AbsTypeDecl acceptor) {
		if(acceptor.error){
			return;
		}
		acceptor.name.accept(this);
		acceptor.type.accept(this);		
	}

	@Override
	public void visit(AbsTypeName acceptor) {
		if(acceptor.error){
			return;
		}
		
		AbsDecl decl = SemTable.fnd(acceptor.name);
		
		if(decl != null){
			SemDesc.setNameDecl(acceptor, decl);
			
		}
		else
			Report.warning("Nedeklariran tip: " + acceptor.name);	
		
		
	}

	@Override
	public void visit(AbsUnExpr acceptor) {
		if(acceptor.error){
			return;
		}
	
		acceptor.expr.accept(this);
		
	}

	@Override
	public void visit(AbsValExprs acceptor) {
		if(acceptor.error){
			return;
		}		
	
		for(AbsValExpr e : acceptor.exprs){
			e.accept(this);
		}
				
		
	}

	@Override
	public void visit(AbsValName acceptor) {
		
		if(acceptor.error){
			return;
		}
		
		AbsDecl decl = SemTable.fnd(acceptor.name);
		
		if(decl != null){
			SemDesc.setNameDecl(acceptor, decl);			
		}
		else
			Report.warning("Nedeklariran identifikator: " + acceptor.name);	
		
	
		
	}

	@Override
	public void visit(AbsVarDecl acceptor) {
		if(acceptor.error){
			return;
		}
		
		acceptor.name.accept(this);
		acceptor.type.accept(this);
		
	}

	@Override
	public void visit(AbsWhileStmt acceptor) {
		if(acceptor.error){
			return;
		}
	
		acceptor.cond.accept(this);
		acceptor.stmt.accept(this);
		
	}
	

	private String getDeclType(AbsDecl d){
		if(d instanceof AbsConstDecl)
			return "konstante";
		else if(d instanceof AbsFunDecl)
			return "funkcije";
		else if(d instanceof AbsProcDecl)
			return "procedure";
		else if(d instanceof AbsTypeDecl)
			return "tipa";
		else if(d instanceof AbsVarDecl)
			return "spremenljivke";
		else
			return "?";
	}
	
	private String getDeclName(AbsDecl d){

		if(d instanceof AbsConstDecl)
			return ((AbsConstDecl)d).name.name;
		else if(d instanceof AbsFunDecl)
			return ((AbsFunDecl)d).name.name;
		else if(d instanceof AbsProcDecl)
			return ((AbsProcDecl)d).name.name;
		else if(d instanceof AbsTypeDecl)
			return ((AbsTypeDecl)d).name.name;
		else if(d instanceof AbsVarDecl)
			return ((AbsVarDecl)d).name.name;
		else
			return "?";
	}


}