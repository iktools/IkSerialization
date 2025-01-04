package com.ikcode.serialization.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import org.jetbrains.annotations.Nullable

class PropertyInfo(property: KSPropertyDeclaration, types: TypeUtil) {
    val name = property.simpleName
    val type = types[property.type.resolve()]

    @OptIn(KspExperimental::class)
    val isNullable = property.isAnnotationPresent(Nullable::class)
}