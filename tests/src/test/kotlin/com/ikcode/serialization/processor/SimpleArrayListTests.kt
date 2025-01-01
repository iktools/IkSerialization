package com.ikcode.serialization.processor

import com.ikcode.serialization.processor.examples.collections.IntArrayListData_Packer
import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.collections.IntArrayListData
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleArrayListTests {

    @Test
    fun intArrayListTests() {
        val data = IntArrayListData(
            arrayListOf(1),
            arrayListOf(2),
            null,
            arrayListOf(3)
        )
            .apply {
            mutable = arrayListOf(4)
            nullableNull = null
            nullableValue = arrayListOf(5)
        }
        val session = PackingSession()
        val pointer = IntArrayListData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(1), packed["readonlyC"])
        assertEquals(listOf(2), packed["mutableC"])
        assertEquals(listOf(3), packed["nullableValueC"])
        assertEquals(listOf(4), packed["mutable"])
        assertEquals(listOf(5), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntArrayListData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(arrayListOf(1), unpacked.readonlyC)
        assertEquals(arrayListOf(2), unpacked.mutableC)
        assertEquals(arrayListOf(3), unpacked.nullableValueC)
        assertEquals(arrayListOf(4), unpacked.mutable)
        assertEquals(arrayListOf(5), unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }
}