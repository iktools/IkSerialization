package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec

class EnumInfo(ksType: KSType): ATypeInfo(ksType) {
    override val fillable get() = false

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        code.add("%T.entries[($data as Number).toInt()]", this.kpType)
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        code.add("$data.ordinal")
    }

    override fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean) {
        if (!instantiate)
            throw IllegalArgumentException("Number can't be filled")

        this.instantiate(code, data)
    }
}