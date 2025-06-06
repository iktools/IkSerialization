package com.ikcode.serialization.processor.builders

import com.ikcode.serialization.processor.PackerInfo
import com.ikcode.serialization.processor.types.TypeUtil
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec

class ProxyBuilder(
    classInfo: PackerInfo,
    types: TypeUtil
): ABuilder(classInfo) {
    val proxyType = types[classInfo.proxy!!.arguments[0].type!!.resolve()]

    override fun pack(funBuilder: FunSpec.Builder) {
        val code = CodeBlock.builder()
        code.add("return ")

        this.proxyType.pack(code, "obj.packingProxy()")
        funBuilder.addCode(code.build())
    }

    override fun unpack(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("return this.instantiate(packedData, session)")
    }

    override fun instantiate(funBuilder: FunSpec.Builder) {
        val code = CodeBlock.builder()

        code.add("return %T(", classInfo.kpType)
        this.proxyType.instantiate(code, "packedData")
        code.add(")")

        funBuilder.addCode(code.build())
    }

    override fun remember(funBuilder: FunSpec.Builder) {
        funBuilder.addComment("no operation")
    }

    override fun fill(funBuilder: FunSpec.Builder) {
        funBuilder.addComment("no operation")
    }
}