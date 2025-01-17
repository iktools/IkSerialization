package com.ikcode.serialization.processor.examples.inheritance

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.ObjectSample

@SerializableClass
abstract class AbstractData(
    @SerializationData val baseReadonlyC1: ObjectSample,
    @SerializationData var baseMutableC1: ObjectSample,
    @SerializationData val baseNullableNullC1: ObjectSample?,
    @SerializationData val baseNullableValueC1: ObjectSample?,

    @SerializationData val baseReadonlyC2: ObjectSample,
    @SerializationData var baseMutableC2: ObjectSample,
    @SerializationData val baseNullableNullC2: ObjectSample?,
    @SerializationData val baseNullableValueC2: ObjectSample?
) {
    @SerializationData var baseMutable1 = ObjectSample(1)
    @SerializationData var baseNullableNull1: ObjectSample? = ObjectSample(2)
    @SerializationData var baseNullableValue1: ObjectSample? = ObjectSample(3)

    @SerializationData var baseMutable2 = ObjectSample(4)
    @SerializationData var baseNullableNull2: ObjectSample? = ObjectSample(5)
    @SerializationData var baseNullableValue2: ObjectSample? = ObjectSample(6)
}