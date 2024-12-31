package com.ikcode.serialization.processor.examples.inheritance

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.ObjectSample

@SerializableClass
class ImplementationData(
    @field:SerializationData val readonlyC1: ObjectSample,
    @field:SerializationData var mutableC1: ObjectSample,
    @field:SerializationData val nullableNullC1: ObjectSample?,
    @field:SerializationData val nullableValueC1: ObjectSample?,

    @field:SerializationData val readonlyC2: ObjectSample,
    @field:SerializationData var mutableC2: ObjectSample,
    @field:SerializationData val nullableNullC2: ObjectSample?,
    @field:SerializationData val nullableValueC2: ObjectSample?,
): InterfaceSample {
    @field:SerializationData
    var mutable1 = ObjectSample()
    @field:SerializationData
    var nullableNull1: ObjectSample? =
        ObjectSample()
    @field:SerializationData
    var nullableValue1: ObjectSample? =
        ObjectSample()

    @field:SerializationData
    var mutable2 = ObjectSample()
    @field:SerializationData
    var nullableNull2: ObjectSample? =
        ObjectSample()
    @field:SerializationData
    var nullableValue2: ObjectSample? =
        ObjectSample()
}