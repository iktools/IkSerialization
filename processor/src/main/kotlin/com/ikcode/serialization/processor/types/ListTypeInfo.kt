package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec

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
        if (this.mutable)
            when {
                this.concrete -> code.add("%T()", this.kpType)
                this.isSet -> code.add("mutableSetOf<%T>()", this.argument.kpType)
                else -> code.add("mutableListOf<%T>()", this.argument.kpType)
            }
        else {
            if (this.concrete)
                code.beginControlFlow("%T(($data as Collection<*>).map", this.kpType)
            else
                code.beginControlFlow("($data as Collection<*>).map", this.kpType)

            this.argument.instantiate(code, "it!!")
            if (this.argument.fillable)
                code.add(".also { item -> session.rememberData(item, it!!) }")
            code.add("\n")
            code.endControlFlow()

            this.collector(code)
        }
    }

    override fun remember(funBuilder: FunSpec.Builder, obj: String, name: String) {
        //TODO("Not yet implemented")
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
            if (this.argument.fillable)
                code.add(".also { item -> session.rememberData(item, itemData!!) }")
            code.endControlFlow()

            this.collector(code)
        } else if (this.mutable) {
            code.beginControlFlow("($data as List<*>).forEach { itemData ->")
            code.add("$destination += ")
            this.argument.instantiate(code, "itemData!!")
            if (this.argument.fillable)
                code.add(".also { item -> session.rememberData(item, itemData!!) }")
            code.endControlFlow()
        }

        if (this.argument.fillable) {
            if (instantiate || this.mutable)
                code.add("\n")

            code.beginControlFlow("$destination.forEach { item ->")
            code.add("val data = session.getData(item)\n")
            this.argument.fill(code, "data", "item", false)
            code.endControlFlow()
        }
    }

    private fun collector(code: CodeBlock.Builder) {
        when {
            this.concrete -> code.add(")")
            !this.isSet && this.mutable -> code.add(".toMutableList()")
            !this.isSet && !this.mutable -> code.add(".toList()")
            this.isSet && this.mutable -> code.add(".toMutableSet()")
            else -> code.add(".toSet()")
        }
    }
}