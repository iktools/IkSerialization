package com.ikcode.serialization.processor

import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.proxy.ListProxyData
import com.ikcode.serialization.processor.examples.proxy.ListProxyData_Packer
import com.ikcode.serialization.processor.examples.proxy.MapProxyData
import com.ikcode.serialization.processor.examples.proxy.MapProxyData_Packer
import kotlin.test.Test
import kotlin.test.assertEquals

class ProxyTests {
    @Test
    fun listProxyTest() {
        val data = ListProxyData(1, 2)
        val session = PackingSession()
        val packed = ListProxyData_Packer().pack(data, session) as List<*>

        assertEquals(1, packed[0])
        assertEquals(2, packed[1])

        val unpacked = ListProxyData_Packer().unpack(packed, UnpackingSession(session.referencedData))

        assertEquals(1, unpacked.item1)
        assertEquals(2, unpacked.item2)
    }

    @Test
    fun mapProxyTest() {
        val data = MapProxyData(1, 2)
        val session = PackingSession()
        val packed = MapProxyData_Packer().pack(data, session) as Map<*, *>

        assertEquals(1, packed["one"])
        assertEquals(2, packed["two"])

        val unpacked = MapProxyData_Packer().unpack(packed, UnpackingSession(session.referencedData))

        assertEquals(1, unpacked.item1)
        assertEquals(2, unpacked.item2)
    }
}