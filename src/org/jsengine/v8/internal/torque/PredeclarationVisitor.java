package org.jsengine.v8.internal.torque; 

public class PredeclarationVisitor {
	public static void predeclare(Ast ast) {
		CurrentScope.Scope current_namespace = new CurrentScope.Scope(GlobalContext.getDefaultNamespace());
		for (Declaration child : ast.declarations()) 
			predeclare(child);
	}
	
	private static void predeclare(Declaration decl) {
		CurrentSourcePosition.Scope scope = new CurrentSourcePosition.Scope(decl.pos);
		
		switch (decl.kind) {
			case kAbstractTypeDeclaration:
				predeclare(AbstractTypeDeclaration.cast(decl));
				break;
			case kTypeAliasDeclaration:
				predeclare(TypeAliasDeclaration.cast(decl));
				break;
			case kClassDeclaration:
				predeclare(ClassDeclaration.cast(decl));
				break;
			case kStructDeclaration:
				predeclare(StructDeclaration.cast(decl));
				break;
			case kNamespaceDeclaration:
				predeclare(NamespaceDeclaration.cast(decl));
				break;
			case kGenericDeclaration:
				predeclare(GenericDeclaration.cast(decl));
				break;
			default:
				break;
		}
	}
	
	public static void resolvePredeclarations() {
		for (Declarable p : GlobalContext.allDeclarables()) {
			TypeAlias alias = TypeAlias.dynamicCast(p);
			if (alias != null) {
				CurrentScope.Scope scope_activator = new CurrentScope.Scope(alias.parentScope());
				CurrentSourcePosition.Scope position_activator = new CurrentSourcePosition.Scope(alias.position());
				alias.resolve();
			}
		}
	}
}