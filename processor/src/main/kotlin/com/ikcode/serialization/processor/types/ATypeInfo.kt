package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType

abstract class ATypeInfo(ksType: KSType) {
    val name = ksType.declaration.simpleName.asString()
    //val fullName = ksType.declaration.qualifiedName?.asString() ?: throw Exception("Local declaration is not supported")
    val isNullable = ksType.isMarkedNullable

    abstract fun instantiate(data: String): String

    //val arguments = ksType.arguments.map { types[it.type!!.resolve()] }


    /*private val justType = type.starProjection().makeNotNullable()
    private val classDeclaration = types.resolve(type.declaration)

    val isNumber = this.justType in types.numbers
    val isPrimitive = this.justType in types.primitives
    val isEnum = classDeclaration?.classKind == ClassKind.ENUM_CLASS
    val concrete = classDeclaration?.getConstructors()?.any() == true
    val isCollection = types.collectionType.isAssignableFrom(this.justType)
    val isMap = types.mapType.isAssignableFrom(this.justType)*/

    //TODO remove
    val original = ksType
}