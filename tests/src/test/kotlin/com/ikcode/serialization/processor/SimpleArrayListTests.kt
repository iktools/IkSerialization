package com.ikcode.serialization.processor

import com.ikcode.serialization.processor.examples.collections.IntArrayListData_Packer
import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import org.junit.Assert
import org.junit.Test

class SimpleArrayListTests {

    @Test
    fun intArrayListTests() {
        val data = com.ikcode.serialization.processor.examples.collections.IntArrayListData(
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

        Assert.assertEquals(listOf(1), packed["readonlyC"])
        Assert.assertEquals(listOf(2), packed["mutableC"])
        Assert.assertEquals(listOf(3), packed["nullableValueC"])
        Assert.assertEquals(listOf(4), packed["mutable"])
        Assert.assertEquals(listOf(5), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntArrayListData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        Assert.assertEquals(arrayListOf(1), unpacked.readonlyC)
        Assert.assertEquals(arrayListOf(2), unpacked.mutableC)
        Assert.assertEquals(arrayListOf(3), unpacked.nullableValueC)
        Assert.assertEquals(arrayListOf(4), unpacked.mutable)
        Assert.assertEquals(arrayListOf(5), unpacked.nullableValue)
        Assert.assertEquals(null, unpacked.nullableNullC)
        Assert.assertEquals(null, unpacked.nullableNull)
    }
}