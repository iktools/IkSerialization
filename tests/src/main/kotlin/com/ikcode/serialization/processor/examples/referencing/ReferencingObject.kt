package com.ikcode.serialization.processor.examples.referencing

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.FillableObject

@SerializableClass
class ReferencingObject: AReferencing() {
    @SerializationData
    override val data = FillableObject()
    @SerializationData
    override val nullableNotNull: FillableObject? = FillableObject()
    @SerializationData
    override val nullableNull: FillableObject? = null
    @SerializationData
    override val listData = mutableListOf<FillableObject>()
    @SerializationData
    override val setData = mutableSetOf<FillableObject>()
    @SerializationData
    override val mapData = mutableMapOf<FillableObject, FillableObject>()
}