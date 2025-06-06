package com.ikcode.serialization.processor.examples.referencing

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class SelfreferencingObject {
    @SerializationData var id = 0
    @SerializationData val listData = mutableListOf<SelfreferencingObject>()
    @SerializationData val setData = mutableSetOf<SelfreferencingObject>()
    @SerializationData val mapData = mutableMapOf<SelfreferencingObject, SelfreferencingObject>()
}