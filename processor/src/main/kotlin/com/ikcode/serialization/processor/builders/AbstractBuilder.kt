package com.ikcode.serialization.processor.builders

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.processor.PackerInfo
import com.squareup.kotlinpoet.FunSpec

class AbstractBuilder(
    classInfo: PackerInfo
): ABuilder(classInfo) {
    override fun pack(funBuilder: FunSpec.Builder) {
        funBuilder.beginControlFlow("return when(obj)")
        for (subclass in classInfo.subclasses)
            funBuilder.addStatement("is %T -> %T().pack(obj, session)", subclass.kpType, subclass.packerType)
        if (classInfo.isOpen)
            funBuilder.addStatement("else -> packMap.putAll(otherSubclasses[obj.javaClass]?.packOwnData(obj, session) ?: throw Exception(\"Unknown·${classInfo.name}·subtype\"))")
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
            .addStatement("if (existingObj != null) return existingObj as ${classInfo.name}")

        funBuilder.beginControlFlow("return when(objData[\"@type\"])")
        for(subclass in classInfo.subclasses)
            funBuilder.addStatement("\"${subclass.name}\" -> %T().instantiate(packedData, session)", subclass.packerType)
        if (classInfo.isOpen)
            funBuilder.addStatement("else -> subclassPackerNames[objData[\"@type\"]]?.instantiate(packedData, session) ?: throw Exception(\"Unknown·${classInfo.name}·subtype\")")
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
}