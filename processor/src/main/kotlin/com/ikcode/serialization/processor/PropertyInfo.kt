package com.ikcode.serialization.processor

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

class PropertyInfo(property: KSPropertyDeclaration, types: TypeUtil, val inConstructor: Boolean) {
    val ksName = property.simpleName

    val name = property.simpleName.asString()
    val type = types[property.type.resolve()]

    val isMutable = property.isMutable
}