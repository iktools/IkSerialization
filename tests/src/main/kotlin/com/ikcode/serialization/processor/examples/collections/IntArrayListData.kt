package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntArrayListData(
    @SerializationData val readonlyC: ArrayList<Int>,
    @SerializationData var mutableC: ArrayList<Int>,
    @SerializationData val nullableNullC: ArrayList<Int>?,
    @SerializationData val nullableValueC: ArrayList<Int>?,
) {
    @SerializationData var mutable = arrayListOf<Int>()
    @SerializationData var nullableNull: ArrayList<Int>? = arrayListOf()
    @SerializationData var nullableValue: ArrayList<Int>? = arrayListOf()
    @SerializationData val readonly = arrayListOf<Int>()
}