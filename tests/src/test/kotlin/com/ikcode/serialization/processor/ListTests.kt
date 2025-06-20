package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.collections.IntArrayListData
import com.ikcode.serialization.processor.examples.collections.IntArrayListData_Packer
import com.ikcode.serialization.processor.examples.collections.IntCollectionData
import com.ikcode.serialization.processor.examples.collections.IntCollectionData_Packer
import com.ikcode.serialization.processor.examples.collections.IntIterableData
import com.ikcode.serialization.processor.examples.collections.IntIterableData_Packer
import com.ikcode.serialization.processor.examples.collections.ListNestedIntMapData
import com.ikcode.serialization.processor.examples.collections.IntMutableListData
import com.ikcode.serialization.processor.examples.collections.IntMutableListData_Packer
import com.ikcode.serialization.processor.examples.collections.ListNestedIntMapData_Packer
import com.ikcode.serialization.processor.examples.collections.ListNestedMutableIntMapData
import com.ikcode.serialization.processor.examples.collections.ListNestedMutableIntMapData_Packer
import com.ikcode.serialization.processor.examples.collections.ObjectArrayListData
import com.ikcode.serialization.processor.examples.collections.ObjectArrayListData_Packer
import com.ikcode.serialization.processor.examples.simple.ObjectSample
import kotlin.test.Test
import kotlin.test.assertEquals

class ListTests {

    @Test
    fun intArrayListTests() {
        val data = IntArrayListData(
            arrayListOf(1),
            arrayListOf(2),
            null,
            arrayListOf(3)
        ).apply {
            mutable = arrayListOf(4)
            nullableNull = null
            nullableValue = arrayListOf(5)
            readonly.add(6)
        }
        val session = PackingSession()
        val pointer = IntArrayListData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(1), packed["readonlyC"])
        assertEquals(listOf(2), packed["mutableC"])
        assertEquals(listOf(3), packed["nullableValueC"])
        assertEquals(listOf(4), packed["mutable"])
        assertEquals(listOf(5), packed["nullableValue"])
        assertEquals(listOf(6), packed["readonly"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntArrayListData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(arrayListOf(1), unpacked.readonlyC)
        assertEquals(arrayListOf(2), unpacked.mutableC)
        assertEquals(arrayListOf(3), unpacked.nullableValueC)
        assertEquals(arrayListOf(4), unpacked.mutable)
        assertEquals(arrayListOf(5), unpacked.nullableValue)
        assertEquals(arrayListOf(6), unpacked.readonly)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

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

        assertEquals(listOf(1), packed["readonlyC"])
        assertEquals(listOf(2), packed["mutableC"])
        assertEquals(listOf(3), packed["nullableValueC"])
        assertEquals(listOf(4), packed["mutable"])
        assertEquals(listOf(5), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntCollectionData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(arrayListOf(1), unpacked.readonlyC)
        assertEquals(arrayListOf(2), unpacked.mutableC)
        assertEquals(arrayListOf(3), unpacked.nullableValueC)
        assertEquals(arrayListOf(4), unpacked.mutable)
        assertEquals(arrayListOf(5), unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun intIterableTests() {
        val data = IntIterableData(
            listOf(1),
            listOf(2),
            null,
            listOf(3)
        ).apply {
            mutable = listOf(4)
            nullableNull = null
            nullableValue = listOf(5)
        }
        val session = PackingSession()
        val pointer = IntIterableData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(1), packed["readonlyC"])
        assertEquals(listOf(2), packed["mutableC"])
        assertEquals(listOf(3), packed["nullableValueC"])
        assertEquals(listOf(4), packed["mutable"])
        assertEquals(listOf(5), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntIterableData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(arrayListOf(1), unpacked.readonlyC)
        assertEquals(arrayListOf(2), unpacked.mutableC)
        assertEquals(arrayListOf(3), unpacked.nullableValueC)
        assertEquals(arrayListOf(4), unpacked.mutable)
        assertEquals(arrayListOf(5), unpacked.nullableValue)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun listNestedIntMapTests() {
        val data = ListNestedIntMapData(
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
        val pointer = ListNestedIntMapData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(mapOf(1 to 10)), packed["readonlyC"])
        assertEquals(listOf(mapOf(2 to 20)), packed["mutableC"])
        assertEquals(listOf(mapOf(3 to 30)), packed["nullableValueC"])
        assertEquals(listOf(mapOf(4 to 40)), packed["mutable"])
        assertEquals(listOf(mapOf(5 to 50)), packed["nullableValue"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = ListNestedIntMapData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(listOf(hashMapOf(1 to 10)), unpacked.readonlyC)
        assertEquals(listOf(hashMapOf(2 to 20)), unpacked.mutableC)
        assertEquals(listOf(hashMapOf(3 to 30)), unpacked.nullableValueC)
        assertEquals(listOf(hashMapOf(4 to 40) as Map<Int, Int>), unpacked.mutable)
        assertEquals(listOf(hashMapOf(5 to 50) as Map<Int, Int>), unpacked.nullableValue!!)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun listNestedMutableIntMapTests() {
        val data = ListNestedMutableIntMapData(
            mutableListOf(mutableMapOf(1 to 10)),
            mutableListOf(mutableMapOf(2 to 20)),
            null,
            mutableListOf(mutableMapOf(3 to 30))
        ).apply {
            mutable = mutableListOf(mutableMapOf(4 to 40))
            nullableNull = null
            nullableValue = mutableListOf(mutableMapOf(5 to 50))
            readonly += mutableMapOf(6 to 60)
        }
        val session = PackingSession()
        val pointer = ListNestedMutableIntMapData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(mapOf(1 to 10)), packed["readonlyC"])
        assertEquals(listOf(mapOf(2 to 20)), packed["mutableC"])
        assertEquals(listOf(mapOf(3 to 30)), packed["nullableValueC"])
        assertEquals(listOf(mapOf(4 to 40)), packed["mutable"])
        assertEquals(listOf(mapOf(5 to 50)), packed["nullableValue"])
        assertEquals(listOf(mapOf(6 to 60)), packed["readonly"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = ListNestedMutableIntMapData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(mutableListOf(mutableMapOf(1 to 10)), unpacked.readonlyC)
        assertEquals(mutableListOf(mutableMapOf(2 to 20)), unpacked.mutableC)
        assertEquals(mutableListOf(mutableMapOf(3 to 30)), unpacked.nullableValueC)
        assertEquals(mutableListOf(mutableMapOf(4 to 40)), unpacked.mutable)
        assertEquals(mutableListOf(mutableMapOf(5 to 50)), unpacked.nullableValue!!)
        assertEquals(mutableListOf(mutableMapOf(6 to 60)), unpacked.readonly)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun intMutableListTests() {
        val data = IntMutableListData(
            mutableListOf(1),
            mutableListOf(2),
            null,
            mutableListOf(3)
        ).apply {
            mutable = mutableListOf(4)
            nullableNull = null
            nullableValue = mutableListOf(5)
            readonly += 6
        }
        val session = PackingSession()
        val pointer = IntMutableListData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        assertEquals(listOf(1), packed["readonlyC"])
        assertEquals(listOf(2), packed["mutableC"])
        assertEquals(listOf(3), packed["nullableValueC"])
        assertEquals(listOf(4), packed["mutable"])
        assertEquals(listOf(5), packed["nullableValue"])
        assertEquals(listOf(6), packed["readonly"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = IntMutableListData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(arrayListOf(1), unpacked.readonlyC)
        assertEquals(arrayListOf(2), unpacked.mutableC)
        assertEquals(arrayListOf(3), unpacked.nullableValueC)
        assertEquals(arrayListOf(4), unpacked.mutable)
        assertEquals(arrayListOf(5), unpacked.nullableValue)
        assertEquals(arrayListOf(6), unpacked.readonly)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }

    @Test
    fun objectArrayListTests() {
        val referencedData1 = ObjectSample(1)
        val referencedData2 = ObjectSample(2)
        val referencedData3 = ObjectSample(3)
        val referencedData4 = ObjectSample(4)
        val referencedData5 = ObjectSample(5)
        val referencedData6 = ObjectSample(6)

        val data = ObjectArrayListData(
            arrayListOf(referencedData1),
            arrayListOf(referencedData2),
            null,
            arrayListOf(referencedData3)
        ).apply {
            mutable = arrayListOf(referencedData4)
            nullableNull = null
            nullableValue = arrayListOf(referencedData5)
            readonly += referencedData6
        }
        val session = PackingSession()
        val pointer = ObjectArrayListData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        val reference1 = (packed["readonlyC"] as List<*>)[0] as ReferencePointer
        val reference2 = (packed["mutableC"] as List<*>)[0] as ReferencePointer
        val reference3 = (packed["nullableValueC"] as List<*>)[0] as ReferencePointer
        val reference4 = (packed["mutable"] as List<*>)[0] as ReferencePointer
        val reference5 = (packed["nullableValue"] as List<*>)[0] as ReferencePointer
        val reference6 = (packed["readonly"] as List<*>)[0] as ReferencePointer
        assert(reference1.name.startsWith("ObjectSample"))
        assert(reference2.name.startsWith("ObjectSample"))
        assert(reference3.name.startsWith("ObjectSample"))
        assert(reference4.name.startsWith("ObjectSample"))
        assert(reference5.name.startsWith("ObjectSample"))
        assert(reference6.name.startsWith("ObjectSample"))

        assertEquals(listOf(reference1), packed["readonlyC"])
        assertEquals(listOf(reference2), packed["mutableC"])
        assertEquals(listOf(reference3), packed["nullableValueC"])
        assertEquals(listOf(reference4), packed["mutable"])
        assertEquals(listOf(reference5), packed["nullableValue"])
        assertEquals(listOf(reference6), packed["readonly"])
        assert(!packed.containsKey("nullableNullC"))
        assert(!packed.containsKey("nullableNull"))

        val unpacked = ObjectArrayListData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(arrayListOf(ObjectSample(1)), unpacked.readonlyC)
        assertEquals(arrayListOf(ObjectSample(2)), unpacked.mutableC)
        assertEquals(arrayListOf(ObjectSample(3)), unpacked.nullableValueC)
        assertEquals(arrayListOf(ObjectSample(4)), unpacked.mutable)
        assertEquals(arrayListOf(ObjectSample(5)), unpacked.nullableValue)
        assertEquals(arrayListOf(ObjectSample(6)), unpacked.readonly)
        assertEquals(null, unpacked.nullableNullC)
        assertEquals(null, unpacked.nullableNull)
    }
}