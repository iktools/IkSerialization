package com.ikcode.serialization.processor.examples.referencing

import com.ikcode.serialization.core.annotations.ReferenceOnly
import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData
import com.ikcode.serialization.processor.examples.simple.FillableObject

@SerializableClass
class ReferenceOnlyData {
    @ReferenceOnly
    val producedData = FillableObject()

    @SerializationData
    val stateData = ReferencingObject()
}