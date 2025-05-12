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
}