package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class FloatData(
    @field:SerializationData val readonlyC: Float,
    @field:SerializationData var mutableC: Float,
    @field:SerializationData val nullableNullC: Float?,
    @field:SerializationData val nullableValueC: Float?,
) {
    @field:SerializationData
    var mutable = 0f
    @field:SerializationData
    var nullableNull: Float? = 0f
    @field:SerializationData
    var nullableValue: Float? = 0f
}