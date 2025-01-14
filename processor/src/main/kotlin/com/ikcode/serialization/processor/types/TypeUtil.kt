package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.findActualType
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.ikcode.serialization.core.session.IProxyPacked

class TypeUtil(
    resolver: Resolver
) {
    val proxyType = resolver
        .getClassDeclarationByName<IProxyPacked<*>>()!!
        .asStarProjectedType()

    private val numbers = setOf(
        resolver.builtIns.intType,
        resolver.builtIns.byteType,
        resolver.builtIns.shortType,
        resolver.builtIns.intType,
        resolver.builtIns.longType,
        resolver.builtIns.floatType,
        resolver.builtIns.doubleType
    )
    private val primitives = numbers + resolver.builtIns.booleanType + resolver.builtIns.stringType + resolver.builtIns.charType

    private val iterableType = resolver
        .getClassDeclarationByName<Iterable<*>>()!!
        .asStarProjectedType()
    private val mutableIterableType = resolver
        .getClassDeclarationByName<MutableIterable<*>>()!!
        .asStarProjectedType()
    private val setType = resolver
        .getClassDeclarationByName<Set<*>>()!!
        .asStarProjectedType()
    private val mapType = resolver
        .getClassDeclarationByName<Map<*, *>>()!!
        .asStarProjectedType()
    private val mutableMapType = resolver
        .getClassDeclarationByName<MutableMap<*, *>>()!!
        .asStarProjectedType()

    private val pairType = resolver
        .getClassDeclarationByName<Pair<*, *>>()!!
        .asStarProjectedType()

    operator fun get(type: KSType): ATypeInfo {
        val justType = type.starProjection().makeNotNullable()
        val classDeclaration = when(val declaration = type.declaration) {
            is KSClassDeclaration -> declaration
            is KSTypeAlias -> declaration.findActualType()
            else -> null
        }
        val concrete = classDeclaration?.getConstructors()?.any() == true

        return when {
            justType in this.numbers -> NumberInfo(type)
            justType in this.primitives -> PrimitiveInfo(type)
            classDeclaration?.classKind == ClassKind.ENUM_CLASS -> EnumInfo(type)
            this.mapType.isAssignableFrom(justType) -> MapTypeInfo(
                type,
                concrete,
                this.mutableMapType.isAssignableFrom(justType),
                this
            )
            this.iterableType.isAssignableFrom(justType) -> ListTypeInfo(
                type,
                concrete,
                this.mutableIterableType.isAssignableFrom(justType),
                this.setType.isAssignableFrom(justType),
                this
            )
            this.pairType == justType -> ClassAsListInfo(type, classDeclaration!!, this)
            else -> ClassInfo(type)
        }
    }

}