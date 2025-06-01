package com.ikcode.serialization.processor.examples.simple

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class SelfreferencingObject {
    @SerializationData var data = 0
    @SerializationData val listData = mutableListOf<SelfreferencingObject>()
    @SerializationData val setData = mutableSetOf<SelfreferencingObject>()
    @SerializationData val mapData = mutableMapOf<SelfreferencingObject, SelfreferencingObject>()
}