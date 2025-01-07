package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType

class EnumInfo(ksType: KSType): ATypeInfo(ksType) {
    override fun instantiate(data: String) = "${this.name}.entries[$data as Int]"
}