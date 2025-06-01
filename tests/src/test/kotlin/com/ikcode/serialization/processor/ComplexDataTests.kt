package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.simple.FillableObject
import com.ikcode.serialization.processor.examples.simple.ReferencingObject
import com.ikcode.serialization.processor.examples.simple.ReferencingObject_Packer
import com.ikcode.serialization.processor.examples.simple.SelfreferencingObject
import com.ikcode.serialization.processor.examples.simple.SelfreferencingObject_Packer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ComplexDataTests {
    @Test
    fun finalFillableTests() {
        val data = ReferencingObject().apply {
            data.simpleData = 1
            nullableNotNull!!.simpleData = 2
            listData += FillableObject().apply { simpleData = 3 }
            setData += FillableObject().apply { simpleData = 4 }
            mapData[FillableObject().apply { simpleData = 5 }] = FillableObject().apply { simpleData = 6 }
        }
        val session = PackingSession()
        val pointer = ReferencingObject_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer }.dataMap

        val reference1 = packed["data"] as ReferencePointer
        val reference2 = packed["nullableNotNull"] as ReferencePointer
        val reference3 = (packed["listData"] as List<*>)[0] as ReferencePointer
        val reference4 = (packed["setData"] as List<*>)[0] as ReferencePointer
        val reference5 = (packed["mapData"] as Map<*, *>).keys.first() as ReferencePointer
        val reference6 = (packed["mapData"] as Map<*, *>).values.first() as ReferencePointer
        val referencedData1 = session.referencedData.first { it.pointer == reference1 }.dataMap
        val referencedData2 = session.referencedData.first { it.pointer == reference2 }.dataMap
        val referencedData3 = session.referencedData.first { it.pointer == reference3 }.dataMap
        val referencedData4 = session.referencedData.first { it.pointer == reference4 }.dataMap
        val referencedData5 = session.referencedData.first { it.pointer == reference5 }.dataMap
        val referencedData6 = session.referencedData.first { it.pointer == reference6 }.dataMap
        assert(reference1.name.startsWith("FillableObject"))
        assert(reference2.name.startsWith("FillableObject"))
        assert(reference3.name.startsWith("FillableObject"))
        assert(reference4.name.startsWith("FillableObject"))
        assert(reference5.name.startsWith("FillableObject"))
        assert(reference6.name.startsWith("FillableObject"))
        assertFalse(packed.containsKey("nullableNull"))
        assertEquals(1, referencedData1["simpleData"])
        assertEquals(2, referencedData2["simpleData"])
        assertEquals(3, referencedData3["simpleData"])
        assertEquals(4, referencedData4["simpleData"])
        assertEquals(5, referencedData5["simpleData"])
        assertEquals(6, referencedData6["simpleData"])

        val unpacked = ReferencingObject_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(1, unpacked.data.simpleData)
        assertEquals(2, unpacked.nullableNotNull!!.simpleData)
        assertEquals(null, unpacked.nullableNull)
        assertEquals(3, unpacked.listData[0].simpleData)
        assertEquals(4, unpacked.setData.first().simpleData)
        assertEquals(5, unpacked.mapData.keys.first().simpleData)
        assertEquals(6, unpacked.mapData.values.first().simpleData)
    }

    @Test
    fun cyclicFillableTests() {
        val data1 = SelfreferencingObject().apply {
            data = 1
        }
        val data2 = SelfreferencingObject().apply {
            data = 2
        }

        data1.apply {
            listData += data2
            setData += data2
            mapData[data2] = data2
        }
        data2.apply {
            listData += data1
            setData += data1
            mapData[data1] = data1
        }
        data1.data = 10
        data2.data = 20

        val session = PackingSession()
        val pointer1 = SelfreferencingObject_Packer().pack(data1, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer1 }.dataMap

        val reference1 = (packed["listData"] as List<*>)[0] as ReferencePointer
        val reference2 = (packed["setData"] as List<*>)[0] as ReferencePointer
        val reference3 = (packed["mapData"] as Map<*, *>).keys.first() as ReferencePointer
        val reference4 = (packed["mapData"] as Map<*, *>).values.first() as ReferencePointer
        val referencedData1 = session.referencedData.first { it.pointer == reference1 }.dataMap
        val referencedData2 = session.referencedData.first { it.pointer == reference2 }.dataMap
        val referencedData3 = session.referencedData.first { it.pointer == reference3 }.dataMap
        val referencedData4 = session.referencedData.first { it.pointer == reference4 }.dataMap
        assert(reference1.name.startsWith("SelfreferencingObject"))
        assert(reference2.name.startsWith("SelfreferencingObject"))
        assert(reference3.name.startsWith("SelfreferencingObject"))
        assert(reference4.name.startsWith("SelfreferencingObject"))
        assertEquals(10, packed["data"])
        assertEquals(20, referencedData1["data"])
        assertEquals(20, referencedData2["data"])
        assertEquals(20, referencedData3["data"])
        assertEquals(20, referencedData4["data"])

        val unpacked = SelfreferencingObject_Packer().unpack(
            pointer1,
            UnpackingSession(session.referencedData)
        )

        assertEquals(10, unpacked.data)
        assertEquals(20, unpacked.listData[0].data)
        assertEquals(20, unpacked.setData.first().data)
        assertEquals(20, unpacked.mapData.keys.first().data)
        assertEquals(20, unpacked.mapData.values.first().data)
    }
}