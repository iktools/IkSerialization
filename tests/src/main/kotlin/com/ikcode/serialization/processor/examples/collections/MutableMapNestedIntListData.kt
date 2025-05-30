package com.ikcode.serialization.processor.examples.collections

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class MutableMapNestedIntListData(
    @SerializationData val readonlyC: MutableMap<MutableList<Int>, MutableList<Int>>,
    @SerializationData var mutableC: MutableMap<MutableList<Int>, MutableList<Int>>,
    @SerializationData val nullableNullC: MutableMap<MutableList<Int>, MutableList<Int>>?,
    @SerializationData val nullableValueC: MutableMap<MutableList<Int>, MutableList<Int>>?,
) {
    @SerializationData var mutable: MutableMap<MutableList<Int>, MutableList<Int>> = mutableMapOf()
    @SerializationData var nullableNull: MutableMap<MutableList<Int>, MutableList<Int>>? = mutableMapOf()
    @SerializationData var nullableValue: MutableMap<MutableList<Int>, MutableList<Int>>? = mutableMapOf()
    @SerializationData val readonly = mutableMapOf<MutableList<Int>, MutableList<Int>>()
}