package com.ikcode.serialization.processor.builders

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.ikcode.serialization.processor.types.TypeUtil

class PropertyInfo(property: KSPropertyDeclaration, types: TypeUtil, val inConstructor: Boolean) {
    val ksName = property.simpleName

    val name = property.simpleName.asString()
    val type = types[property.type.resolve()]

    val isMutable = property.isMutable
}