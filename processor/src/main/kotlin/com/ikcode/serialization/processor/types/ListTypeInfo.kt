package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType

class ListTypeInfo(ksType: KSType, types: TypeUtil): ATypeInfo(ksType) {
    val argument = types[ksType.arguments[0].type!!.resolve()]

    override fun instantiate(data: String) = "/* TODO */"
}