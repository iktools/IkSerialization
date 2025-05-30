package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class ListNestedMutableIntMapData(
    @SerializationData val readonlyC: MutableList<MutableMap<Int, Int>>,
    @SerializationData var mutableC: MutableList<MutableMap<Int, Int>>,
    @SerializationData val nullableNullC: MutableList<MutableMap<Int, Int>>?,
    @SerializationData val nullableValueC: MutableList<MutableMap<Int, Int>>?,
) {
    @SerializationData var mutable: MutableList<MutableMap<Int, Int>> = mutableListOf()
    @SerializationData var nullableNull: MutableList<MutableMap<Int, Int>>? = mutableListOf()
    @SerializationData var nullableValue: MutableList<MutableMap<Int, Int>>? = mutableListOf()
    @SerializationData val readonly = mutableListOf<MutableMap<Int, Int>>()
}