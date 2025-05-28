package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.collections.IntHashSetData
import com.ikcode.serialization.processor.examples.collections.IntHashSetData_Packer
import com.ikcode.serialization.processor.examples.collections.IntMutableSetData
import com.ikcode.serialization.processor.examples.collections.IntMutableSetData_Packer
import com.ikcode.serialization.processor.examples.collections.IntSetData
import com.ikcode.serialization.processor.examples.collections.IntSetData_Packer
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleSetTests {

    @Test
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
            readonly += 6
        }
        val session = PackingSession()
        val pointer = IntHashSetData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(1), packed["readonlyC"])
        assertEquals(listOf(2), packed["mutableC"])
        assertEquals(listOf(3), packed["nullableValueC"])
        assertEquals(listOf(4), packed["mutable"])
        assertEquals(listOf(5), packed["nullableValue"])
        assertEquals(listOf(6), packed["readonly"])
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
        assertEquals(hashSetOf(6), unpacked.readonly)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun intMutableSetTests() {
        val data = IntMutableSetData(
            mutableSetOf(1),
            mutableSetOf(2),
            null,
            mutableSetOf(3)
        ).apply {
            mutable = mutableSetOf(4)
            nullableNull = null
            nullableValue = mutableSetOf(5)
            readonly += 6
        }
        val session = PackingSession()
        val pointer = IntMutableSetData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(1), packed["readonlyC"])
        assertEquals(listOf(2), packed["mutableC"])
        assertEquals(listOf(3), packed["nullableValueC"])
        assertEquals(listOf(4), packed["mutable"])
        assertEquals(listOf(5), packed["nullableValue"])
        assertEquals(listOf(6), packed["readonly"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntMutableSetData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(mutableSetOf(1), unpacked.readonlyC)
        assertEquals(mutableSetOf(2), unpacked.mutableC)
        assertEquals(mutableSetOf(3), unpacked.nullableValueC)
        assertEquals(mutableSetOf(4), unpacked.mutable)
        assertEquals(mutableSetOf(5), unpacked.nullableValue)
        assertEquals(mutableSetOf(6), unpacked.readonly)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun intSetTests() {
        val data = IntSetData(
            setOf(1),
            setOf(2),
            null,
            setOf(3)
        ).apply {
            mutable = hashSetOf(4)
            nullableNull = null
            nullableValue = hashSetOf(5)
        }
        val session = PackingSession()
        val pointer = IntSetData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(1), packed["readonlyC"])
        assertEquals(listOf(2), packed["mutableC"])
        assertEquals(listOf(3), packed["nullableValueC"])
        assertEquals(listOf(4), packed["mutable"])
        assertEquals(listOf(5), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntSetData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(setOf(1), unpacked.readonlyC)
        assertEquals(setOf(2), unpacked.mutableC)
        assertEquals(setOf(3), unpacked.nullableValueC)
        assertEquals(setOf(4), unpacked.mutable)
        assertEquals(setOf(5), unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }
}