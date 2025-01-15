package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.simple.ObjectSample
import com.ikcode.serialization.processor.examples.tuples.IntPairData
import com.ikcode.serialization.processor.examples.tuples.IntPairData_Packer
import com.ikcode.serialization.processor.examples.tuples.ObjPairData
import com.ikcode.serialization.processor.examples.tuples.ObjPairData_Packer
import kotlin.test.Test
import kotlin.test.assertEquals

class ClassAsListTests {
    @Test
    fun intPairTests() {
        val data = IntPairData(1 to 10, 2 to 20, null, 3 to 30).apply {
            mutable = 4 to 40
            nullableNull = null
            nullableValue = 5 to 50
        }
        val session = PackingSession()
        val pointer = IntPairData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(1, 10), packed["readonlyC"])
        assertEquals(listOf(2, 20), packed["mutableC"])
        assertEquals(listOf(3, 30), packed["nullableValueC"])
        assertEquals(listOf(4, 40), packed["mutable"])
        assertEquals(listOf(5, 50), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntPairData_Packer().unpack(
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

    @Test
    fun objPairTests() {
        val referencedData1 = ObjectSample(1)
        val referencedData2 = ObjectSample(2)
        val referencedData3 = ObjectSample(3)
        val referencedData4 = ObjectSample(4)
        val referencedData5 = ObjectSample(5)
        val data = ObjPairData(
            1 to referencedData1,
            referencedData1 to 10,
            2 to referencedData2,
            referencedData2 to 20,
            null,
            null,
            3 to referencedData3,
            referencedData3 to 30
        ).apply {
            mutable1 = 4 to referencedData4
            mutable2 = referencedData4 to 40
            nullableNull1 = null
            nullableNull2 = null
            nullableValue1 = 5 to referencedData5
            nullableValue2 = referencedData5 to 50
        }
        val session = PackingSession()
        val pointer = ObjPairData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        val reference1a = (packed["readonlyC1"] as List<*>)[1] as ReferencePointer
        val reference1b = (packed["readonlyC2"] as List<*>)[0] as ReferencePointer
        val reference2a = (packed["mutableC1"] as List<*>)[1] as ReferencePointer
        val reference2b = (packed["mutableC2"] as List<*>)[0] as ReferencePointer
        val reference3a = (packed["nullableValueC1"] as List<*>)[1] as ReferencePointer
        val reference3b = (packed["nullableValueC2"] as List<*>)[0] as ReferencePointer
        val reference4a = (packed["mutable1"] as List<*>)[1] as ReferencePointer
        val reference4b = (packed["mutable2"] as List<*>)[0] as ReferencePointer
        val reference5a = (packed["nullableValue1"] as List<*>)[1] as ReferencePointer
        val reference5b = (packed["nullableValue2"] as List<*>)[0] as ReferencePointer
        assert(reference1a.name.startsWith("ObjectSample"))
        assert(reference1b.name.startsWith("ObjectSample"))
        assert(reference2a.name.startsWith("ObjectSample"))
        assert(reference2b.name.startsWith("ObjectSample"))
        assert(reference3a.name.startsWith("ObjectSample"))
        assert(reference3b.name.startsWith("ObjectSample"))
        assert(reference4a.name.startsWith("ObjectSample"))
        assert(reference4b.name.startsWith("ObjectSample"))
        assert(reference5a.name.startsWith("ObjectSample"))
        assert(reference5b.name.startsWith("ObjectSample"))

        assertEquals(listOf(1, reference1a), packed["readonlyC1"])
        assertEquals(listOf(reference1b, 10), packed["readonlyC2"])
        assertEquals(listOf(2, reference2a), packed["mutableC1"])
        assertEquals(listOf(reference2b, 20), packed["mutableC2"])
        assertEquals(listOf(3, reference3a), packed["nullableValueC1"])
        assertEquals(listOf(reference3b, 30), packed["nullableValueC2"])
        assertEquals(listOf(4, reference4a), packed["mutable1"])
        assertEquals(listOf(reference4b, 40), packed["mutable2"])
        assertEquals(listOf(5, reference5a), packed["nullableValue1"])
        assertEquals(listOf(reference5b, 50), packed["nullableValue2"])
        assert(!packed.containsKey("nullableNullC1"))
        assert(!packed.containsKey("nullableNullC2"))
        assert(!packed.containsKey("nullableNull1"))
        assert(!packed.containsKey("nullableNull2"))

        val unpacked = ObjPairData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(1 to referencedData1, unpacked.readonlyC1)
        assertEquals(referencedData1 to 10, unpacked.readonlyC2)
        assertEquals(2 to referencedData2, unpacked.mutableC1)
        assertEquals(referencedData2 to 20, unpacked.mutableC2)
        assertEquals(3 to referencedData3, unpacked.nullableValueC1)
        assertEquals(referencedData3 to 30, unpacked.nullableValueC2)
        assertEquals(4 to referencedData4, unpacked.mutable1)
        assertEquals(referencedData4 to 40, unpacked.mutable2)
        assertEquals(5 to referencedData5, unpacked.nullableValue1)
        assertEquals(referencedData5 to 50, unpacked.nullableValue2)
        assertEquals(null, unpacked.nullableNullC1)
        assertEquals(null, unpacked.nullableNullC2)
        assertEquals(null, unpacked.nullableNull1)
        assertEquals(null, unpacked.nullableNull2)
    }
}