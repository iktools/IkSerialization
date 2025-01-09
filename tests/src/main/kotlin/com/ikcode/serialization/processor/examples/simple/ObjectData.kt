package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class ObjectData(
    @SerializationData val readonlyC1: ObjectSample,
    @SerializationData var mutableC1: ObjectSample,
    @SerializationData val nullableNullC1: ObjectSample?,
    @SerializationData val nullableValueC1: ObjectSample?,

    @SerializationData val readonlyC2: ObjectSample,
    @SerializationData var mutableC2: ObjectSample,
    @SerializationData val nullableNullC2: ObjectSample?,
    @SerializationData val nullableValueC2: ObjectSample?,
) {
    @SerializationData var mutable1 = ObjectSample(-1)
    @SerializationData var nullableNull1: ObjectSample? = ObjectSample(-2)
    @SerializationData var nullableValue1: ObjectSample? = ObjectSample(-3)

    @SerializationData var mutable2 = ObjectSample(-4)
    @SerializationData var nullableNull2: ObjectSample? = ObjectSample(-5)
    @SerializationData var nullableValue2: ObjectSample? = ObjectSample(-6)
}