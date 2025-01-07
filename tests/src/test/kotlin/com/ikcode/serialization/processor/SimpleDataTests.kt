package com.ikcode.serialization.processor

import com.ikcode.serialization.processor.examples.simple.*
import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleDataTests {
    //TODO Byte
    //TODO Char
    //TODO Double
    //TODO Long
    //TODO Short

    @Test
    fun enumTests() {
        val data = EnumData(EnumSample.Value1, EnumSample.Value2, null, EnumSample.Value3).apply {
            mutable = EnumSample.Value4
            nullableNull = null
            nullableValue = EnumSample.Value5
        }
        val session = PackingSession()
        val pointer = EnumData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(EnumSample.Value1.ordinal, packed["readonlyC"])
        assertEquals(EnumSample.Value2.ordinal, packed["mutableC"])
        assertEquals(EnumSample.Value3.ordinal, packed["nullableValueC"])
        assertEquals(EnumSample.Value4.ordinal, packed["mutable"])
        assertEquals(EnumSample.Value5.ordinal, packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = EnumData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(EnumSample.Value1, unpacked.readonlyC)
        assertEquals(EnumSample.Value2, unpacked.mutableC)
        assertEquals(EnumSample.Value3, unpacked.nullableValueC)
        assertEquals(EnumSample.Value4, unpacked.mutable)
        assertEquals(EnumSample.Value5, unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun floatTests() {
        val data = FloatData(1f, 2f, null, 3f).apply {
            mutable = 4f
            nullableNull = null
            nullableValue = 5f
        }
        val session = PackingSession()
        val pointer = FloatData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(1f, packed["readonlyC"])
        assertEquals(2f, packed["mutableC"])
        assertEquals(3f, packed["nullableValueC"])
        assertEquals(4f, packed["mutable"])
        assertEquals(5f, packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = FloatData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(1f, unpacked.readonlyC)
        assertEquals(2f, unpacked.mutableC)
        assertEquals(3f, unpacked.nullableValueC)
        assertEquals(4f, unpacked.mutable)
        assertEquals(5f, unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun intTests() {
        val data = IntData(1, 2, null, 3).apply {
            mutable = 4
            nullableNull = null
            nullableValue = 5
        }
        val session = PackingSession()
        val pointer = IntData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(1, packed["readonlyC"])
        assertEquals(2, packed["mutableC"])
        assertEquals(3, packed["nullableValueC"])
        assertEquals(4, packed["mutable"])
        assertEquals(5, packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(1, unpacked.readonlyC)
        assertEquals(2, unpacked.mutableC)
        assertEquals(3, unpacked.nullableValueC)
        assertEquals(4, unpacked.mutable)
        assertEquals(5, unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    //TODO
    /*@Test
    fun referenceTests() {
        val referencedData1 = ObjectSample()
        val referencedData2 = ObjectSample()
        val data = ObjectData(
            referencedData1, referencedData1, null, referencedData1,
            referencedData2, referencedData2, null, referencedData2
        ).apply {
            mutable1 = referencedData1
            nullableNull1 = null
            nullableValue1 = referencedData1

            mutable2 = referencedData2
            nullableNull2 = null
            nullableValue2 = referencedData2
        }
        val session = PackingSession()
        val pointer = ObjectData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        val reference1 = (packed["readonlyC1"] as ReferencePointer).name
        assert(reference1.startsWith("ObjectSample"))
        assertEquals(reference1, (packed["mutableC1"] as ReferencePointer).name)
        assertEquals(reference1, (packed["nullableValueC1"] as ReferencePointer).name)
        assertEquals(reference1, (packed["mutable1"] as ReferencePointer).name)
        assertEquals(reference1, (packed["nullableValue1"] as ReferencePointer).name)
        assert(!packed.containsKey("nullableNullC1"))
        assert(!packed.containsKey("nullableNull1"))

        val reference2 = (packed["readonlyC2"] as ReferencePointer).name
        assert(reference2.startsWith("ObjectSample"))
        assert(reference1 != reference2)
        assertEquals(reference2, (packed["mutableC2"] as ReferencePointer).name)
        assertEquals(reference2, (packed["nullableValueC2"] as ReferencePointer).name)
        assertEquals(reference2, (packed["mutable2"] as ReferencePointer).name)
        assertEquals(reference2, (packed["nullableValue2"] as ReferencePointer).name)
        assert(!packed.containsKey("nullableNullC2"))
        assert(!packed.containsKey("nullableNull2"))

        val unpacked = ObjectData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        val obj1 = unpacked.readonlyC1
        assertEquals(obj1, unpacked.mutableC1)
        assertEquals(obj1, unpacked.nullableValueC1)
        assertEquals(obj1, unpacked.mutable1)
        assertEquals(obj1, unpacked.nullableValue1)
        assertEquals(null, unpacked.nullableNullC1)
        assertEquals(null, unpacked.nullableNull1)

        val obj2 = unpacked.readonlyC2
        assert(obj1 != obj2)
        assertEquals(obj2, unpacked.mutableC2)
        assertEquals(obj2, unpacked.nullableValueC2)
        assertEquals(obj2, unpacked.mutable2)
        assertEquals(obj2, unpacked.nullableValue2)
        assertEquals(null, unpacked.nullableNullC2)
        assertEquals(null, unpacked.nullableNull2)
    }
*/

    @Test
    fun stringTests() {
        val data = StringData("1", "2", null, "3").apply {
            mutable = "4"
            nullableNull = null
            nullableValue = "5"
        }
        val session = PackingSession()
        val pointer = StringData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals("1", packed["readonlyC"])
        assertEquals("2", packed["mutableC"])
        assertEquals("3", packed["nullableValueC"])
        assertEquals("4", packed["mutable"])
        assertEquals("5", packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = StringData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals("1", unpacked.readonlyC)
        assertEquals("2", unpacked.mutableC)
        assertEquals("3", unpacked.nullableValueC)
        assertEquals("4", unpacked.mutable)
        assertEquals("5", unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }
}