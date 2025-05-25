package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.findActualType
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
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

    private val iterableType = starType<Iterable<*>>(resolver)
    private val mutableIterableType = starType<MutableIterable<*>>(resolver)
    private val setType = starType<Set<*>>(resolver)
    private val mapType = starType<Map<*, *>>(resolver)
    private val mutableMapType = starType<MutableMap<*, *>>(resolver)
    private val pairType = starType<Pair<*, *>>(resolver)

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
            this.proxyType.isAssignableFrom(justType) -> ProxyTypeInfo(
                type,
                get(classDeclaration!!.superTypes.map {
                    it.resolve()
                } .first {
                    this.proxyType.isAssignableFrom(it)
                }.let {
                    substituteGenerics(
                        it.arguments[0].type!!.resolve(),
                        classDeclaration.typeParameters.zip(type.arguments).associate { (arg, argType) ->
                            arg.name.getShortName() to argType
                        }
                    )
                })
            )
            else -> ClassInfo(type)
        }
    }

    companion object {
        private inline fun <reified T> starType(resolver: Resolver) = resolver
            .getClassDeclarationByName<T>()!!
            .asStarProjectedType()

        private fun substituteGenerics(type: KSType, substitutionMap: Map<String, KSTypeArgument>): KSType {
            return (type.declaration as KSClassDeclaration).asType(type.arguments.map {
                substitutionMap[it.type?.resolve()?.declaration?.simpleName?.getShortName()] ?: it
            })
        }
    }
}