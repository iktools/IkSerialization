package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock

class ClassInfo(ksType: KSType): ATypeInfo(ksType) {
    override val fillable get() = true

    override fun instantiate(data: String) = "null /* TODO */" //TODO

    override fun pack(code: CodeBlock.Builder, data: String) {
        //TODO
    }

    override fun fill(code: CodeBlock.Builder, data: String) {
        //TODO
    }
}