package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class DoubleData(
    @SerializationData val readonlyC: Double,
    @SerializationData var mutableC: Double,
    @SerializationData val nullableNullC: Double?,
    @SerializationData val nullableValueC: Double?,
) {
    @SerializationData var mutable = 0.0
    @SerializationData var nullableNull: Double? = 0.0
    @SerializationData var nullableValue: Double? = 0.0
}