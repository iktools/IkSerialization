package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class CharData(
    @SerializationData val readonlyC: Char,
    @SerializationData var mutableC: Char,
    @SerializationData val nullableNullC: Char?,
    @SerializationData val nullableValueC: Char?,
) {
    @SerializationData var mutable = 'A'
    @SerializationData var nullableNull: Char? = 'A'
    @SerializationData var nullableValue: Char? = 'A'
}