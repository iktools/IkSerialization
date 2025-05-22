package com.ikcode.serialization.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.findActualType
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.ikcode.serialization.core.annotations.SerializableClass
import com.squareup.kotlinpoet.ksp.toTypeName

class SuperclassInfo(val type: KSType) {
    val declaration = when (val superDeclaration = type.declaration) {
        is KSTypeAlias -> superDeclaration.findActualType()
        else -> superDeclaration
    }
    val kpType = type.toTypeName()
    val namespace = declaration.packageName.asString()
    val name = type.toString()

    @OptIn(KspExperimental::class)
    val annotation = this.declaration.getAnnotationsByType(SerializableClass::class).firstOrNull()
    val isOpen = annotation != null && annotation.isOpen

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SuperclassInfo

        if (namespace != other.namespace) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = namespace.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}