package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ksp.toClassName

abstract class ATypeInfo(ksType: KSType) {
    val name = ksType.declaration.simpleName.asString()
    val isNullable = ksType.isMarkedNullable
    val kpType = ksType.makeNotNullable().toClassName()

    abstract val fillable: Boolean
    abstract fun instantiate(code: CodeBlock.Builder, data: String)
    abstract fun pack(code: CodeBlock.Builder, data: String)
    abstract fun fill(code: CodeBlock.Builder, data: String, instance: String?)

    val nullAssert get() = if (this.isNullable) "!!" else ""

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