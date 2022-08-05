package org.jsengine.v8.internal.torque;

public class AstNodeClassCheck {
    public static boolean isInstanceOf(AstNode node) {
        /*switch (node.kind) {
            AST_NODE_KIND_LIST(ENUM_ITEM)
            default:
                throw new RuntimeException("unimplemented");
        }*/
        return true;
    }
}
/*
#define ENUM_ITEM(name)
        case AstNode::Kind::k##name:
        return std::is_base_of<T, name>::value;
        break;

        			CallExpression
			CallMethodExpression
			IntrinsicCallExpression
			StructExpression
			LogicalOrExpression
			LogicalAndExpression
			SpreadExpression
			ConditionalExpression
			IdentifierExpression
			StringLiteralExpression
			NumberLiteralExpression
			FieldAccessExpression
			ElementAccessExpression
			DereferenceExpression
			AssignmentExpression
			IncrementDecrementExpression
			NewExpression
			AssumeTypeImpossibleExpression
			StatementExpression
			TryLabelExpression
			BasicTypeExpression
			FunctionTypeExpression
			UnionTypeExpression
			BlockStatement
			ExpressionStatement
			IfStatement
			WhileStatement
			ForLoopStatement
			BreakStatement
			ContinueStatement
			ReturnStatement
			DebugStatement
			AssertStatement
			TailCallStatement
			VarDeclarationStatement
			GotoStatement
			AbstractTypeDeclaration
			TypeAliasDeclaration
			ClassDeclaration
			StructDeclaration
			GenericDeclaration
			SpecializationDeclaration
			ExternConstDeclaration
			NamespaceDeclaration
			ConstDeclaration
			CppIncludeDeclaration
			TorqueMacroDeclaration
			TorqueBuiltinDeclaration
			ExternalMacroDeclaration
			ExternalBuiltinDeclaration
			ExternalRuntimeDeclaration
			IntrinsicDeclaration
			Identifier
			LabelBlock
*/