package com.ikcode.serialization.processor

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ksp.toClassName

class PackerInfo(declaration: KSClassDeclaration, proxyType: KSType) {
    val namespace = declaration.packageName.asString()
    val name = declaration.simpleName.asString()
    val kpType = declaration.toClassName()

    val isEnum = declaration.classKind == ClassKind.ENUM_CLASS
    val isProxy = declaration.asStarProjectedType().isAssignableFrom(proxyType)

    val outFileName = this.name + "_Packer"
}