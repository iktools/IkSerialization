package com.ikcode.serialization.processor

import com.google.devtools.ksp.symbol.KSType

open class TypeInfo(type: KSType, types: TypeUtil) {
    val name = type.declaration.simpleName
    val arguments = type.arguments.map { types[it.type!!.resolve()] }
    val isNullable = type.isMarkedNullable

    private val justType = type.starProjection().makeNotNullable()

    val isNumber = this.justType in types.numbers
    val isPrimitive = this.justType in types.primitives
    val isMap = types.mapType.isAssignableFrom(this.justType)

    //TODO remove
    val original = type
}