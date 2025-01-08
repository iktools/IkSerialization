package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock

class PrimitiveInfo(ksType: KSType): ATypeInfo(ksType) {
    override val fillable get() = false

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        code.add("$data as ${this.name}")
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        code.add(data)
    }

    override fun fill(code: CodeBlock.Builder, data: String, instance: String?) {
        if (instance != null)
            throw IllegalArgumentException("Number can't be filled")

        code.add("$data as ${this.name}")
    }
}