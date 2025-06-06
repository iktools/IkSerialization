package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec

class NumberInfo(ksType: KSType): ATypeInfo(ksType) {
    override val fillable get() = false

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        code.add("($data as Number).to${this.name}()")
    }

    override fun remember(funBuilder: FunSpec.Builder, obj: String, name: String) {
        TODO("Not yet implemented")
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        code.add(data)
    }

    override fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean) {
        if (!instantiate)
            throw IllegalArgumentException("Number can't be filled")

        this.instantiate(code, data)
    }
}