package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntMapData(
    @SerializationData val readonlyC: Map<Int, Int>,
    @SerializationData var mutableC: Map<Int, Int>,
    @SerializationData val nullableNullC: Map<Int, Int>?,
    @SerializationData val nullableValueC: Map<Int, Int>?,
) {
    @SerializationData var mutable: Map<Int, Int> = hashMapOf()
    @SerializationData var nullableNull: Map<Int, Int>? = hashMapOf()
    @SerializationData var nullableValue: Map<Int, Int>? = hashMapOf()
}