package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class EnumData(
    @SerializationData val readonlyC: EnumSample,
    @SerializationData var mutableC: EnumSample,
    @SerializationData val nullableNullC: EnumSample?,
    @SerializationData val nullableValueC: EnumSample?,
) {
    @SerializationData
    var mutable = EnumSample.Value0
    @SerializationData
    var nullableNull: EnumSample? =
        EnumSample.Value0
    @SerializationData
    var nullableValue: EnumSample? =
        EnumSample.Value0
}