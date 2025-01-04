package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleHashSetTests {

    //TODO
    /*@Test
    fun intHashSetTests() {
        val data = IntHashSetData(
            hashSetOf(1),
            hashSetOf(2),
            null,
            hashSetOf(3)
        ).apply {
            mutable = hashSetOf(4)
            nullableNull = null
            nullableValue = hashSetOf(5)
        }
        val session = PackingSession()
        val pointer = IntHashSetData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(1), packed["readonlyC"])
        assertEquals(listOf(2), packed["mutableC"])
        assertEquals(listOf(3), packed["nullableValueC"])
        assertEquals(listOf(4), packed["mutable"])
        assertEquals(listOf(5), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntHashSetData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(hashSetOf(1), unpacked.readonlyC)
        assertEquals(hashSetOf(2), unpacked.mutableC)
        assertEquals(hashSetOf(3), unpacked.nullableValueC)
        assertEquals(hashSetOf(4), unpacked.mutable)
        assertEquals(hashSetOf(5), unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }*/
}