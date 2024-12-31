package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntData(
    @field:SerializationData val readonlyC: Int,
    @field:SerializationData var mutableC: Int,
    @field:SerializationData val nullableNullC: Int?,
    @field:SerializationData val nullableValueC: Int?,
) {
    @field:SerializationData var mutable = 0
    @field:SerializationData var nullableNull: Int? = 0
    @field:SerializationData var nullableValue: Int? = 0
}