package org.jsengine.v8.internal.torque; 

public class ParseResultHolderBase {
	private TypeId type_id_;
	
	public ParseResultHolderBase(TypeId type_id) {
		type_id_ = type_id;
	}
	
	public static enum TypeId {
		kStdString,
		kBool,
		kStdVectorOfString,
		kExpressionPtr,
		kIdentifierPtr,
		kOptionalIdentifierPtr,
		kStatementPtr,
		kDeclarationPtr,
		kTypeExpressionPtr,
		kOptionalTypeExpressionPtr,
		kLabelBlockPtr,
		kOptionalLabelBlockPtr,
		kNameAndTypeExpression,
		kImplicitParameters,
		kOptionalImplicitParameters,
		kNameAndExpression,
		kAnnotation,
		kVectorOfAnnotation,
		kClassFieldExpression,
		kStructFieldExpression,
		kStdVectorOfNameAndTypeExpression,
		kStdVectorOfNameAndExpression,
		kStdVectorOfClassFieldExpression,
		kStdVectorOfStructFieldExpression,
		kIncrementDecrementOperator,
		kOptionalStdString,
		kStdVectorOfStatementPtr,
		kStdVectorOfDeclarationPtr,
		kStdVectorOfStdVectorOfDeclarationPtr,
		kStdVectorOfExpressionPtr,
		kExpressionWithSource,
		kParameterList,
		kTypeList,
		kOptionalTypeList,
		kLabelAndTypes,
		kStdVectorOfLabelAndTypes,
		kStdVectorOfLabelBlockPtr,
		kOptionalStatementPtr,
		kOptionalExpressionPtr,
		kTypeswitchCase,
		kStdVectorOfTypeswitchCase,
		kStdVectorOfIdentifierPtr,
		
		kJsonValue,
		kJsonMember,
		kStdVectorOfJsonValue,
		kStdVectorOfJsonMember,
	};
	
	public <T> T cast(Class<T> type) {
		return (T) ((ParseResultHolder) this).value_;
	}
}