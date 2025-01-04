package com.ikcode.serialization.processor

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSType
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
    val mapType = resolver
        .getClassDeclarationByName<Map<*, *>>()!!
        .asStarProjectedType()

    operator fun get(type: KSType) = TypeInfo(type, this)
}