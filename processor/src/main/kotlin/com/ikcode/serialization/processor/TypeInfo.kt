package com.ikcode.serialization.processor

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSType

open class TypeInfo(type: KSType, types: TypeUtil) {
    val name = type.declaration.simpleName.asString()
    val arguments = type.arguments.map { types[it.type!!.resolve()] }
    val isNullable = type.isMarkedNullable

    private val justType = type.starProjection().makeNotNullable()
    private val classDeclaration = types.resolve(type.declaration)

    val isNumber = this.justType in types.numbers
    val isPrimitive = this.justType in types.primitives
    val isEnum = classDeclaration?.classKind == ClassKind.ENUM_CLASS
    val concrete = classDeclaration?.getConstructors()?.any() ?: false
    val isCollection = types.collectionType.isAssignableFrom(this.justType)
    val isMap = types.mapType.isAssignableFrom(this.justType)

    //TODO remove
    val original = type
}