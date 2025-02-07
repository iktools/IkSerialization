package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntCollectionData(
    @SerializationData val readonlyC: Collection<Int>,
    @SerializationData var mutableC: Collection<Int>,
    @SerializationData val nullableNullC: Collection<Int>?,
    @SerializationData val nullableValueC: Collection<Int>?,
) {
    @SerializationData var mutable: Collection<Int> = listOf()
    @SerializationData var nullableNull: Collection<Int>? = listOf()
    @SerializationData var nullableValue: Collection<Int>? = listOf()
}