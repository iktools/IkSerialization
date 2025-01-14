package com.ikcode.serialization.processor.builders

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.ikcode.serialization.processor.types.TypeUtil

class PropertyInfo(property: KSPropertyDeclaration, containingType: KSType?, types: TypeUtil, val inConstructor: Boolean) {
    val ksName = property.simpleName

    val name = property.simpleName.asString()
    val type = types[
        if (containingType != null)
            property.asMemberOf(containingType)
        else
            property.type.resolve()
    ]

    val isMutable = property.isMutable
}