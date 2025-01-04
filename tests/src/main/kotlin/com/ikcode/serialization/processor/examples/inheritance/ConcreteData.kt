package com.ikcode.serialization.processor.examples.inheritance

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.ObjectSample

//TODO
/*@SerializableClass
class ConcreteData(
    baseReadonlyC1: ObjectSample,
    baseMutableC1: ObjectSample,
    baseNullableNullC1: ObjectSample?,
    baseNullableValueC1: ObjectSample?,

    baseReadonlyC2: ObjectSample,
    baseMutableC2: ObjectSample,
    baseNullableNullC2: ObjectSample?,
    baseNullableValueC2: ObjectSample?,

    @field:SerializationData val derivedReadonlyC1: ObjectSample,
    @field:SerializationData var derivedMutableC1: ObjectSample,
    @field:SerializationData val derivedNullableNullC1: ObjectSample?,
    @field:SerializationData val derivedNullableValueC1: ObjectSample?,

    @field:SerializationData val derivedReadonlyC2: ObjectSample,
    @field:SerializationData var derivedMutableC2: ObjectSample,
    @field:SerializationData val derivedNullableNullC2: ObjectSample?,
    @field:SerializationData val derivedNullableValueC2: ObjectSample?
) : AbstractData(
    baseReadonlyC1, baseMutableC1, baseNullableNullC1, baseNullableValueC1,
    baseReadonlyC2, baseMutableC2, baseNullableNullC2, baseNullableValueC2)
{
    @field:SerializationData
    var derivedMutable1 = ObjectSample()
    @field:SerializationData
    var derivedNullableNull1: ObjectSample? =
        ObjectSample()
    @field:SerializationData
    var derivedNullableValue1: ObjectSample? =
        ObjectSample()

    @field:SerializationData
    var derivedMutable2 = ObjectSample()
    @field:SerializationData
    var derivedNullableNull2: ObjectSample? =
        ObjectSample()
    @field:SerializationData
    var derivedNullableValue2: ObjectSample? =
        ObjectSample()
}*/