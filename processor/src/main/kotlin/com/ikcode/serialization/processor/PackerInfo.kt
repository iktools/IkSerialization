package com.ikcode.serialization.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.ikcode.serialization.core.annotations.ReferenceOnly
import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.builders.PropertyInfo
import com.ikcode.serialization.processor.types.ClassInfo
import com.ikcode.serialization.processor.types.TypeUtil
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName

class PackerInfo(val declaration: KSClassDeclaration, types: TypeUtil, allClasses: Set<KSClassDeclaration>) {
    val namespace = declaration.packageName.asString()
    val name = declaration.simpleName.asString()
    val kpType = declaration.toClassName()
    val justType = declaration.asStarProjectedType()
    val file = declaration.containingFile!!

    val isEnum = declaration.classKind == ClassKind.ENUM_CLASS
    val isAbstract = Modifier.ABSTRACT in declaration.modifiers || declaration.classKind == ClassKind.INTERFACE

    @OptIn(KspExperimental::class)
    val isOpen = declaration.getAnnotationsByType(SerializableClass::class).first().crossModuleOpen
    val outFileName = this.name + "_Packer" //TODO read from annotation

    val constructorParams = declaration.primaryConstructor?.parameters ?: listOf()

    val subclasses = allClasses.filter {
        val type = it.asStarProjectedType()
        type != justType && justType.isAssignableFrom(type)
    }.map {
        types[it.asStarProjectedType()] as ClassInfo
    }

    val superclasses = declaration.superTypes.map {
        SuperclassInfo(it.resolve().starProjection())
    }.filter {
        Modifier.ABSTRACT in it.declaration.modifiers ||
        it.declaration is KSClassDeclaration && it.declaration.classKind == ClassKind.INTERFACE
    }.toList()

    val proxy = declaration.superTypes.map {
        it.resolve()
    }.firstOrNull {
        it.starProjection() == types.proxyType
    }

    @OptIn(KspExperimental::class)
    val allProperties = declaration.getAllProperties().filter {
        it.isAnnotationPresent(SerializationData::class)
    }.map {
        PropertyInfo(it, declaration.asStarProjectedType(), types, this.constructorParams.any { param -> param.name == it.simpleName })
    }.toList()

    @OptIn(KspExperimental::class)
    val producedData = declaration.getAllProperties().filter {
        it.isAnnotationPresent(ReferenceOnly::class)
    }.map {
        PropertyInfo(it, declaration.asStarProjectedType(), types, this.constructorParams.any { param -> param.name == it.simpleName })
    }.toList()

    val interfaceFileName = "I${this.name}_Packer"
    val interfaceType = ClassName(this.namespace, interfaceFileName)
}