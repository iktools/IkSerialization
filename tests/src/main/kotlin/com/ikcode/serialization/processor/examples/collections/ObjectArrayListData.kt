package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.ObjectSample

/*@SerializableClass
class ObjectArrayListData(
    @SerializationData val readonlyC: ArrayList<ObjectSample>,
    @SerializationData var mutableC: ArrayList<ObjectSample>,
    @SerializationData val nullableNullC: ArrayList<ObjectSample>?,
    @SerializationData val nullableValueC: ArrayList<ObjectSample>?,
) {
    @SerializationData var mutable = arrayListOf<ObjectSample>()
    @SerializationData var nullableNull: ArrayList<ObjectSample>? = arrayListOf()
    @SerializationData var nullableValue: ArrayList<ObjectSample>? = arrayListOf()
}*/