package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntCollectionData(
    @field:SerializationData val readonlyC: Collection<Int>,
    @field:SerializationData var mutableC: Collection<Int>,
    @field:SerializationData val nullableNullC: Collection<Int>?,
    @field:SerializationData val nullableValueC: Collection<Int>?,
) {
    @field:SerializationData
    var mutable: Collection<Int> = listOf()
    @field:SerializationData
    var nullableNull: Collection<Int>? = listOf()
    @field:SerializationData
    var nullableValue: Collection<Int>? = listOf()
}