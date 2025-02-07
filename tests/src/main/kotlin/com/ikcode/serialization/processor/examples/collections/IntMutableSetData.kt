package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntMutableSetData(
    @SerializationData val readonlyC: MutableSet<Int>,
    @SerializationData var mutableC: MutableSet<Int>,
    @SerializationData val nullableNullC: MutableSet<Int>?,
    @SerializationData val nullableValueC: MutableSet<Int>?,
) {
    @SerializationData var mutable: MutableSet<Int> = hashSetOf()
    @SerializationData var nullableNull: MutableSet<Int>? = hashSetOf()
    @SerializationData var nullableValue: MutableSet<Int>? = hashSetOf()
}