package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class FloatData(
    @SerializationData val readonlyC: Float,
    @SerializationData var mutableC: Float,
    @SerializationData val nullableNullC: Float?,
    @SerializationData val nullableValueC: Float?,
) {
    @SerializationData var mutable = 0f
    @SerializationData var nullableNull: Float? = 0f
    @SerializationData var nullableValue: Float? = 0f
}