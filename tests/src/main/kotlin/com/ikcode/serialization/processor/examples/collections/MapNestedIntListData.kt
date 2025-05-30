package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class MapNestedIntListData(
    @SerializationData val readonlyC: Map<List<Int>, List<Int>>,
    @SerializationData var mutableC: Map<List<Int>, List<Int>>,
    @SerializationData val nullableNullC: Map<List<Int>, List<Int>>?,
    @SerializationData val nullableValueC: Map<List<Int>, List<Int>>?,
) {
    @SerializationData var mutable: Map<List<Int>, List<Int>> = mapOf()
    @SerializationData var nullableNull: Map<List<Int>, List<Int>>? = mapOf()
    @SerializationData var nullableValue: Map<List<Int>, List<Int>>? = mapOf()
}