package org.jsengine.v8.internal.torque; 

public class AstNode {
	public static enum Kind {
		kCallExpression,
		kCallMethodExpression,
		kIntrinsicCallExpression,
		kStructExpression,
		kLogicalOrExpression,
		kLogicalAndExpression,
		kSpreadExpression,
		kConditionalExpression,
		kIdentifierExpression,
		kStringLiteralExpression,
		kNumberLiteralExpression,
		kFieldAccessExpression,
		kElementAccessExpression,
		kDereferenceExpression,
		kAssignmentExpression,
		kIncrementDecrementExpression,
		kNewExpression,
		kAssumeTypeImpossibleExpression,
		kStatementExpression,
		kTryLabelExpression,
		kBasicTypeExpression,
		kFunctionTypeExpression,
		kUnionTypeExpression,
		kBlockStatement,
		kExpressionStatement,
		kIfStatement,
		kWhileStatement,
		kForLoopStatement,
		kBreakStatement,
		kContinueStatement,
		kReturnStatement,
		kDebugStatement,
		kAssertStatement,
		kTailCallStatement,
		kVarDeclarationStatement,
		kGotoStatement,
		kAbstractTypeDeclaration,
		kTypeAliasDeclaration,
		kClassDeclaration,
		kStructDeclaration,
		kGenericDeclaration,
		kSpecializationDeclaration,
		kExternConstDeclaration,
		kNamespaceDeclaration,
		kConstDeclaration,
		kCppIncludeDeclaration,
		kTorqueMacroDeclaration,
		kTorqueBuiltinDeclaration,
		kExternalMacroDeclaration,
		kExternalBuiltinDeclaration,
		kExternalRuntimeDeclaration,
		kIntrinsicDeclaration,
		kIdentifier,
		kLabelBlock
	};
	
	public Kind kind;
	public SourcePosition pos;
	
	public AstNode(Kind kind, SourcePosition pos) {
		this.kind = kind;
		this.pos = pos;
	}
}