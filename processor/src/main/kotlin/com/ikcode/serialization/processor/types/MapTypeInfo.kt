package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock

class MapTypeInfo(
    ksType: KSType,
    private val concrete: Boolean,
    private val mutable: Boolean,
    types: TypeUtil
): ATypeInfo(ksType) {
    override val fillable get() = true
    private val keyType = types[ksType.arguments[0].type!!.resolve()]
    private val valueType = types[ksType.arguments[1].type!!.resolve()]

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        if (this.concrete)
            code.beginControlFlow("%T(($data as Map<*, *>).map", this.kpType)
        else
            code.beginControlFlow("($data as Map<*, *>).map", this.kpType)

        this.keyType.instantiate(code, "it.key!!")
        code.add(" to ")
        this.valueType.instantiate(code, "it.value!!")
        code.add("\n")
        code.endControlFlow()

        this.collector(code)
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        code.beginControlFlow("$data.map")
        this.keyType.pack(code, "it.key!!")
        code.add(" to ")
        this.valueType.pack(code, "it.value!!")
        code.endControlFlow()

        code.add(".toMap()\n")
    }

    override fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean) {
        if (instantiate) {
            if (this.concrete)
                code.addStatement("%T(", this.kpType)

            code.beginControlFlow("($data as Map<*, *>).map { itemData ->")
            this.keyType.instantiate(code, "itemData.key!!")
            code.add(" to ")
            this.valueType.instantiate(code, "itemData.value!!")
            code.endControlFlow()

            this.collector(code)
        }

        if (this.keyType.fillable || this.valueType.fillable) {
            code.beginControlFlow("$destination.forEach { item ->")
            if (this.keyType.fillable)
                this.keyType.fill(code, "", "item.key", false)
            if (this.valueType.fillable)
                this.valueType.fill(code, "", "item.value", false)
            code.endControlFlow()
        }
    }

    private fun collector(code: CodeBlock.Builder) {
        code.add(".toMap()")
        when {
            this.concrete -> code.add(")")
            this.mutable -> code.add(".toMutableMap()")
        }
        code.add("\n")
    }
}