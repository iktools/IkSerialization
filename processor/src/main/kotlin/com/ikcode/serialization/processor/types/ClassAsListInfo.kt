package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.ikcode.serialization.processor.builders.PropertyInfo
import com.squareup.kotlinpoet.CodeBlock

class ClassAsListInfo(ksType: KSType, declaration: KSClassDeclaration, types: TypeUtil): ATypeInfo(ksType) {
    private val constructorParams = declaration.primaryConstructor?.parameters ?: listOf()
    private val allProperties = declaration.getAllProperties().map {
        PropertyInfo(it, ksType, types, this.constructorParams.any { param -> param.name == it.simpleName })
    }.toList()

    override val fillable = this.allProperties.all { it.type.fillable }

    override fun instantiate(code: CodeBlock.Builder, data: String) {
        code.add("%T(", this.kpType)

        var commaCount = this.constructorParams.size - 1
        this.constructorParams.forEach { paramName ->
            val i = this.allProperties.indexOfFirst {
                paramName.name == it.ksName
            }
            val field = this.allProperties[i]

            field.type.instantiate(code, "($data as List<*>)[$i]!!")

            if (commaCount > 0) {
                code.add(", ")
                commaCount--
            }
        }

        code.add(")")
    }

    override fun pack(code: CodeBlock.Builder, data: String) {
        val propCount = this.allProperties.size
        code.add("listOf(")

        this.allProperties.forEachIndexed { i, field ->
            field.type.pack(code, "$data.${field.name}")
            if (i < propCount - 1)
                code.add(", ")
        }

        code.add(")")
    }

    override fun fill(code: CodeBlock.Builder, data: String, destination: String, instantiate: Boolean) {
        if (instantiate)
            this.instantiate(code, data)

        for (i in this.allProperties.indices) {
            val property = this.allProperties[i]
            if (!property.type.fillable)
                continue

            if (instantiate)
                code.add("\n")
            property.type.fill(code, "$data[$i]", "$destination.${property.name}", false)
        }
    }
}