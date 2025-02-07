package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntMutableMapData(
    @SerializationData val readonlyC: MutableMap<Int, Int>,
    @SerializationData var mutableC: MutableMap<Int, Int>,
    @SerializationData val nullableNullC: MutableMap<Int, Int>?,
    @SerializationData val nullableValueC: MutableMap<Int, Int>?,
) {
    @SerializationData var mutable: MutableMap<Int, Int> = hashMapOf()
    @SerializationData var nullableNull: MutableMap<Int, Int>? = hashMapOf()
    @SerializationData var nullableValue: MutableMap<Int, Int>? = hashMapOf()
}