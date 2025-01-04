package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class StringData(
    @SerializationData val readonlyC: String,
    @SerializationData var mutableC: String,
    @SerializationData val nullableNullC: String?,
    @SerializationData val nullableValueC: String?,
) {
    @SerializationData
    var mutable = ""
    @SerializationData
    var nullableNull: String? = ""
    @SerializationData
    var nullableValue: String? = ""
}