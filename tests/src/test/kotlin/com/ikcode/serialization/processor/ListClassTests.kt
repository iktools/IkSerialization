package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.simple.PairData
import com.ikcode.serialization.processor.examples.simple.PairData_Packer
import kotlin.test.Test
import kotlin.test.assertEquals

class ListClassTests {
    @Test
    fun pairTests() {
        val data = PairData(1 to 10, 2 to 20, null, 3 to 30).apply {
            mutable = 4 to 40
            nullableNull = null
            nullableValue = 5 to 50
        }
        val session = PackingSession()
        val pointer = PairData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(1, 10), packed["readonlyC"])
        assertEquals(listOf(2, 20), packed["mutableC"])
        assertEquals(listOf(3, 30), packed["nullableValueC"])
        assertEquals(listOf(4, 40), packed["mutable"])
        assertEquals(listOf(5, 50), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = PairData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(1 to 10, unpacked.readonlyC)
        assertEquals(2 to 20, unpacked.mutableC)
        assertEquals(3 to 30, unpacked.nullableValueC)
        assertEquals(4 to 40, unpacked.mutable)
        assertEquals(5 to 50, unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }
}