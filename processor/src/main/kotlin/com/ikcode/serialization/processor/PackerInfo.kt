package com.ikcode.serialization.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.ikcode.serialization.core.annotations.SerializationData
import com.squareup.kotlinpoet.ksp.toClassName

class PackerInfo(declaration: KSClassDeclaration, types: TypeUtil) {
    val namespace = declaration.packageName.asString()
    val name = declaration.simpleName.asString()
    val kpType = declaration.toClassName()

    val isEnum = declaration.classKind == ClassKind.ENUM_CLASS
    val isProxy = declaration.asStarProjectedType().isAssignableFrom(types.proxyType)

    val outFileName = this.name + "_Packer"

    val constructorParams = declaration.primaryConstructor?.parameters ?: listOf()
    @OptIn(KspExperimental::class)
    val ownProperties = declaration.getDeclaredProperties().filter {
        it.isAnnotationPresent(SerializationData::class)
    }.map {
        PropertyInfo(it, types)
    }.toList()
    @OptIn(KspExperimental::class)
    val allProperties = declaration.getAllProperties().filter {
        it.isAnnotationPresent(SerializationData::class)
    }.map {
        PropertyInfo(it, types)
    }.toList()
}