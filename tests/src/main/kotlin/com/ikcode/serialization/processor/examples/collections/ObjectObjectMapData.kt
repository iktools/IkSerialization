package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.ObjectData
import com.ikcode.serialization.processor.examples.simple.ObjectSample

@SerializableClass
class ObjectObjectMapData(
    @SerializationData val readonlyC: Map<ObjectSample, ObjectSample>,
    @SerializationData var mutableC: Map<ObjectSample, ObjectSample>,
    @SerializationData val nullableNullC: Map<ObjectSample, ObjectSample>?,
    @SerializationData val nullableValueC: Map<ObjectSample, ObjectSample>?,
) {
    @SerializationData var mutable: Map<ObjectSample, ObjectSample> = hashMapOf()
    @SerializationData var nullableNull: Map<ObjectSample, ObjectSample>? = hashMapOf()
    @SerializationData var nullableValue: Map<ObjectSample, ObjectSample>? = hashMapOf()
}