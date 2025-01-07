package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType

class ClassInfo(ksType: KSType): ATypeInfo(ksType) {
    override fun instantiate(data: String) = "null /* TODO */"
}