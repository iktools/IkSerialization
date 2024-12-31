package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class StringData(
    @field:SerializationData val readonlyC: String,
    @field:SerializationData var mutableC: String,
    @field:SerializationData val nullableNullC: String?,
    @field:SerializationData val nullableValueC: String?,
) {
    @field:SerializationData
    var mutable = ""
    @field:SerializationData
    var nullableNull: String? = ""
    @field:SerializationData
    var nullableValue: String? = ""
}