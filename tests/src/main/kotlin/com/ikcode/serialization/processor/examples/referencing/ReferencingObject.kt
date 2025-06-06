package com.ikcode.serialization.processor.examples.referencing

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.FillableObject

@SerializableClass
class ReferencingObject {
    @SerializationData val data = FillableObject()
    @SerializationData val nullableNotNull: FillableObject? = FillableObject()
    @SerializationData val nullableNull: FillableObject? = null
    @SerializationData val listData = mutableListOf<FillableObject>()
    @SerializationData val setData = mutableSetOf<FillableObject>()
    @SerializationData val mapData = mutableMapOf<FillableObject, FillableObject>()
}