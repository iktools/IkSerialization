package com.ikcode.serialization.processor.examples.referencing

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.processor.examples.simple.FillableObject

@SerializableClass
interface IReferencing {
    val data: FillableObject
    val nullableNotNull: FillableObject?
    val nullableNull: FillableObject?
    val listData: MutableList<FillableObject>
    val setData: MutableSet<FillableObject>
    val mapData: MutableMap<FillableObject, FillableObject>
}