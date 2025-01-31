package com.ikcode.serialization.processor.builders

import com.ikcode.serialization.processor.PackerInfo
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName

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
                .returns(Class::class.asClassName().parameterizedBy(WildcardTypeName.producerOf(this.classInfo.kpType)))
                .build())
            .addFunction(FunSpec.builder("typeName")
                .addModifiers(KModifier.ABSTRACT)
                .returns(String::class)
                .build())
    }
}