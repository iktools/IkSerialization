package com.ikcode.serialization.processor

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

class PropertyInfo(property: KSPropertyDeclaration, types: TypeUtil) {
    val name = property.simpleName
    val type = types[property.type.resolve()]

    val isMutable = property.isMutable
}