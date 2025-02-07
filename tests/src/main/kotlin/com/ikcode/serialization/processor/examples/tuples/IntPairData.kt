package com.ikcode.serialization.processor.examples.tuples

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntPairData(
    @SerializationData val readonlyC: Pair<Int, Int>,
    @SerializationData var mutableC: Pair<Int, Int>,
    @SerializationData val nullableNullC: Pair<Int, Int>?,
    @SerializationData val nullableValueC: Pair<Int, Int>?,
) {
    @SerializationData var mutable = 0 to 0
    @SerializationData var nullableNull: Pair<Int, Int>? = 0 to 0
    @SerializationData var nullableValue: Pair<Int, Int>? = 0 to 0
}