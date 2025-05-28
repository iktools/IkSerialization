package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.ObjectData
import com.ikcode.serialization.processor.examples.simple.ObjectSample

@SerializableClass
class ObjectIntMutableMapData(
    @SerializationData val readonlyC: MutableMap<ObjectSample, Int>,
    @SerializationData var mutableC: MutableMap<ObjectSample, Int>,
    @SerializationData val nullableNullC: MutableMap<ObjectSample, Int>?,
    @SerializationData val nullableValueC: MutableMap<ObjectSample, Int>?,
) {
    @SerializationData var mutable: MutableMap<ObjectSample, Int> = hashMapOf()
    @SerializationData var nullableNull: MutableMap<ObjectSample, Int>? = hashMapOf()
    @SerializationData var nullableValue: MutableMap<ObjectSample, Int>? = hashMapOf()
    @SerializationData val readonly: MutableMap<ObjectSample, Int> = hashMapOf()
}