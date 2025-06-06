package com.ikcode.serialization.processor.examples.referencing

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class DeepReferencingObject {
    @SerializationData val data = ReferencingObject()
    @SerializationData val abstractData: AReferencing = ReferencingObject()
    @SerializationData val interfaceData: IReferencing = ReferencingObject()
    @SerializationData val nullableNotNull: ReferencingObject? = ReferencingObject()
    @SerializationData val nullableNull: ReferencingObject? = null
    @SerializationData val listData = mutableListOf<ReferencingObject>()
    @SerializationData val setData = mutableSetOf<ReferencingObject>()
    @SerializationData val mapData = mutableMapOf<ReferencingObject, ReferencingObject>()
}