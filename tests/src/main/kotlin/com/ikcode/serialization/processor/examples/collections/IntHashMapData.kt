package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntHashMapData(
    @SerializationData val readonlyC: HashMap<Int, Int>,
    @SerializationData var mutableC: HashMap<Int, Int>,
    @SerializationData val nullableNullC: HashMap<Int, Int>?,
    @SerializationData val nullableValueC: HashMap<Int, Int>?,
) {
    @SerializationData var mutable: HashMap<Int, Int> = hashMapOf()
    @SerializationData var nullableNull: HashMap<Int, Int>? = hashMapOf()
    @SerializationData var nullableValue: HashMap<Int, Int>? = hashMapOf()
    @SerializationData val readonly = hashMapOf<Int, Int>()
}