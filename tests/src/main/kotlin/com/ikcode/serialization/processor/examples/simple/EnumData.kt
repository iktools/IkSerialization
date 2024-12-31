package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class EnumData(
    @field:SerializationData val readonlyC: EnumSample,
    @field:SerializationData var mutableC: EnumSample,
    @field:SerializationData val nullableNullC: EnumSample?,
    @field:SerializationData val nullableValueC: EnumSample?,
) {
    @field:SerializationData
    var mutable = EnumSample.Value0
    @field:SerializationData
    var nullableNull: EnumSample? =
        EnumSample.Value0
    @field:SerializationData
    var nullableValue: EnumSample? =
        EnumSample.Value0
}