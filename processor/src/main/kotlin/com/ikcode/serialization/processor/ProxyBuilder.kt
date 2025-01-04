package com.ikcode.serialization.processor

import com.google.devtools.ksp.processing.Resolver
import com.squareup.kotlinpoet.FunSpec

class ProxyBuilder(
    classInfo: PackerInfo
): ABuilder(classInfo) {
    override fun pack(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("TODO()")
    }

    override fun unpack(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("TODO()")
    }

    override fun instantiate(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("TODO()")
    }

    override fun fill(funBuilder: FunSpec.Builder) {
        // no operation
    }
}