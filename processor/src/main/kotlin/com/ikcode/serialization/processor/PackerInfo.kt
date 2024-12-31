package com.ikcode.serialization.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration

class PackerInfo(declaration: KSClassDeclaration) {
    val namespace = declaration.packageName.asString()
    val name = declaration.simpleName.asString()
}