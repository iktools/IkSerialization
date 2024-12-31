package com.ikcode.serialization.processor

import com.ikcode.serialization.processor.examples.collections.IntMapNestedValueData
import com.ikcode.serialization.processor.examples.collections.IntMapNestedValueData_Packer
import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import org.junit.Assert
import org.junit.Test

class NestedMapTests {

    @Test
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

        Assert.assertEquals(listOf(mapOf(1 to 10)), packed["readonlyC"])
        Assert.assertEquals(listOf(mapOf(2 to 20)), packed["mutableC"])
        Assert.assertEquals(listOf(mapOf(3 to 30)), packed["nullableValueC"])
        Assert.assertEquals(listOf(mapOf(4 to 40)), packed["mutable"])
        Assert.assertEquals(listOf(mapOf(5 to 50)), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntMapNestedValueData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        Assert.assertEquals(listOf(hashMapOf(1 to 10)), unpacked.readonlyC)
        Assert.assertEquals(listOf(hashMapOf(2 to 20)), unpacked.mutableC)
        Assert.assertEquals(listOf(hashMapOf(3 to 30)), unpacked.nullableValueC)
        Assert.assertEquals(listOf(hashMapOf(4 to 40)), unpacked.mutable)
        Assert.assertEquals(listOf(hashMapOf(5 to 50)), unpacked.nullableValue)
        Assert.assertEquals(null, unpacked.nullableNullC)
        Assert.assertEquals(null, unpacked.nullableNull)
    }
}