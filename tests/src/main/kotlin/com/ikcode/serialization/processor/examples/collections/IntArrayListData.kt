package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntArrayListData(
    @field:SerializationData val readonlyC: ArrayList<Int>,
    @field:SerializationData var mutableC: ArrayList<Int>,
    @field:SerializationData val nullableNullC: ArrayList<Int>?,
    @field:SerializationData val nullableValueC: ArrayList<Int>?,
) {
    @field:SerializationData
    var mutable = arrayListOf<Int>()
    @field:SerializationData
    var nullableNull: ArrayList<Int>? = arrayListOf()
    @field:SerializationData
    var nullableValue: ArrayList<Int>? = arrayListOf()
}