package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.ObjectData
import com.ikcode.serialization.processor.examples.simple.ObjectSample

@SerializableClass
class ObjectIntMapData(
    @SerializationData val readonlyC: Map<ObjectSample, Int>,
    @SerializationData var mutableC: Map<ObjectSample, Int>,
    @SerializationData val nullableNullC: Map<ObjectSample, Int>?,
    @SerializationData val nullableValueC: Map<ObjectSample, Int>?,
) {
    @SerializationData var mutable: Map<ObjectSample, Int> = hashMapOf()
    @SerializationData var nullableNull: Map<ObjectSample, Int>? = hashMapOf()
    @SerializationData var nullableValue: Map<ObjectSample, Int>? = hashMapOf()
}