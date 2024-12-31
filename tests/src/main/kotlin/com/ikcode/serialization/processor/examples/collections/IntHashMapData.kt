package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntHashMapData(
    @field:SerializationData val readonlyC: HashMap<Int, Int>,
    @field:SerializationData var mutableC: HashMap<Int, Int>,
    @field:SerializationData val nullableNullC: HashMap<Int, Int>?,
    @field:SerializationData val nullableValueC: HashMap<Int, Int>?,
) {
    @field:SerializationData
    var mutable: HashMap<Int, Int> = hashMapOf()
    @field:SerializationData
    var nullableNull: HashMap<Int, Int>? = hashMapOf()
    @field:SerializationData
    var nullableValue: HashMap<Int, Int>? = hashMapOf()
}