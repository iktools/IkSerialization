package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntSetData(
    @SerializationData val readonlyC: Set<Int>,
    @SerializationData var mutableC: Set<Int>,
    @SerializationData val nullableNullC: Set<Int>?,
    @SerializationData val nullableValueC: Set<Int>?,
) {
    @SerializationData
    var mutable: Set<Int> = setOf()
    @SerializationData
    var nullableNull: Set<Int>? = setOf()
    @SerializationData
    var nullableValue: Set<Int>? = setOf()
}