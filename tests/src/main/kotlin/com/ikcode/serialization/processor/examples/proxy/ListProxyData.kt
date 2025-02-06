package com.ikcode.serialization.processor.examples.proxy

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.session.IProxyPacked

@SerializableClass
class ListProxyData(
    val item1: Int,
    val item2: Int
): IProxyPacked<List<Int>> {
    constructor(data: List<Int>): this(data[0], data[1])

    override fun packingProxy() = listOf(item1, item2)
}