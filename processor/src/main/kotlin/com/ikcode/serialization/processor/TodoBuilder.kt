package com.ikcode.serialization.processor

import com.squareup.kotlinpoet.FunSpec

//TODO remove
class TodoBuilder(classInfo: PackerInfo): ABuilder(classInfo) {
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