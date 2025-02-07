package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.ObjectSample

@SerializableClass
class IntObjectMapData(
    @SerializationData val readonlyC: Map<Int, ObjectSample>,
    @SerializationData var mutableC: Map<Int, ObjectSample>,
    @SerializationData val nullableNullC: Map<Int, ObjectSample>?,
    @SerializationData val nullableValueC: Map<Int, ObjectSample>?,
) {
    @SerializationData var mutable: Map<Int, ObjectSample> = hashMapOf()
    @SerializationData var nullableNull: Map<Int, ObjectSample>? = hashMapOf()
    @SerializationData var nullableValue: Map<Int, ObjectSample>? = hashMapOf()
}