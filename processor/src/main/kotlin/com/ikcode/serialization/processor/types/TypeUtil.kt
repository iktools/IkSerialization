package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.findActualType
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.ikcode.serialization.core.session.IProxyPacked

class TypeUtil(
    resolver: Resolver
) {
    val proxyType = resolver
        .getClassDeclarationByName<IProxyPacked<*>>()!!
        .asStarProjectedType()

    val numbers = setOf(
        resolver.builtIns.intType,
        resolver.builtIns.byteType,
        resolver.builtIns.shortType,
        resolver.builtIns.intType,
        resolver.builtIns.longType,
        resolver.builtIns.floatType,
        resolver.builtIns.doubleType
    )
    val primitives = numbers + resolver.builtIns.booleanType + resolver.builtIns.stringType + resolver.builtIns.charType
    val collectionType = resolver
        .getClassDeclarationByName<Collection<*>>()!!
        .asStarProjectedType()
    val mapType = resolver
        .getClassDeclarationByName<Map<*, *>>()!!
        .asStarProjectedType()

    operator fun get(type: KSType) = ATypeInfo(type, this)

    fun resolve(declaration: KSDeclaration): KSClassDeclaration? = when(declaration) {
        is KSClassDeclaration -> declaration
        is KSTypeAlias -> declaration.findActualType()
        else -> null
    }
}