package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntMutableListData(
    @SerializationData val readonlyC: MutableList<Int>,
    @SerializationData var mutableC: MutableList<Int>,
    @SerializationData val nullableNullC: MutableList<Int>?,
    @SerializationData val nullableValueC: MutableList<Int>?,
) {
    @SerializationData var mutable = mutableListOf<Int>()
    @SerializationData var nullableNull: MutableList<Int>? = mutableListOf()
    @SerializationData var nullableValue: MutableList<Int>? = mutableListOf()
}