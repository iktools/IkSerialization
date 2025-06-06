package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toTypeName

abstract class ATypeInfo(val ksType: KSType) {
    val name = ksType.declaration.simpleName.asString()
    val isNullable = ksType.isMarkedNullable
    val kpType = ksType.makeNotNullable().toTypeName()

    abstract val fillable: Boolean
    abstract fun instantiate(code: CodeBlock.Builder, data: String)
    open fun remember(funBuilder: FunSpec.Builder, obj: String, data: String) { }
    abstract fun pack(code: CodeBlock.Builder, data: String)
    abstract fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean)

    val nullAssert get() = if (this.isNullable) "!!" else ""
}