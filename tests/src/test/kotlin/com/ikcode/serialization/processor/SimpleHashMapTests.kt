package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleHashMapTests {
    //TODO tests where one of key or value is a primitive and other is a reference

    //TODO
    /*@Test
    fun intHashMapTests() {
        val data = IntHashMapData(hashMapOf(1 to 10), hashMapOf(2 to 20), null, hashMapOf(3 to 30)).apply {
            mutable = hashMapOf(4 to 40)
            nullableNull = null
            nullableValue = hashMapOf(5 to 50)
        }
        val session = PackingSession()
        val pointer = IntHashMapData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(mapOf(1 to 10), packed["readonlyC"])
        assertEquals(mapOf(2 to 20), packed["mutableC"])
        assertEquals(mapOf(3 to 30), packed["nullableValueC"])
        assertEquals(mapOf(4 to 40), packed["mutable"])
        assertEquals(mapOf(5 to 50), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntHashMapData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(hashMapOf(1 to 10), unpacked.readonlyC)
        assertEquals(hashMapOf(2 to 20), unpacked.mutableC)
        assertEquals(hashMapOf(3 to 30), unpacked.nullableValueC)
        assertEquals(hashMapOf(4 to 40), unpacked.mutable)
        assertEquals(hashMapOf(5 to 50), unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }*/
}