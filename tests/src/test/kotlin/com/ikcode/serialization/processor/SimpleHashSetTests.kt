package com.ikcode.serialization.processor

import com.ikcode.serialization.processor.examples.collections.IntHashSetData_Packer
import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import org.junit.Assert
import org.junit.Test

class SimpleHashSetTests {

    @Test
    fun intHashSetTests() {
        val data = com.ikcode.serialization.processor.examples.collections.IntHashSetData(
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

        Assert.assertEquals(listOf(1), packed["readonlyC"])
        Assert.assertEquals(listOf(2), packed["mutableC"])
        Assert.assertEquals(listOf(3), packed["nullableValueC"])
        Assert.assertEquals(listOf(4), packed["mutable"])
        Assert.assertEquals(listOf(5), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntHashSetData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        Assert.assertEquals(hashSetOf(1), unpacked.readonlyC)
        Assert.assertEquals(hashSetOf(2), unpacked.mutableC)
        Assert.assertEquals(hashSetOf(3), unpacked.nullableValueC)
        Assert.assertEquals(hashSetOf(4), unpacked.mutable)
        Assert.assertEquals(hashSetOf(5), unpacked.nullableValue)
        Assert.assertEquals(null, unpacked.nullableNullC)
        Assert.assertEquals(null, unpacked.nullableNull)
    }
}