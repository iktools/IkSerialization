package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock

class ListTypeInfo(
    ksType: KSType,
    private val concrete: Boolean,
    private val mutable: Boolean,
    types: TypeUtil
): ATypeInfo(ksType) {
    override val fillable get() = true
    val argument = types[ksType.arguments[0].type!!.resolve()]

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        code.add("//${this.concrete}\n")
        if (this.concrete)
            code.beginControlFlow("%T(($data as Collection<*>).map", this.kpType)
        else
            code.beginControlFlow("($data as Collection<*>).map", this.kpType)

        this.argument.instantiate(code, "it!!")
        code.add("\n")
        code.endControlFlow()

        if (this.concrete)
            code.add(")")
        else if (this.mutable)
            code.addStatement(".toMutableList()")
        else
            code.addStatement(".toList()")
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        code.beginControlFlow("$data.map")
        this.argument.pack(code, "it")
        code.endControlFlow()

        if (this.mutable)
            code.addStatement(".toMutableList()")
        else
            code.addStatement(".toList()")
    }

    override fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean) {
        if (instantiate) {
            if (this.concrete)
                code.addStatement("%T(", this.kpType)

            code.beginControlFlow("($data as List<*>).map { itemData ->")
            this.argument.instantiate(code, "itemData!!")
            code.endControlFlow()

            if (this.concrete)
                code.addStatement(")")
            else if (this.mutable)
                code.addStatement(".toMutableList()")
            else
                code.addStatement(".toList()")
        }

        if (this.argument.fillable) {
            code.beginControlFlow("$destination.forEach { item ->")
            this.argument.fill(code, "", "item", false)
            code.endControlFlow()
        }
    }
}