package com.ikcode.serialization.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.toClassName

class PackerInfo(declaration: KSClassDeclaration) {
    val namespace = declaration.packageName.asString()
    val name = declaration.simpleName.asString()
    val kpType = declaration.toClassName()
}