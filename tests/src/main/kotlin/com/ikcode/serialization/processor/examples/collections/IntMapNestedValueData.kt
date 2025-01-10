package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntMapNestedValueData(
    @SerializationData val readonlyC: List<Map<Int, Int>>,
    @SerializationData var mutableC: List<Map<Int, Int>>,
    @SerializationData val nullableNullC: List<Map<Int, Int>>?,
    @SerializationData val nullableValueC: List<Map<Int, Int>>?,
) {
    @SerializationData var mutable: MutableList<Map<Int, Int>> = mutableListOf()
    @SerializationData var nullableNull: MutableList<Map<Int, Int>>? = mutableListOf()
    @SerializationData var nullableValue: MutableList<Map<Int, Int>>? = mutableListOf()
}