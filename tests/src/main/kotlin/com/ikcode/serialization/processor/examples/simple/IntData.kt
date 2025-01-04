package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntData(
    @SerializationData val readonlyC: Int,
    @SerializationData var mutableC: Int,
    @SerializationData val nullableNullC: Int?,
    @SerializationData val nullableValueC: Int?,
) {
    @SerializationData var mutable = 0
    @SerializationData var nullableNull: Int? = 0
    @SerializationData var nullableValue: Int? = 0
}