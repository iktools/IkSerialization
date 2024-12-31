package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class IntMapNestedValueData(
    @field:SerializationData val readonlyC: List<Map<Int, Int>>,
    @field:SerializationData var mutableC: List<Map<Int, Int>>,
    @field:SerializationData val nullableNullC: List<Map<Int, Int>>?,
    @field:SerializationData val nullableValueC: List<Map<Int, Int>>?,
) {
    @field:SerializationData var mutable: MutableList<Map<Int, Int>> = mutableListOf()
    @field:SerializationData var nullableNull: MutableList<Map<Int, Int>>? = mutableListOf()
    @field:SerializationData var nullableValue: MutableList<Map<Int, Int>>? = mutableListOf()
}