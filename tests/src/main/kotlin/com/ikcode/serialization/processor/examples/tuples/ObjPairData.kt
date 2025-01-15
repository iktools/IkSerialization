package com.ikcode.serialization.processor.examples.tuples

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.ObjectSample

@SerializableClass
class ObjPairData(
    @SerializationData val readonlyC1: Pair<Int, ObjectSample>,
    @SerializationData val readonlyC2: Pair<ObjectSample, Int>,
    @SerializationData var mutableC1: Pair<Int, ObjectSample>,
    @SerializationData var mutableC2: Pair<ObjectSample, Int>,
    @SerializationData val nullableNullC1: Pair<Int, ObjectSample>?,
    @SerializationData val nullableNullC2: Pair<ObjectSample, Int>?,
    @SerializationData val nullableValueC1: Pair<Int, ObjectSample>?,
    @SerializationData val nullableValueC2: Pair<ObjectSample, Int>?,
) {
    @SerializationData var mutable1 = 0 to ObjectSample(0)
    @SerializationData var mutable2 = ObjectSample(0) to 0
    @SerializationData var nullableNull1: Pair<Int, ObjectSample>? = null
    @SerializationData var nullableNull2: Pair<ObjectSample, Int>? = null
    @SerializationData var nullableValue1: Pair<Int, ObjectSample>? = null
    @SerializationData var nullableValue2: Pair<ObjectSample, Int>? = null
}