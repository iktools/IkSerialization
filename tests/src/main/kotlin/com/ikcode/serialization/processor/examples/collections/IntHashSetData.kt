package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntHashSetData(
    @SerializationData val readonlyC: HashSet<Int>,
    @SerializationData var mutableC: HashSet<Int>,
    @SerializationData val nullableNullC: HashSet<Int>?,
    @SerializationData val nullableValueC: HashSet<Int>?,
) {
    @SerializationData var mutable: HashSet<Int> = hashSetOf()
    @SerializationData var nullableNull: HashSet<Int>? = hashSetOf()
    @SerializationData var nullableValue: HashSet<Int>? = hashSetOf()
    @SerializationData val readonly = hashSetOf<Int>()
}