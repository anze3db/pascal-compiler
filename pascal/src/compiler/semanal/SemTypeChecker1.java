package compiler.semanal;
import java.util.LinkedList;

import compiler.abstree.*;
import compiler.abstree.tree.*;
import compiler.report.Report;
import compiler.semanal.type.SemArrayType;
import compiler.semanal.type.SemAtomType;
import compiler.semanal.type.SemPointerType;
import compiler.semanal.type.SemRecordType;
import compiler.semanal.type.SemSubprogramType;
import compiler.semanal.type.SemType;

public class SemTypeChecker1 implements AbsVisitor{

	public boolean error;
	private static LinkedList<AbsTree> currentCrossReferencedTypes = new LinkedList<AbsTree>();
	private AbsDecl currentFunDecl = null;

	public void visit(AbsAlloc acceptor) {
		if (acceptor.error)
			return; 
		
	
		acceptor.type.accept(this);		
		SemType semType = SemDesc.getActualType(acceptor.type);
		if(semType != null && !semType.coercesTo(new SemAtomType(SemAtomType.INT)))
			Report.warning("Tip izraza za dodeljevanje pomnilnika mora biti integer!");
			
		SemDesc.setActualType(acceptor, semType);
		
	}

	public void visit(AbsArrayType acceptor) {
		if (acceptor.error)
			return; 
			
		acceptor.type.accept(this);
		acceptor.hiBound.accept(this);
		acceptor.loBound.accept(this);
		SemType semType = SemDesc.getActualType(acceptor.type);
		
		if(semType == null){
			Report.warning("Napaka pri doloèanju tipa za polje!");
		}
		else{
			try{
				int lb = SemDesc.getActualConst(acceptor.loBound);
				int hb = SemDesc.getActualConst(acceptor.hiBound);
				if(lb <= 0 || hb <=0)
						Report.warning("Meje polj morajo biti pozitivna tevila tipa integer!");			
				else if(lb > hb)
					Report.warning("Sp. meja polja mora biti nija od zgornje!");	
				SemType rt = new SemArrayType(semType, lb, hb);			
				SemDesc.setActualType(acceptor, rt);
			}
			catch(Exception e){
				Report.warning("Meje polja morajo biti tipa integer!");			
			}
		}
		
		
	}

	public void visit(AbsAssignStmt acceptor) {
		if (acceptor.error)
			return; 
		
		acceptor.dstExpr.accept(this);
		acceptor.srcExpr.accept(this);
		
		SemType ltype = SemDesc.getActualType(acceptor.dstExpr);
		SemType rtype = SemDesc.getActualType(acceptor.srcExpr);
		
		if(ltype instanceof SemPointerType){
			ltype = ((SemPointerType)ltype).type;
			if(rtype instanceof SemPointerType){
				rtype = ((SemPointerType)rtype).type;
			}
			else{
				Report.warning("V prireditvenem stavku morata biti tipa leve in desne strani enaka!");
				return;
			}
		}
		else if(ltype instanceof SemSubprogramType){  //return value
			AbsDecl funDecl = SemDesc.getNameDecl(acceptor.dstExpr);
			SemType type = ((SemSubprogramType)ltype).getResultType();
			if(type instanceof SemAtomType && ((SemAtomType)type).type != SemAtomType.VOID && currentFunDecl != funDecl)  //preveri, èe se vraèa sploh vraèa vrednost lastni funkciji
				Report.warning("Napaka! Funkcija ne sme vraèati vrednost tujih funkcij!");
			if(!((SemSubprogramType)ltype).getResultType().coercesTo(rtype)){
				if(type instanceof SemAtomType && ((SemAtomType)type).type == SemAtomType.VOID)
					Report.warning("Napaka! Procedura ne more vraèati vrednosti!");
				else
					Report.warning("Napaka! Funkcija skua vrniti vrednost napaènega tipa!");
			}
		}
		else if(ltype == null || rtype == null || !ltype.coercesTo(rtype))
			Report.warning("V prireditvenem stavku morata biti tipa leve in desne strani enaka!");	
			
	}

	public void visit(AbsAtomConst acceptor) {
		if (acceptor.error)
			return; 
		
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

	public void visit(AbsAtomType acceptor) {
		if (acceptor.error) 
			return; 
		
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

	public void visit(AbsBinExpr acceptor) {
		if (acceptor.error)
			return; 	
		
		SemType ltype = null, rtype = null;
		
		switch (acceptor.oper) {	
		case AbsBinExpr.ARRACCESS:
			acceptor.fstExpr.accept(this);
			acceptor.sndExpr.accept(this);
			ltype = SemDesc.getActualType(acceptor.fstExpr);
			rtype = SemDesc.getActualType(acceptor.sndExpr);
			if(ltype != null && ltype instanceof SemArrayType){	
				ltype = ((SemArrayType)ltype).type;
			}
			else
				Report.warning("Levi argument operatorja [] mora biti tabela!");
			if(rtype == null || !rtype.coercesTo(new SemAtomType(SemAtomType.INT)))
				Report.warning("Napaèen tip indeksa za dostop do elementa polja!");
			SemDesc.setActualType(acceptor, ltype);
			break;
		case AbsBinExpr.RECACCESS:
			acceptor.fstExpr.accept(this);
			String fieldName = null;
			
			if(acceptor.sndExpr instanceof AbsValName)
				fieldName = ((AbsValName)acceptor.sndExpr).name;
			else
				Report.warning("Desni argument operatorja mora biti identifikator, ki ga zapis vsebuje!");
			
			ltype = SemDesc.getActualType(acceptor.fstExpr);
			if(ltype != null && ltype instanceof SemRecordType){	
				//poièi tip elementa v recordu
				SemRecordType rt = (SemRecordType)ltype;

			}			
			if(ltype != null && !(ltype instanceof SemRecordType))
				Report.warning("Levi argument operatorja . mora biti zapis!");
							
			SemDesc.setActualType(acceptor.sndExpr, rtype);
			SemDesc.setActualType(acceptor, rtype);
			break;
		default:
			acceptor.fstExpr.accept(this);
			acceptor.sndExpr.accept(this);
			ltype = SemDesc.getActualType(acceptor.fstExpr);
			rtype = SemDesc.getActualType(acceptor.sndExpr);
			
			if(ltype == null)
				Report.warning("Napaka! Ne najdem definicije tipa!");
			else if(ltype != null && !ltype.coercesTo(rtype))
				Report.warning("Napaka! Nezdruljiva tipa!");
			
			switch (acceptor.oper) {			
				case AbsBinExpr.ADD:
				case AbsBinExpr.SUB:
				case AbsBinExpr.MUL:
				case AbsBinExpr.DIV:					
					if(ltype == null || rtype == null || !ltype.coercesTo(new SemAtomType(SemAtomType.INT)) || !rtype.coercesTo(new SemAtomType(SemAtomType.INT)))
						Report.warning("Napaka! Aritmetiène operacije morajo biti tipa integer!");
					
					SemDesc.setActualType(acceptor, ltype);
					break;
				case AbsBinExpr.AND:
				case AbsBinExpr.OR:
					if(ltype == null || !ltype.coercesTo(new SemAtomType(SemAtomType.BOOL)) || !rtype.coercesTo(new SemAtomType(SemAtomType.BOOL)))
						Report.warning("Napaka! Aritmetiène operacije morajo biti tipa integer!");
					
					SemDesc.setActualType(acceptor, ltype);
					break;
				case AbsBinExpr.EQU:
				case AbsBinExpr.NEQ:
				case AbsBinExpr.LTH:
				case AbsBinExpr.GTH:
				case AbsBinExpr.LEQ:
				case AbsBinExpr.GEQ:
					if(ltype instanceof SemPointerType){
						ltype = ((SemPointerType)ltype).type;
						if(rtype instanceof SemPointerType){
							rtype = ((SemPointerType)rtype).type;
						}
						else{
							Report.warning("Napaka! Argumenta (ali argument) operatorjev =, <>, <, >, <= in >= morata biti bodisi istega atomarnega tipa bodisi istega kazalènega tipa!");
							break;
						}
					}
					
					if(ltype != null && !ltype.coercesTo(rtype))
						Report.warning("Napaka! Argumenta (ali argument) operatorjev =, <>, <, >, <= in >= morata biti bodisi istega atomarnega tipa bodisi istega kazalènega tipa!");
					SemDesc.setActualType(acceptor, new SemAtomType(SemAtomType.BOOL));	
					break;
				default:
					SemDesc.setActualType(acceptor, ltype);
					break;
			}
		}	
		
			
		
		
	}

	public void visit(AbsBlockStmt acceptor) {
		if (acceptor.error)
			return; 
		
		acceptor.stmts.accept(this);
		
	}

	public void visit(AbsCallExpr acceptor) {
		if (acceptor.error)
			return; 
		
				
		if(acceptor.name.name.equals("free")){ //free(pointer p)
			acceptor.args.accept(this);
			if(acceptor.args.exprs.size() != 1)
				Report.warning("Neustrezno t. parametrov za klic procedure free!");
			SemType st = SemDesc.getActualType(acceptor.args.exprs.get(0));
			if(st == null || !(st instanceof SemPointerType))
				Report.warning("Tip parametra za klic procedure free mora biti pointer!");
		}
		else{
			acceptor.name.accept(this);
			acceptor.args.accept(this);
			SemType st = SemDesc.getActualType(acceptor.name);
			if(st != null && st instanceof SemSubprogramType)		{	
				SemSubprogramType spt = (SemSubprogramType) st;

				SemDesc.setActualType(acceptor, spt.getResultType());
			}
			else
				Report.warning("Klic podprograma mora vsebovati identifikator podprograma!");
		}
		
		
	}

	public void visit(AbsConstDecl acceptor) {
		if (acceptor.error)
			return; 

		//acceptor.name.accept(this);
		acceptor.value.accept(this);
		SemType st = SemDesc.getActualType(acceptor.value);
		SemDesc.setActualType(acceptor, st);
		
	}

	public void visit(AbsDeclName acceptor) {
		if (acceptor.error)
			return; 
		
		
	}

	public void visit(AbsDecls acceptor) {
		if (acceptor.error)
			return; 
		
		for (AbsDecl decl : acceptor.decls) 
			decl.accept(this);
		
	}

	public void visit(AbsExprStmt acceptor) {
		if (acceptor.error)
			return;
		
		acceptor.expr.accept(this);
		
	}

	public void visit(AbsForStmt acceptor) {
		if (acceptor.error)
			return; 
		
		
		acceptor.name.accept(this);
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);
		
		SemType cType1 = SemDesc.getActualType(acceptor.loBound);
		SemType cType2 = SemDesc.getActualType(acceptor.hiBound);
		if(cType1 == null || cType2 == null  || !cType1.coercesTo(new SemAtomType(SemAtomType.INT)) || !cType2.coercesTo(new SemAtomType(SemAtomType.INT)))
			Report.warning("Napaka! Meje zanke for morajo biti tipa integer!");
		
		
		acceptor.stmt.accept(this);
		
	}

	public void visit(AbsFunDecl acceptor) {
		if (acceptor.error)
			return; 
		
				
		//acceptor.name.accept(this);
		acceptor.pars.accept(this);
		acceptor.type.accept(this);
		acceptor.decls.accept(this);
		
		SemType ft = SemDesc.getActualType(acceptor.type);
		SemSubprogramType spt = new SemSubprogramType(ft);
		for(AbsDecl d: acceptor.pars.decls){			
			SemType pt = SemDesc.getActualType(d);
			spt.addParType(pt);
		}
		SemDesc.setActualType(acceptor, spt);
		
		currentFunDecl = acceptor;
		acceptor.stmt.accept(this);
	}

	public void visit(AbsIfStmt acceptor) {
		if (acceptor.error)
			return; 
		
		acceptor.cond.accept(this);
		
		SemType cType = SemDesc.getActualType(acceptor.cond);
		if(cType != null && !cType.coercesTo(new SemAtomType(SemAtomType.BOOL)))
			Report.warning("Napaka! Tip izraza v if-stavku mora biti tipa boolean!");
		
		acceptor.thenStmt.accept(this);
		acceptor.elseStmt.accept(this);
		

	}

	public void visit(AbsNilConst acceptor) {
		if (acceptor.error)
			return; 
		
		SemDesc.setActualType(acceptor, new SemAtomType(SemAtomType.VOID));
		
	}

	public void visit(AbsPointerType acceptor) {
		if (acceptor.error)
			return; 

		acceptor.type.accept(this);
		SemType st = SemDesc.getActualType(acceptor.type);
		/*if(st == null){
			return;		
		}*/
		SemDesc.setActualType(acceptor, new SemPointerType(st));
	}

	public void visit(AbsProcDecl acceptor) {
		if (acceptor.error)
			return; 

		//acceptor.name.accept(this);
		acceptor.pars.accept(this);
		acceptor.decls.accept(this);		
		
		SemSubprogramType spt = new SemSubprogramType(new SemAtomType(SemAtomType.VOID));
		for(AbsDecl d: acceptor.pars.decls){			
			SemType pt = SemDesc.getActualType(d);
			spt.addParType(pt);
		}
		SemDesc.setActualType(acceptor, spt);
		
		acceptor.stmt.accept(this);
		
	}

	public void visit(AbsProgram acceptor) {
		if (acceptor.error)
			return; 
		
		ConstEvaluator constVisitor = new ConstEvaluator();
		acceptor.accept(constVisitor);
		
		//acceptor.name.accept(this);
		acceptor.decls.accept(this);
		acceptor.stmt.accept(this);
		
	}

	public void visit(AbsRecordType acceptor) {
		if (acceptor.error)
			return; 

		SemRecordType rt = new SemRecordType();
		SemDesc.setActualType(acceptor, rt);
		acceptor.fields.accept(this);
		
		
		for(AbsDecl d: acceptor.fields.decls){ 
			SemType semType = SemDesc.getActualType(d);
			AbsDeclName name = getDeclName(d);
			
			if(semType == null){
				Report.warning("Ne najdem tipa za deklaracijo: " + name);
			}
			else if(semType == rt){
				Report.warning("Zapis ne sme vsebovati samega sebe - lahko pa vsebuje kazalec nase!");
			}

			else{
				rt.addField(name, semType);
			}		
		}
		
	}

	public void visit(AbsStmts acceptor) {
		if (acceptor.error)
			return; 
		
		for (AbsStmt stmt : acceptor.stmts){
			stmt.accept(this);
			
			if(stmt instanceof AbsExprStmt && !(((AbsExprStmt) stmt).expr instanceof AbsCallExpr))
				Report.warning("Opozorilo: Stavek, ki je zgolj opis vrednosti, mora biti klic procedure!",  stmt.begLine, stmt.begColumn);	
		}
		
	}

	public void visit(AbsTypeDecl acceptor) { //+
		if (acceptor.error)
			return; 
		
		SemType t = SemDesc.getActualType(acceptor.type);
		if(t == null){   //èe konstanta e ni izraèunana => izraèunaj				
			if(currentCrossReferencedTypes.contains(acceptor))
				Report.warning("Napaka! Krono sklicevanje na tip: " + acceptor.name.name);	
			else					
				currentCrossReferencedTypes.add(acceptor);
			
			acceptor.type.accept(this);
			t = SemDesc.getActualType(acceptor.type);
		}			
		SemDesc.setActualType(acceptor, t);
		
		
		
	}

	public void visit(AbsTypeName acceptor) {
		if (acceptor.error)
			return; 
		
		AbsDecl decl = SemDesc.getNameDecl(acceptor);
		
		if(decl == null){			
			Report.warning("Manjka povezava na deklaracijo imena tipa: "+acceptor.name);
			//return;
		}
		SemType semType = SemDesc.getActualType(decl);
		if(semType == null){
			if(currentCrossReferencedTypes.contains(acceptor))
				Report.warning("Napaka! Krono sklicevanje na tip: " + acceptor.name);	
			else					
				currentCrossReferencedTypes.add(acceptor);
			//if decl instanceof SemRecordType && acceptor
			decl.accept(this);
			semType = SemDesc.getActualType(decl);
		}
		if(semType == null)
			Report.warning("Manjka tip za deklaracijo identifikatorja: ");
			
		SemDesc.setActualType(acceptor, semType);
		
	}

	public void visit(AbsUnExpr acceptor) {
		if (acceptor.error)
			return; 
		
		acceptor.expr.accept(this);
		SemType type = SemDesc.getActualType(acceptor.expr);
		
		switch (acceptor.oper) {
		case AbsUnExpr.VAL:
			try{
				SemPointerType spt = (SemPointerType) type;
				SemDesc.setActualType(acceptor, spt.type);
			}catch(Exception e){
				Report.warning("Napaka! Izraz ni tipa pointer!");					
			}
			break;
		case AbsUnExpr.MEM:
			SemPointerType spt = new SemPointerType(type);
			SemDesc.setActualType(acceptor, spt);
			break;
		case AbsUnExpr.NOT:
			if(type != null && !type.coercesTo(new SemAtomType(SemAtomType.BOOL)))
				Report.warning("Napaka! Operandi aritmetiènih operacij morajo biti tipa integer!");
			
			SemDesc.setActualType(acceptor, type);
			break;
		default:
			SemDesc.setActualType(acceptor, type);
			break;
		}
	
	}

	public void visit(AbsValExprs acceptor) {
		if (acceptor.error)
			return; 
		
		for (AbsValExpr expr : acceptor.exprs) expr.accept(this);
		
	}

	public void visit(AbsValName acceptor) {
		if (acceptor.error)
			return; 

		AbsDecl decl = SemDesc.getNameDecl(acceptor);
		
		if(decl == null){			
			Report.warning("Manjka povezava na deklaracijo identifikatorja: "+acceptor.name);
			//return;
		}
		SemType semType = SemDesc.getActualType(decl);
		if(semType == null){
			decl.accept(this);
			semType = SemDesc.getActualType(decl);
		}
		if(semType == null)
			Report.warning("Manjka tip za deklaracijo identifikatorja: ");
			
		SemDesc.setActualType(acceptor, semType);
	}

	public void visit(AbsVarDecl acceptor) {
		if (acceptor.error)
			return; 

		//acceptor.name.accept(this);
		acceptor.type.accept(this);
		SemType st = SemDesc.getActualType(acceptor.type);
		SemDesc.setActualType(acceptor, st);
		
	}

	public void visit(AbsWhileStmt acceptor) {
		if (acceptor.error)
			return; 
		
		acceptor.cond.accept(this);
		
		SemType cType = SemDesc.getActualType(acceptor.cond);
		if(!cType.coercesTo(new SemAtomType(SemAtomType.BOOL)))
			Report.warning("Napaka! Tip izraza v pogoju stavka while mora biti tipa boolean!");
		
		
		acceptor.stmt.accept(this);
		
		if(acceptor.stmt instanceof AbsExprStmt && !(((AbsExprStmt) acceptor.stmt).expr instanceof AbsCallExpr))
			Report.warning("Opozorilo: Stavek, ki je zgolj opis vrednosti, mora biti klic procedure!");	
		
	}
	
	private AbsDeclName getDeclName(AbsDecl d){

		if(d instanceof AbsConstDecl)
			return ((AbsConstDecl)d).name;
		else if(d instanceof AbsFunDecl)
			return ((AbsFunDecl)d).name;
		else if(d instanceof AbsProcDecl)
			return ((AbsProcDecl)d).name;
		else if(d instanceof AbsTypeDecl)
			return ((AbsTypeDecl)d).name;
		else if(d instanceof AbsVarDecl)
			return ((AbsVarDecl)d).name;
		else
			return null;
	}

}