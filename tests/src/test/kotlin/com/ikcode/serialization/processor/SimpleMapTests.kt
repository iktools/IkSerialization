package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.collections.*
import com.ikcode.serialization.processor.examples.simple.ObjectSample
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleMapTests {

    @Test
    fun intMapTests() {
        val data = IntMapData(hashMapOf(1 to 10), hashMapOf(2 to 20), null, hashMapOf(3 to 30)).apply {
            mutable = hashMapOf(4 to 40)
            nullableNull = null
            nullableValue = hashMapOf(5 to 50)
        }
        val session = PackingSession()
        val pointer = IntMapData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(mapOf(1 to 10), packed["readonlyC"])
        assertEquals(mapOf(2 to 20), packed["mutableC"])
        assertEquals(mapOf(3 to 30), packed["nullableValueC"])
        assertEquals(mapOf(4 to 40), packed["mutable"])
        assertEquals(mapOf(5 to 50), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntMapData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(mapOf(1 to 10), unpacked.readonlyC)
        assertEquals(mapOf(2 to 20), unpacked.mutableC)
        assertEquals(mapOf(3 to 30), unpacked.nullableValueC)
        assertEquals(mapOf(4 to 40), unpacked.mutable)
        assertEquals(mapOf(5 to 50), unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun intObjectMapTests() {
        val map1 = mapOf(1 to ObjectSample(10))
        val map2 = mapOf(2 to ObjectSample(20))
        val map3 = mapOf(3 to ObjectSample(30))
        val map4 = mapOf(4 to ObjectSample(40))
        val map5 = mapOf(5 to ObjectSample(50))
        val data = IntObjectMapData(map1, map2, null, map3).apply {
            mutable = map4
            nullableNull = null
            nullableValue = map5
        }
        val session = PackingSession()
        val pointer = IntObjectMapData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        val reference1 = (packed["readonlyC"] as Map<*, *>).values.first() as ReferencePointer
        val reference2 = (packed["mutableC"] as Map<*, *>).values.first() as ReferencePointer
        val reference3 = (packed["nullableValueC"] as Map<*, *>).values.first() as ReferencePointer
        val reference4 = (packed["mutable"] as Map<*, *>).values.first() as ReferencePointer
        val reference5 = (packed["nullableValue"] as Map<*, *>).values.first() as ReferencePointer
        assertEquals(mapOf(1 to reference1), packed["readonlyC"])
        assertEquals(mapOf(2 to reference2), packed["mutableC"])
        assertEquals(mapOf(3 to reference3), packed["nullableValueC"])
        assertEquals(mapOf(4 to reference4), packed["mutable"])
        assertEquals(mapOf(5 to reference5), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntObjectMapData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(map1, unpacked.readonlyC)
        assertEquals(map2, unpacked.mutableC)
        assertEquals(map3, unpacked.nullableValueC)
        assertEquals(map4, unpacked.mutable)
        assertEquals(map5, unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

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

        assertEquals(mapOf(1 to 10), packed["readonlyC"])
        assertEquals(mapOf(2 to 20), packed["mutableC"])
        assertEquals(mapOf(3 to 30), packed["nullableValueC"])
        assertEquals(mapOf(4 to 40), packed["mutable"])
        assertEquals(mapOf(5 to 50), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntHashMapData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(hashMapOf(1 to 10), unpacked.readonlyC)
        assertEquals(hashMapOf(2 to 20), unpacked.mutableC)
        assertEquals(hashMapOf(3 to 30), unpacked.nullableValueC)
        assertEquals(hashMapOf(4 to 40), unpacked.mutable)
        assertEquals(hashMapOf(5 to 50), unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun intMutableMapTests() {
        val data = IntMutableMapData(hashMapOf(1 to 10), hashMapOf(2 to 20), null, hashMapOf(3 to 30)).apply {
            mutable = hashMapOf(4 to 40)
            nullableNull = null
            nullableValue = hashMapOf(5 to 50)
        }
        val session = PackingSession()
        val pointer = IntMutableMapData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(mapOf(1 to 10), packed["readonlyC"])
        assertEquals(mapOf(2 to 20), packed["mutableC"])
        assertEquals(mapOf(3 to 30), packed["nullableValueC"])
        assertEquals(mapOf(4 to 40), packed["mutable"])
        assertEquals(mapOf(5 to 50), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntMutableMapData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(hashMapOf(1 to 10), unpacked.readonlyC)
        assertEquals(hashMapOf(2 to 20), unpacked.mutableC)
        assertEquals(hashMapOf(3 to 30), unpacked.nullableValueC)
        assertEquals(hashMapOf(4 to 40), unpacked.mutable)
        assertEquals(hashMapOf(5 to 50), unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun objectIntMapTests() {
        val map1 = mapOf(ObjectSample(10) to 1)
        val map2 = mapOf(ObjectSample(20) to 2)
        val map3 = mapOf(ObjectSample(30) to 3)
        val map4 = mapOf(ObjectSample(40) to 4)
        val map5 = mapOf(ObjectSample(50) to 5)
        val data = ObjectIntMapData(map1, map2, null, map3).apply {
            mutable = map4
            nullableNull = null
            nullableValue = map5
        }
        val session = PackingSession()
        val pointer = ObjectIntMapData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        val reference1 = (packed["readonlyC"] as Map<*, *>).keys.first() as ReferencePointer
        val reference2 = (packed["mutableC"] as Map<*, *>).keys.first() as ReferencePointer
        val reference3 = (packed["nullableValueC"] as Map<*, *>).keys.first() as ReferencePointer
        val reference4 = (packed["mutable"] as Map<*, *>).keys.first() as ReferencePointer
        val reference5 = (packed["nullableValue"] as Map<*, *>).keys.first() as ReferencePointer
        assertEquals(mapOf(reference1 to 1), packed["readonlyC"])
        assertEquals(mapOf(reference2 to 2), packed["mutableC"])
        assertEquals(mapOf(reference3 to 3), packed["nullableValueC"])
        assertEquals(mapOf(reference4 to 4), packed["mutable"])
        assertEquals(mapOf(reference5 to 5), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = ObjectIntMapData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(map1, unpacked.readonlyC)
        assertEquals(map2, unpacked.mutableC)
        assertEquals(map3, unpacked.nullableValueC)
        assertEquals(map4, unpacked.mutable)
        assertEquals(map5, unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun objectObjectMapTests() {
        val map1 = mapOf(ObjectSample(10) to ObjectSample(1))
        val map2 = mapOf(ObjectSample(20) to ObjectSample(2))
        val map3 = mapOf(ObjectSample(30) to ObjectSample(3))
        val map4 = mapOf(ObjectSample(40) to ObjectSample(4))
        val map5 = mapOf(ObjectSample(50) to ObjectSample(5))
        val data = ObjectObjectMapData(map1, map2, null, map3).apply {
            mutable = map4
            nullableNull = null
            nullableValue = map5
        }
        val session = PackingSession()
        val pointer = ObjectObjectMapData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        val reference1k = (packed["readonlyC"] as Map<*, *>).keys.first() as ReferencePointer
        val reference2k = (packed["mutableC"] as Map<*, *>).keys.first() as ReferencePointer
        val reference3k = (packed["nullableValueC"] as Map<*, *>).keys.first() as ReferencePointer
        val reference4k = (packed["mutable"] as Map<*, *>).keys.first() as ReferencePointer
        val reference5k = (packed["nullableValue"] as Map<*, *>).keys.first() as ReferencePointer
        val reference1v = (packed["readonlyC"] as Map<*, *>).values.first() as ReferencePointer
        val reference2v = (packed["mutableC"] as Map<*, *>).values.first() as ReferencePointer
        val reference3v = (packed["nullableValueC"] as Map<*, *>).values.first() as ReferencePointer
        val reference4v = (packed["mutable"] as Map<*, *>).values.first() as ReferencePointer
        val reference5v = (packed["nullableValue"] as Map<*, *>).values.first() as ReferencePointer
        assertEquals(mapOf(reference1k to reference1v), packed["readonlyC"])
        assertEquals(mapOf(reference2k to reference2v), packed["mutableC"])
        assertEquals(mapOf(reference3k to reference3v), packed["nullableValueC"])
        assertEquals(mapOf(reference4k to reference4v), packed["mutable"])
        assertEquals(mapOf(reference5k to reference5v), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = ObjectObjectMapData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(map1, unpacked.readonlyC)
        assertEquals(map2, unpacked.mutableC)
        assertEquals(map3, unpacked.nullableValueC)
        assertEquals(map4, unpacked.mutable)
        assertEquals(map5, unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }
}