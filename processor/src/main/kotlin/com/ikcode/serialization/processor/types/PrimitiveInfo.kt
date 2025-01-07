package com.ikcode.serialization.processor.types

import com.google.devtools.ksp.symbol.KSType

class PrimitiveInfo(ksType: KSType): ATypeInfo(ksType) {
    override fun instantiate(data: String) = "($data as Number).to${this.name}()"
}