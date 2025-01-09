package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock

class ListTypeInfo(
    ksType: KSType,
    private val concrete: Boolean,
    types: TypeUtil
): ATypeInfo(ksType) {
    override val fillable get() = true
    val argument = types[ksType.arguments[0].type!!.resolve()]

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        code.beginControlFlow("%T(($data as Collection<*>).map", this.kpType)
        this.argument.instantiate(code, "it!!")
        code.add("\n")
        code.endControlFlow()
        code.add(")")
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        code.beginControlFlow("$data.map")
        this.argument.pack(code, "it")
        code.endControlFlow()
        code.addStatement(".toList()") //TODO add for mutable list
        //TODO
        //"$getValue.map { ${packType(type.arguments[0], "it")} }.toList()"
    }

    override fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean) {
        if (instantiate) {
            if (this.concrete)
                code.addStatement("%T(", this.kpType)

            code.beginControlFlow("($data as List<*>).map { itemData ->")
            this.argument.instantiate(code, "itemData!!")
            //fillParamType(fillFunc, type.arguments[0], "", "itemData!!", false, true, true)
            code.endControlFlow()

            if (this.concrete)
                code.addStatement(")")
            else
                code.addStatement(".toList()") //TODO add for mutable list
        }

        if (this.argument.fillable) {
            code.beginControlFlow("$destination.forEach { item ->")
            this.argument.fill(code, "", "item", false)
            //fillParamType(fillFunc, type.arguments[0], "item")
            code.endControlFlow()
        }
    }
}