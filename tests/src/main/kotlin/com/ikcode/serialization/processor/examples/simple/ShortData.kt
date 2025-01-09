package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class ShortData(
    @SerializationData val readonlyC: Short,
    @SerializationData var mutableC: Short,
    @SerializationData val nullableNullC: Short?,
    @SerializationData val nullableValueC: Short?,
) {
    @SerializationData var mutable = 0.toShort()
    @SerializationData var nullableNull: Short? = 0
    @SerializationData var nullableValue: Short? = 0
}