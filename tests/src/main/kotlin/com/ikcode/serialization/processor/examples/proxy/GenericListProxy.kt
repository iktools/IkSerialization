package com.ikcode.serialization.processor.examples.proxy

import com.ikcode.serialization.core.session.IProxyPacked

class GenericListProxy<T>(
    val item: T,
): IProxyPacked<List<T>> {
    constructor(data: List<T>): this(data[0])

    override fun packingProxy() = listOf(this.item)
}