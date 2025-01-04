package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import kotlin.test.Test
import kotlin.test.assertEquals

class NestedMapTests {

    //TODO
    /*@Test
    fun intMapNestedTests() {
        val data = IntMapNestedValueData(
            listOf(mapOf(1 to 10)),
            listOf(mapOf(2 to 20)),
            null,
            listOf(mapOf(3 to 30))
        ).apply {
            mutable = mutableListOf(mapOf(4 to 40))
            nullableNull = null
            nullableValue = mutableListOf(mapOf(5 to 50))
        }
        val session = PackingSession()
        val pointer = IntMapNestedValueData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(mapOf(1 to 10)), packed["readonlyC"])
        assertEquals(listOf(mapOf(2 to 20)), packed["mutableC"])
        assertEquals(listOf(mapOf(3 to 30)), packed["nullableValueC"])
        assertEquals(listOf(mapOf(4 to 40)), packed["mutable"])
        assertEquals(listOf(mapOf(5 to 50)), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntMapNestedValueData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(listOf(hashMapOf(1 to 10)), unpacked.readonlyC)
        assertEquals(listOf(hashMapOf(2 to 20)), unpacked.mutableC)
        assertEquals(listOf(hashMapOf(3 to 30)), unpacked.nullableValueC)
        assertEquals(listOf(hashMapOf(4 to 40) as Map<Int, Int>), unpacked.mutable)
        assertEquals(listOf(hashMapOf(5 to 50) as Map<Int, Int>), unpacked.nullableValue!!)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }*/
}