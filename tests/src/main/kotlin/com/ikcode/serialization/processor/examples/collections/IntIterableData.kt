package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntIterableData(
    @SerializationData val readonlyC: Iterable<Int>,
    @SerializationData var mutableC: Iterable<Int>,
    @SerializationData val nullableNullC: Iterable<Int>?,
    @SerializationData val nullableValueC: Iterable<Int>?,
) {
    @SerializationData var mutable: Iterable<Int> = listOf()
    @SerializationData var nullableNull: Iterable<Int>? = listOf()
    @SerializationData var nullableValue: Iterable<Int>? = listOf()
}