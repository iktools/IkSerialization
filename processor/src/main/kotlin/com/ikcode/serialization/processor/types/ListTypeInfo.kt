package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock

class ListTypeInfo(ksType: KSType, types: TypeUtil): ATypeInfo(ksType) {
    override val fillable get() = true
    val argument = types[ksType.arguments[0].type!!.resolve()]

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        //TODO
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        //TODO
    }

    override fun fill(code: CodeBlock.Builder, data: String, instance: String?) {
        //TODO
    }
}