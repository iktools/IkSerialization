package com.ikcode.serialization.processor.builders

import com.ikcode.serialization.processor.PackerInfo
import com.squareup.kotlinpoet.FunSpec

class EnumBuilder(
    classInfo: PackerInfo
): ABuilder(classInfo) {
    override fun pack(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("return obj.ordinal")
    }

    override fun unpack(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("return this.instantiate(packedData, session)")
    }

    override fun instantiate(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("return ${classInfo.name}.values()[packedData as Int]")
    }

    override fun fill(funBuilder: FunSpec.Builder) {
        funBuilder.addComment("no operation")
    }
}