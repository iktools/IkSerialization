package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class ReferencingObject {
    @SerializationData val data = FillableObject()
    @SerializationData val nullableNotNull: FillableObject? = FillableObject()
    @SerializationData val nullableNull: FillableObject? = null
    @SerializationData val listData = mutableListOf<FillableObject>()
    @SerializationData val setData = mutableSetOf<FillableObject>()
    @SerializationData val mapData = mutableMapOf<FillableObject, FillableObject>()
}