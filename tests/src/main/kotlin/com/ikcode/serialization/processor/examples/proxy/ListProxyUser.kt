package com.ikcode.serialization.processor.examples.proxy

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.annotations.SerializationData

@SerializableClass
class ListProxyUser(
    @SerializationData val intData: GenericListProxy<Int>
) {
}