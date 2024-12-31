package com.ikcode.serialization.processor

import com.ikcode.serialization.processor.examples.collections.IntHashMapData
import com.ikcode.serialization.processor.examples.collections.IntHashMapData_Packer
import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import org.junit.Assert
import org.junit.Test

class SimpleHashMapTests {
    //TODO tests where one of key or value is a primitive and other is a reference

    @Test
    fun intHashMapTests() {
        val data = IntHashMapData(hashMapOf(1 to 10), hashMapOf(2 to 20), null, hashMapOf(3 to 30)).apply {
            mutable = hashMapOf(4 to 40)
            nullableNull = null
            nullableValue = hashMapOf(5 to 50)
        }
        val session = PackingSession()
        val pointer = IntHashMapData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        Assert.assertEquals(mapOf(1 to 10), packed["readonlyC"])
        Assert.assertEquals(mapOf(2 to 20), packed["mutableC"])
        Assert.assertEquals(mapOf(3 to 30), packed["nullableValueC"])
        Assert.assertEquals(mapOf(4 to 40), packed["mutable"])
        Assert.assertEquals(mapOf(5 to 50), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntHashMapData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        Assert.assertEquals(hashMapOf(1 to 10), unpacked.readonlyC)
        Assert.assertEquals(hashMapOf(2 to 20), unpacked.mutableC)
        Assert.assertEquals(hashMapOf(3 to 30), unpacked.nullableValueC)
        Assert.assertEquals(hashMapOf(4 to 40), unpacked.mutable)
        Assert.assertEquals(hashMapOf(5 to 50), unpacked.nullableValue)
        Assert.assertEquals(null, unpacked.nullableNullC)
        Assert.assertEquals(null, unpacked.nullableNull)
    }
}