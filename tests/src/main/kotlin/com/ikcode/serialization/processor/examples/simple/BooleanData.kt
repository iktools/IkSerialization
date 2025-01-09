package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class BooleanData(
    @SerializationData val readonlyC1: Boolean,
    @SerializationData val readonlyC2: Boolean,
    @SerializationData var mutableC1: Boolean,
    @SerializationData var mutableC2: Boolean,
    @SerializationData val nullableNullC: Boolean?,
    @SerializationData val nullableValueC1: Boolean?,
    @SerializationData val nullableValueC2: Boolean?,
) {
    @SerializationData var mutable1 = false
    @SerializationData var mutable2 = true
    @SerializationData var nullableNull: Boolean? = false
    @SerializationData var nullableValue1: Boolean? = false
    @SerializationData var nullableValue2: Boolean? = true
}