package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec

class ProxyTypeInfo(ksType: KSType, val proxy: ATypeInfo): ATypeInfo(ksType) {
    override val fillable: Boolean
        get() = false

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        code.add("%T(", kpType)
        this.proxy.instantiate(code, data)
        code.add(")")
    }

    override fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean) {
        if (!instantiate)
            throw IllegalArgumentException("Proxied type can't be filled")

        this.instantiate(code, data)
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        this.proxy.pack(code, "$data.packingProxy()")
    }
}