package com.ikcode.serialization.processor

import com.ikcode.serialization.processor.examples.collections.IntCollectionData
import com.ikcode.serialization.processor.examples.collections.IntCollectionData_Packer
import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import org.junit.Assert
import org.junit.Test

class SimpleCollectionTests {

    @Test
    fun intCollectionTests() {
        val data = IntCollectionData(listOf(1), listOf(2), null, listOf(3)).apply {
            mutable = listOf(4)
            nullableNull = null
            nullableValue = listOf(5)
        }
        val session = PackingSession()
        val pointer = IntCollectionData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        Assert.assertEquals(listOf(1), packed["readonlyC"])
        Assert.assertEquals(listOf(2), packed["mutableC"])
        Assert.assertEquals(listOf(3), packed["nullableValueC"])
        Assert.assertEquals(listOf(4), packed["mutable"])
        Assert.assertEquals(listOf(5), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntCollectionData_Packer().unpack(
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