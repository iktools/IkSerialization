package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class LongData(
    @SerializationData val readonlyC: Long,
    @SerializationData var mutableC: Long,
    @SerializationData val nullableNullC: Long?,
    @SerializationData val nullableValueC: Long?,
) {
    @SerializationData var mutable = 0L
    @SerializationData var nullableNull: Long? = 0
    @SerializationData var nullableValue: Long? = 0
}