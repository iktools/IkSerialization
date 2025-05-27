package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock

class ListTypeInfo(
    ksType: KSType,
    private val concrete: Boolean,
    private val mutable: Boolean,
    private val isSet: Boolean,
    types: TypeUtil
): ATypeInfo(ksType) {
    override val fillable get() = true
    private val argument = types[ksType.arguments[0].type!!.resolve()]

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        if (this.concrete)
            code.beginControlFlow("%T(($data as Collection<*>).map", this.kpType)
        else
            code.beginControlFlow("($data as Collection<*>).map", this.kpType)

        this.argument.instantiate(code, "it!!")
        code.add("\n")
        code.endControlFlow()

        this.collector(code)
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        code.beginControlFlow("$data.map")
        this.argument.pack(code, "it")
        code.endControlFlow()

        code.add(".toList()")
    }

    override fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean) {
        if (instantiate) {
            if (this.concrete)
                code.addStatement("%T(", this.kpType)

            code.beginControlFlow("($data as List<*>).map { itemData ->")
            this.argument.instantiate(code, "itemData!!")
            code.endControlFlow()

            this.collector(code)
        }

        if (this.argument.fillable) {
            code.beginControlFlow("$destination.forEach { item ->")
            this.argument.fill(code, "", "item", false)
            code.endControlFlow()
        }
    }

    private fun collector(code: CodeBlock.Builder) {
        when {
            this.concrete -> code.add(")\n")
            !this.isSet && this.mutable -> code.add(".toMutableList()\n")
            !this.isSet && !this.mutable -> code.add(".toList()\n")
            this.isSet && this.mutable -> code.add(".toMutableSet()\n")
            else -> code.add(".toSet()\n")
        }
    }
}