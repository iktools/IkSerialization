package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class ByteData(
    @SerializationData val readonlyC: Byte,
    @SerializationData var mutableC: Byte,
    @SerializationData val nullableNullC: Byte?,
    @SerializationData val nullableValueC: Byte?,
) {
    @SerializationData var mutable: Byte = 0
    @SerializationData var nullableNull: Byte? = 0
    @SerializationData var nullableValue: Byte? = 0
}