package com.ikcode.serialization.processor.builders

import com.ikcode.serialization.processor.PackerInfo
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

class InterfaceBuilder(
    classInfo: PackerInfo
): ABuilder(classInfo) {
    override val typeBuilder get() = TypeSpec.interfaceBuilder(this.classInfo.interfaceFileName)

    override fun pack(funBuilder: FunSpec.Builder) {
        funBuilder.addModifiers(KModifier.ABSTRACT)
    }

    override fun unpack(funBuilder: FunSpec.Builder) {
        funBuilder.addModifiers(KModifier.ABSTRACT)
    }

    override fun instantiate(funBuilder: FunSpec.Builder) {
        funBuilder.addModifiers(KModifier.ABSTRACT)
    }

    override fun fill(funBuilder: FunSpec.Builder) {
        funBuilder.addModifiers(KModifier.ABSTRACT)
    }

    override fun extras(typeBuilder: TypeSpec.Builder) {
        typeBuilder
            .addFunction(FunSpec.builder("objType")
                .addModifiers(KModifier.ABSTRACT)
                .returns(Class::class.asClassName().parameterizedBy(STAR))
                .build())
            .addFunction(FunSpec.builder("typeName")
                .addModifiers(KModifier.ABSTRACT)
                .returns(String::class)
                .build())
    }
}