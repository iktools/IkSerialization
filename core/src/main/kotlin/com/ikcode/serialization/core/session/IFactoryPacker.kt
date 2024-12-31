package com.ikcode.serialization.core.session

import com.ikcode.serialization.core.references.ReferencePointer

interface IFactoryPacker {
    fun typeName(): String

    fun produce(pointer: ReferencePointer, session: UnpackingSession): Any
}