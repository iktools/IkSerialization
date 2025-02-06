package com.ikcode.serialization.processor.examples.proxy

import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.session.IProxyPacked

@SerializableClass
class MapProxyData(
    val item1: Int,
    val item2: Int
): IProxyPacked<Map<String, Int>> {
    constructor(data: Map<String, Int>): this(data["one"]!!, data["two"]!!)

    override fun packingProxy() = mapOf("one" to item1, "two" to item2)
}