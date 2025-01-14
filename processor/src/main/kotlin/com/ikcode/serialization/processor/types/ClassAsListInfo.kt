package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.builders.PropertyInfo
import com.ikcode.serialization.processor.logger
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ksp.toClassName
import kotlin.math.log

class ClassAsListInfo(ksType: KSType, declaration: KSClassDeclaration, types: TypeUtil): ATypeInfo(ksType) {
    private val constructorParams = declaration.primaryConstructor?.parameters ?: listOf()
    private val allProperties = declaration.getAllProperties().map {
        PropertyInfo(it, ksType, types, this.constructorParams.any { param -> param.name == it.simpleName })
    }.toList()

    override val fillable get() = false

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        code.add("%T(", this.kpType)

        val paramCount = this.constructorParams.size
        this.constructorParams.forEachIndexed { i, paramName ->
            val field = this.allProperties.first {
                paramName.name == it.ksName
            }

            val data = "objData[$i]!!"

            if (field.type.isNullable)
                code.add("if (objData.containsKey(\"${field.name}\")) ")

            field.type.instantiate(code, data)

            if (field.type.isNullable)
                code.add(" else null")

            if (i < paramCount - 1)
                code.add(",")

            /*if (paramCount > multilineConstructor)
                code.add("\n")
            else
                code.add(" ")*/
        }

        code.add(")")
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        code.add(data)
    }

    override fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean) {
        if (!instantiate)
            throw IllegalArgumentException("Number can't be filled")

        code.add("($data as Number).to${this.name}()")
    }
}