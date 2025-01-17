package com.ikcode.serialization.processor.examples.inheritance

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.ObjectSample

@SerializableClass
class ConcreteData(
    baseReadonlyC1: ObjectSample,
    baseMutableC1: ObjectSample,
    baseNullableNullC1: ObjectSample?,
    baseNullableValueC1: ObjectSample?,

    baseReadonlyC2: ObjectSample,
    baseMutableC2: ObjectSample,
    baseNullableNullC2: ObjectSample?,
    baseNullableValueC2: ObjectSample?,

    @SerializationData val derivedReadonlyC1: ObjectSample,
    @SerializationData var derivedMutableC1: ObjectSample,
    @SerializationData val derivedNullableNullC1: ObjectSample?,
    @SerializationData val derivedNullableValueC1: ObjectSample?,

    @SerializationData val derivedReadonlyC2: ObjectSample,
    @SerializationData var derivedMutableC2: ObjectSample,
    @SerializationData val derivedNullableNullC2: ObjectSample?,
    @SerializationData val derivedNullableValueC2: ObjectSample?
) : AbstractData(
    baseReadonlyC1, baseMutableC1, baseNullableNullC1, baseNullableValueC1,
    baseReadonlyC2, baseMutableC2, baseNullableNullC2, baseNullableValueC2)
{
    @SerializationData var derivedMutable1 = ObjectSample(10)
    @SerializationData var derivedNullableNull1: ObjectSample? = ObjectSample(20)
    @SerializationData var derivedNullableValue1: ObjectSample? = ObjectSample(30)

    @SerializationData var derivedMutable2 = ObjectSample(40)
    @SerializationData var derivedNullableNull2: ObjectSample? = ObjectSample(50)
    @SerializationData var derivedNullableValue2: ObjectSample? = ObjectSample(60)
}