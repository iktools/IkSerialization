package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType
import com.ikcode.serialization.core.references.ReferencePointer
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec

class ClassInfo(ksType: KSType): ATypeInfo(ksType) {
    override val fillable get() = true
    val packerType = ClassName(ksType.declaration.packageName.asString(), "${this.name}_Packer")

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        code.add("%T().instantiate($data, session)", this.packerType)
    }

    override fun remember(funBuilder: FunSpec.Builder, obj: String, data: String) {
        funBuilder.addStatement("%T().remember($obj, $data)", this.packerType)
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        code.add("%T().pack($data, session)", this.packerType)
    }

    override fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean) {
        if (instantiate)
            code.add("%T().unpack($data, session)", this.packerType)
        else
            code.add("%T().fillData($destination, session)", this.packerType)
    }
}