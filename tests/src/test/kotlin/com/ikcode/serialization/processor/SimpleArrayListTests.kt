package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.simple.ObjectSample
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleArrayListTests {

    /*@Test
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

    @Test
    fun objectArrayListTests() {
        val referencedData1 = ObjectSample()
        val referencedData2 = ObjectSample()
        val referencedData3 = ObjectSample()
        val referencedData4 = ObjectSample()
        val referencedData5 = ObjectSample()

        val data = ObjectArrayListData(
            arrayListOf(referencedData1),
            arrayListOf(referencedData2),
            null,
            arrayListOf(referencedData3)
        )
            .apply {
                mutable = arrayListOf(referencedData4)
                nullableNull = null
                nullableValue = arrayListOf(referencedData5)
            }
        val session = PackingSession()
        val pointer = ObjectArrayListData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        val reference1 = (packed["readonlyC"] as List<*>)[0] as ReferencePointer
        val reference2 = (packed["mutableC"] as List<*>)[0] as ReferencePointer
        val reference3 = (packed["nullableValueC"] as List<*>)[0] as ReferencePointer
        val reference4 = (packed["mutable"] as List<*>)[0] as ReferencePointer
        val reference5 = (packed["nullableValue"] as List<*>)[0] as ReferencePointer
        assert(reference1.name.startsWith("ObjectSample"))
        assert(reference2.name.startsWith("ObjectSample"))
        assert(reference3.name.startsWith("ObjectSample"))
        assert(reference4.name.startsWith("ObjectSample"))
        assert(reference5.name.startsWith("ObjectSample"))

        assertEquals(listOf(reference1), packed["readonlyC"])
        assertEquals(listOf(reference2), packed["mutableC"])
        assertEquals(listOf(reference3), packed["nullableValueC"])
        assertEquals(listOf(reference4), packed["mutable"])
        assertEquals(listOf(reference5), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = ObjectArrayListData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(arrayListOf(ObjectSample()), unpacked.readonlyC)
        assertEquals(arrayListOf(ObjectSample()), unpacked.mutableC)
        assertEquals(arrayListOf(ObjectSample()), unpacked.nullableValueC)
        assertEquals(arrayListOf(ObjectSample()), unpacked.mutable)
        assertEquals(arrayListOf(ObjectSample()), unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }*/
}