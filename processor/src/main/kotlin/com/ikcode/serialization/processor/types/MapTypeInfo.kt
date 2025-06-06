package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec

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
        if (this.mutable)
            if (this.concrete)
                code.add("%T()", this.kpType)
            else
                code.add("mutableMapOf<%T, %T>()", this.keyType.kpType, this.valueType.kpType)
        else {
            if (this.concrete)
                code.beginControlFlow("%T(($data as Map<*, *>).map", this.kpType)
            else
                code.beginControlFlow("($data as Map<*, *>).map", this.kpType)

            this.keyType.instantiate(code, "it.key!!")
            if (this.keyType.fillable)
                code.add(".also { item -> session.rememberData(item, it.key!!) }")

            code.add(" to ")

            this.valueType.instantiate(code, "it.value!!")
            if (this.valueType.fillable)
                code.add(".also { item -> session.rememberData(item, it.value!!) }")

            code.add("\n")
            code.endControlFlow()

            this.collector(code)
        }
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        code.beginControlFlow("$data.map")
        this.keyType.pack(code, "it.key!!")
        code.add(" to ")
        this.valueType.pack(code, "it.value!!")
        code.endControlFlow()

        code.add(".toMap()")
    }

    override fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean) {
        if (instantiate) {
            if (this.concrete)
                code.addStatement("%T(", this.kpType)

            code.beginControlFlow("($data as Map<*, *>).map { itemData ->")

            this.keyType.instantiate(code, "itemData.key!!")
            if (this.keyType.fillable)
                code.add(".also { item -> session.rememberData(item, itemData.key!!) }")

            code.add(" to ")

            this.valueType.instantiate(code, "itemData.value!!")
            if (this.valueType.fillable)
                code.add(".also { item -> session.rememberData(item, itemData.value!!) }")

            code.endControlFlow()
            this.collector(code)
        } else if (this.mutable) {
            code.beginControlFlow("($data as Map<*, *>).forEach { itemData ->")

            code.add("$destination[")
            this.keyType.instantiate(code, "itemData.key!!")
            if (this.keyType.fillable)
                code.add(".also { item -> session.rememberData(item, itemData.key!!) }")

            code.add("] = ")

            this.valueType.instantiate(code, "itemData.value!!")
            if (this.valueType.fillable)
                code.add(".also { item -> session.rememberData(item, itemData.value!!) }")

            code.endControlFlow()
        }

        if (this.keyType.fillable || this.valueType.fillable) {
            if (instantiate || this.mutable)
                code.add("\n")

            code.beginControlFlow("$destination.forEach { item ->")
            if (this.keyType.fillable) {
                code.add("val keyData = session.getData(item.key)\n")
                this.keyType.fill(code, "keyData", "item.key", false)
            }
            if (this.keyType.fillable && this.valueType.fillable)
                code.add("\n")
            if (this.valueType.fillable) {
                code.add("val valueData = session.getData(item.value)\n")
                this.valueType.fill(code, "valueData", "item.value", false)
            }
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