package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class ListNestedIntMapData(
    @SerializationData val readonlyC: List<Map<Int, Int>>,
    @SerializationData var mutableC: List<Map<Int, Int>>,
    @SerializationData val nullableNullC: List<Map<Int, Int>>?,
    @SerializationData val nullableValueC: List<Map<Int, Int>>?,
) {
    @SerializationData var mutable: List<Map<Int, Int>> = listOf()
    @SerializationData var nullableNull: List<Map<Int, Int>>? = listOf()
    @SerializationData var nullableValue: List<Map<Int, Int>>? = listOf()
}