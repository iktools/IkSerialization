package com.ikcode.serialization.processor

import com.ikcode.serialization.processor.examples.simple.*
import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleDataTests {
    @Test
    fun booleanTests() {
        val data = BooleanData(true, false, true, false, null, true, false).apply {
            mutable1 = true
            mutable2 = false
            nullableNull = null
            nullableValue1 = true
            nullableValue2 = false
        }
        val session = PackingSession()
        val pointer = BooleanData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(true, packed["readonlyC1"])
        assertEquals(false, packed["readonlyC2"])
        assertEquals(true, packed["mutableC1"])
        assertEquals(false, packed["mutableC2"])
        assertEquals(true, packed["nullableValueC1"])
        assertEquals(false, packed["nullableValueC2"])
        assertEquals(true, packed["mutable1"])
        assertEquals(false, packed["mutable2"])
        assertEquals(true, packed["nullableValue1"])
        assertEquals(false, packed["nullableValue2"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = BooleanData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(true, unpacked.readonlyC1)
        assertEquals(false, unpacked.readonlyC2)
        assertEquals(true, unpacked.mutableC1)
        assertEquals(false, unpacked.mutableC2)
        assertEquals(true, unpacked.nullableValueC1)
        assertEquals(false, unpacked.nullableValueC2)
        assertEquals(true, unpacked.mutable1)
        assertEquals(false, unpacked.mutable2)
        assertEquals(true, unpacked.nullableValue1)
        assertEquals(false, unpacked.nullableValue2)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun byteTests() {
        val data = ByteData(1, 2, null, 3).apply {
            mutable = 4
            nullableNull = null
            nullableValue = 5
        }
        val session = PackingSession()
        val pointer = ByteData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(1.toByte(), packed["readonlyC"])
        assertEquals(2.toByte(), packed["mutableC"])
        assertEquals(3.toByte(), packed["nullableValueC"])
        assertEquals(4.toByte(), packed["mutable"])
        assertEquals(5.toByte(), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = ByteData_Packer().unpack(
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

    @Test
    fun charTests() {
        val data = CharData('A', 'B', null, 'C').apply {
            mutable = 'D'
            nullableNull = null
            nullableValue = 'E'
        }
        val session = PackingSession()
        val pointer = CharData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals('A', packed["readonlyC"])
        assertEquals('B', packed["mutableC"])
        assertEquals('C', packed["nullableValueC"])
        assertEquals('D', packed["mutable"])
        assertEquals('E', packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = CharData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals('A', unpacked.readonlyC)
        assertEquals('B', unpacked.mutableC)
        assertEquals('C', unpacked.nullableValueC)
        assertEquals('D', unpacked.mutable)
        assertEquals('E', unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

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
    fun fillableSingleTests() {
        val data = FillableObject().apply {
            simpleData = 1
        }
        val session = PackingSession()
        val pointer = FillableObject_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(1, packed["simpleData"])

        val unpacked = FillableObject_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(1, unpacked.simpleData)
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
    fun doubleTests() {
        val data = DoubleData(1.0, 2.0, null, 3.0).apply {
            mutable = 4.0
            nullableNull = null
            nullableValue = 5.0
        }
        val session = PackingSession()
        val pointer = DoubleData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(1.0, packed["readonlyC"])
        assertEquals(2.0, packed["mutableC"])
        assertEquals(3.0, packed["nullableValueC"])
        assertEquals(4.0, packed["mutable"])
        assertEquals(5.0, packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = DoubleData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(1.0, unpacked.readonlyC)
        assertEquals(2.0, unpacked.mutableC)
        assertEquals(3.0, unpacked.nullableValueC)
        assertEquals(4.0, unpacked.mutable)
        assertEquals(5.0, unpacked.nullableValue)
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

    @Test
    fun longTests() {
        val data = LongData(1, 2, null, 3).apply {
            mutable = 4
            nullableNull = null
            nullableValue = 5
        }
        val session = PackingSession()
        val pointer = LongData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(1L, packed["readonlyC"])
        assertEquals(2L, packed["mutableC"])
        assertEquals(3L, packed["nullableValueC"])
        assertEquals(4L, packed["mutable"])
        assertEquals(5L, packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = LongData_Packer().unpack(
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

    @Test
    fun referenceTests() {
        val referencedData1 = ObjectSample(1)
        val referencedData2 = ObjectSample(2)
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

    @Test
    fun shortTests() {
        val data = ShortData(1, 2, null, 3).apply {
            mutable = 4
            nullableNull = null
            nullableValue = 5
        }
        val session = PackingSession()
        val pointer = ShortData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(1.toShort(), packed["readonlyC"])
        assertEquals(2.toShort(), packed["mutableC"])
        assertEquals(3.toShort(), packed["nullableValueC"])
        assertEquals(4.toShort(), packed["mutable"])
        assertEquals(5.toShort(), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = ShortData_Packer().unpack(
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