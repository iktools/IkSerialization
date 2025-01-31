package com.ikcode.serialization.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.findActualType
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.google.devtools.ksp.symbol.Modifier
import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.builders.PropertyInfo
import com.ikcode.serialization.processor.types.ClassInfo
import com.ikcode.serialization.processor.types.TypeUtil
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName

class PackerInfo(declaration: KSClassDeclaration, types: TypeUtil, allClasses: Set<KSClassDeclaration>) {
    val namespace = declaration.packageName.asString()
    val name = declaration.simpleName.asString()
    val kpType = declaration.toClassName()
    val justType = declaration.asStarProjectedType()

    val isEnum = declaration.classKind == ClassKind.ENUM_CLASS
    val isProxy = declaration.asStarProjectedType().isAssignableFrom(types.proxyType)
    val isAbstract = Modifier.ABSTRACT in declaration.modifiers || declaration.classKind == ClassKind.INTERFACE

    @OptIn(KspExperimental::class)
    val isOpen = declaration.getAnnotationsByType(SerializableClass::class).first().isOpen
    val outFileName = this.name + "_Packer" //TODO read from annotation

    val constructorParams = declaration.primaryConstructor?.parameters ?: listOf()
    val subclasses = allClasses.filter {
        val type = it.asStarProjectedType()
        type != justType && justType.isAssignableFrom(type)
    }.map {
        types[it.asStarProjectedType()] as ClassInfo
    }
    @OptIn(KspExperimental::class)
    val superclasses = declaration.getAllSuperTypes().map {
        it.starProjection()
    }.filter {
        val declaration = when (val declaration = it.declaration) {
            is KSTypeAlias -> declaration.findActualType()
            else -> declaration
        }
        val annotation = declaration.getAnnotationsByType(SerializableClass::class).firstOrNull()
        if (annotation != null && annotation.isOpen)
            logger.warn("Oho ${this.name} vs $it $declaration")

        annotation != null && annotation.isOpen && (
            Modifier.ABSTRACT in declaration.modifiers || (
                declaration is KSClassDeclaration && declaration.classKind == ClassKind.INTERFACE
            )
        )
    }.toList()
    /*.filter {
        val type = it.asStarProjectedType()
        type != justType && type.isAssignableFrom(justType)
    }.map {
        types[it.asStarProjectedType()] as ClassInfo
    }*/

    @OptIn(KspExperimental::class)
    val allProperties = declaration.getAllProperties().filter {
        it.isAnnotationPresent(SerializationData::class)
    }.map {
        PropertyInfo(it, declaration.asStarProjectedType(), types, this.constructorParams.any { param -> param.name == it.simpleName })
    }.toList()

    val interfaceFileName = "I${this.name}_Packer"
    val interfaceType = ClassName(this.namespace, interfaceFileName)

    //TODO
    val referenceOnlyFields = emptyList<PropertyInfo>()
}