package com.ikcode.serialization.processor.builders

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.processor.PackerInfo
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter

class AbstractBuilder(
    classInfo: PackerInfo
): ABuilder(classInfo) {
    override fun pack(funBuilder: FunSpec.Builder) {
        funBuilder.beginControlFlow("return when(obj)")
        for (subclass in classInfo.subclasses)
            funBuilder.addStatement("is %T -> %T().pack(obj, session)", subclass.kpType, subclass.packerType)
        if (classInfo.isOpen)
            funBuilder.addStatement("else -> otherSubclasses[obj.javaClass]?.pack(obj, session) ?: throw Exception(\"Unknown·${classInfo.name}·subtype\")")
        else
            funBuilder.addStatement("else -> throw Exception(\"Unknown ${classInfo.name} subtype\")")
        funBuilder.endControlFlow()
    }

    override fun unpack(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("val obj = this.instantiate(packedData, session)")
            .addStatement("this.fillData(obj, session)")
            .addStatement("return obj")
    }

    override fun instantiate(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("val name = (packedData as %T).name", ReferencePointer::class)
            .addStatement("val objData = session.dereference(name) as Map<*, *>")
            .addStatement("val existingObj = session.getInstance(name)")
            .addStatement("if (existingObj != null) return existingObj as %T", classInfo.kpType)

        funBuilder.beginControlFlow("return when(objData[\"@type\"])")
        for(subclass in classInfo.subclasses)
            funBuilder.addStatement("\"${subclass.name}\" -> %T().instantiate(packedData, session)", subclass.packerType)
        if (classInfo.isOpen)
            funBuilder.addStatement("else -> subclassPackerNames[objData[\"@type\"]]?.instantiate(packedData, session) ?: throw Exception(\"Unknown·${classInfo.name}·subtype\")")
        else
            funBuilder.addStatement("else -> throw Exception(\"Unknown ${classInfo.name} subtype\")")
        funBuilder.endControlFlow()
    }

    override fun remember(funBuilder: FunSpec.Builder) {
        funBuilder.beginControlFlow("when(obj)")
        for (subclass in classInfo.subclasses)
            funBuilder.addStatement("is %T -> %T().remember(obj, packedData, session)", subclass.kpType, subclass.packerType)
        if (classInfo.isOpen)
            funBuilder.addStatement("else -> otherSubclasses[obj.javaClass]?.remember(obj, packedData, session) ?: throw Exception(\"Unknown·${classInfo.name}·subtype\")")
        else
            funBuilder.addStatement("else -> throw Exception(\"Unknown ${classInfo.name} subtype\")")
        funBuilder.endControlFlow()
    }

    override fun fill(funBuilder: FunSpec.Builder) {
        funBuilder.beginControlFlow("when(obj)")
        for (subclass in classInfo.subclasses)
            funBuilder.addStatement("is %T -> %T().fillData(obj, session)", subclass.kpType, subclass.packerType)
        if (classInfo.isOpen)
            funBuilder.addStatement("else -> otherSubclasses[obj.javaClass]?.fillData(obj, session) ?: throw Exception(\"Unknown·${classInfo.name}·subtype\")")
        else
            funBuilder.addStatement("else -> throw Exception(\"Unknown ${classInfo.name} subtype\")")
        funBuilder.endControlFlow()
    }

    override fun extras(typeBuilder: TypeSpec.Builder) {
        if (!this.classInfo.isOpen)
            return

        typeBuilder.addType(
            TypeSpec.companionObjectBuilder()
                .addProperty(
                    PropertySpec.builder(
                        "otherSubclasses",
                        Map::class.asTypeName()
                            .plusParameter(Class::class.asClassName().parameterizedBy(STAR))
                            .plusParameter(this.classInfo.interfaceType),
                        KModifier.PRIVATE
                    )
                    .initializer("java.util.ServiceLoader.load(I${classInfo.name}_Packer::class.java).associateBy { it.objType() }")
                    .build()
                )
                .addProperty(
                    PropertySpec.builder(
                        "subclassPackerNames",
                        ClassName("kotlin.collections", "Map")
                            .plusParameter(String::class.asTypeName())
                            .plusParameter(this.classInfo.interfaceType),
                        KModifier.PRIVATE
                    )
                    .initializer("otherSubclasses.values.associateBy·{ it.typeName() }")
                    .build()
                )
                .build()
        )
    }
}