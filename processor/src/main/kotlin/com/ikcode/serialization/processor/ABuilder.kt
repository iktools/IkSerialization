package com.ikcode.serialization.processor

import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

abstract class ABuilder(
    protected val classInfo: PackerInfo
) {
    abstract fun pack(funBuilder: FunSpec.Builder)
    abstract fun unpack(funBuilder: FunSpec.Builder)
    abstract fun instantiate(funBuilder: FunSpec.Builder)
    abstract fun fill(funBuilder: FunSpec.Builder)
    open fun extras(typeBuilder: TypeSpec.Builder) {}

    fun file(): FileSpec {
        val packFunc = FunSpec.builder("pack")
            .addParameter("obj", this.classInfo.kpType)
            .addParameter("session", PackingSession::class)
            .returns(Any::class)
        this.pack(packFunc)

        val unpackFunc = FunSpec.builder("unpack")
            .addParameter("packedData", Any::class)
            .addParameter("session", UnpackingSession::class)
            .returns(this.classInfo.kpType)
        this.unpack(unpackFunc)

        val instantiateFunc = FunSpec.builder("instantiate")
            .addParameter("packedData", Any::class)
            .addParameter("session", UnpackingSession::class)
            .returns(this.classInfo.kpType)
        this.instantiate(instantiateFunc)

        val fillDataFunc = FunSpec.builder("fillData")
            .addParameter("obj", this.classInfo.kpType)
            .addParameter("session", UnpackingSession::class)
        this.fill(fillDataFunc)

        val type = TypeSpec.classBuilder(this.classInfo.outFileName)
            .addFunction(packFunc.build())
            .addFunction(unpackFunc.build())
            .addFunction(instantiateFunc.build())
            .addFunction(fillDataFunc.build())
        this.extras(type)

        return FileSpec.builder(this.classInfo.namespace, this.classInfo.outFileName)
            .addType(type.build())
            .build()
    }

    protected fun packType(type: TypeInfo, getValue: String): String = when {
        type.isPrimitive -> getValue
        type.isCollection -> "$getValue.map { ${packType(type.arguments[0], "it")} }.toList()"
        //TODO
        /*type.rawType == "java.util.ArrayList" || type.rawType == "java.util.List" || type.rawType == "java.util.Collection" || type.rawType == "java.lang.Iterable" || type.rawType == "kotlin.collections.List" || type.rawType == "java.util.HashSet" || type.rawType == "java.util.Set" ->
            "$getValue.map { ${packType(type.genericParams[0], "it")} }.toList()"
        type.rawType == "java.util.HashMap" || type.rawType == "java.util.Map" ->
            "$getValue.map { ${packType(type.genericParams[0], "it.key")}·to ${packType(type.genericParams[1], "it.value")} }.toMap()"
        type.rawType == "kotlin.Pair" ->
            "listOf<Any>(${packType(type.genericParams[0], "$getValue.first")}, ${packType(type.genericParams[1], "$getValue.second")})"
        type.rawType == "com.ikcode.ancientstar1.core.util.UnorderedPair" ->
            "listOf<Any>(${packType(type.genericParams[0], "$getValue.first")}, ${packType(type.genericParams[0], "$getValue.second")})"*/
        else -> "${type.name}_Packer().pack($getValue, session)"
    }

    protected fun instantiateParamFunc(type: TypeInfo, data: String): String = when {
        type.isNumber -> "($data as Number).to${type.name}()"
        type.isPrimitive -> "$data as ${type.name}"
        type.isMap ->
            "($data as Map<*, *>).map { ${
                instantiateParamFunc(
                    type.arguments[0],
                    "it.key!!"
                )
            }·to ${instantiateParamFunc(type.arguments[1], "it.value!!")}}.toMap().toMutableMap()"
        type.isCollection -> "${type.name}(($data as Collection<${type.arguments[0].name}>).map { ${instantiateParamFunc(type.arguments[0], "it!!")} })"
        //TODO
        /*type.rawType == "java.util.ArrayList" -> "ArrayList(($data as List<*>).map { ${instantiateParamFunc(type.genericParams[0], "it!!")} })"
        type.rawType == "java.lang.Iterable" || type.rawType == "java.util.Collection" || type.rawType == "kotlin.collections.List" -> "($data as List<*>).map { ${instantiateParamFunc(type.genericParams[0], "it!!")} }.toList()"
        type.rawType == "java.util.HashSet" -> "($data as List<*>).map { ${instantiateParamFunc(type.genericParams[0], "it!!")} }.toHashSet()"
        type.rawType == "java.util.HashMap" -> "HashMap(($data as Map<*, *>).map { ${instantiateParamFunc(type.genericParams[0], "it.key!!")}·to ${instantiateParamFunc(type.genericParams[1], "it.value!!")}}.toMap())"
        type.rawType == "java.util.Map" -> "($data as Map<*, *>).map { ${instantiateParamFunc(type.genericParams[0], "it.key!!")}·to ${instantiateParamFunc(type.genericParams[1], "it.value!!")}}.toMap().toMutableMap()"
        type.rawType == "kotlin.Pair" -> "($data as List<*>).let { Pair(${instantiateParamFunc(type.genericParams[0], "it[0]!!")}, ${instantiateParamFunc(type.genericParams[1], "it[1]!!")}) }"
        type.rawType == "com.ikcode.ancientstar1.core.util.UnorderedPair" -> "($data as List<*>).let { com.ikcode.ancientstar1.core.util.UnorderedPair(${instantiateParamFunc(type.genericParams[0], "it[0]!!")}, ${instantiateParamFunc(type.genericParams[0], "it[1]!!")}) }"*/
        else -> {
            "${type.name}_Packer().instantiate($data, session)"
        }
    }

    protected fun fillParamType(fillFunc: FunSpec.Builder, type: TypeInfo, destination: String, data: String, instantiate: Boolean, fill: Boolean) {
        val nullAssert = if (type.isNullable) "!!" else ""

        when {
            type.isPrimitive -> if (instantiate)
                fillFunc.addStatement("$destination = ${unpackCall(type, data)}")
            type.isCollection -> if (instantiate) {
                if (type.concrete)
                    fillFunc.addStatement("$destination = ${type.name}(")
                else
                    fillFunc.addStatement("$destination = ")

                fillFunc.beginControlFlow("($data as List<*>).map { itemData ->")
                fillFunc.addStatement(unpackCall(type.arguments[0], "itemData!!"))
                fillFunc.endControlFlow()

                if (type.concrete)
                    fillFunc.addStatement(")")
                else
                    fillFunc.addStatement(".toList()") //TODO add for mutable list

                /*fillFunc.beginControlFlow("$destination.forEach { item ->")
                fillParamType(fillFunc, type.arguments[0], "item")
                fillFunc.endControlFlow()*/
            }
            /*"java.util.ArrayList", "java.util.Collection", "java.util.HashSet", "java.util.List" -> {
                fillFunc.beginControlFlow("$destination.forEach { item ->")
                fillParamType(fillFunc, type.genericParams[0], "item")
                fillFunc.endControlFlow()
            }
            "java.util.Map" -> {
                fillFunc.beginControlFlow("$destination.entries.forEach { (key, value) ->")
                if (type.genericParams[0].fillable)
                    fillParamType(fillFunc, type.genericParams[0], "key")
                if (type.genericParams[1].fillable)
                    fillParamType(fillFunc, type.genericParams[1], "value")
                fillFunc.endControlFlow()
            }
            "kotlin.Pair" -> {
                if (type.genericParams[0].fillable)
                    fillParamType(fillFunc, type.genericParams[0], "$destination.first")
                if (type.genericParams[1].fillable)
                    fillParamType(fillFunc, type.genericParams[1], "$destination.second")
            }
            "com.ikcode.ancientstar1.core.util.UnorderedPair" -> {
                if (type.genericParams[0].fillable)
                    fillParamType(fillFunc, type.genericParams[0], "$destination.first")
                if (type.genericParams[0].fillable)
                    fillParamType(fillFunc, type.genericParams[0], "$destination.second")
            }*/
            instantiate && fill -> fillFunc.addStatement("$destination = ${unpackCall(type, data)}")
            fill && !type.isEnum -> fillFunc.addStatement("${type.name}_Packer().fillData($destination$nullAssert, session)")
        }
    }
    private fun unpackCall(type: TypeInfo, data: String) = when {
        type.isPrimitive -> "$data as ${type.name}"
        else -> "${type.name}_Packer().unpack($data, session)"
    }
}