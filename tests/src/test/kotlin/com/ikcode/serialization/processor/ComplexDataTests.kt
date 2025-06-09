package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.referencing.DeepReferencingObject
import com.ikcode.serialization.processor.examples.referencing.DeepReferencingObject_Packer
import com.ikcode.serialization.processor.examples.simple.FillableObject
import com.ikcode.serialization.processor.examples.referencing.ReferenceOnlyData
import com.ikcode.serialization.processor.examples.referencing.ReferenceOnlyData_Packer
import com.ikcode.serialization.processor.examples.referencing.ReferencingObject
import com.ikcode.serialization.processor.examples.referencing.ReferencingObject_Packer
import com.ikcode.serialization.processor.examples.referencing.SelfreferencingObject
import com.ikcode.serialization.processor.examples.referencing.SelfreferencingObject_Packer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ComplexDataTests {

    //TODO test proxy and crossmodule deep final fillable

    @Test
    fun deepFinalFillableTests() {
        val data = DeepReferencingObject().apply {
            data.apply {
                data.simpleData = 1
                nullableNotNull!!.simpleData = 2
                listData += FillableObject().apply { simpleData = 3 }
                setData += FillableObject().apply { simpleData = 4 }
                mapData[FillableObject().apply { simpleData = 5 }] = FillableObject().apply { simpleData = 6 }
            }
            abstractData.apply {
                data.simpleData = 7
                nullableNotNull!!.simpleData = 8
                listData += FillableObject().apply { simpleData = 9 }
                setData += FillableObject().apply { simpleData = 10 }
                mapData[FillableObject().apply { simpleData = 11 }] = FillableObject().apply { simpleData = 12 }
            }
            interfaceData.apply {
                data.simpleData = 13
                nullableNotNull!!.simpleData = 14
                listData += FillableObject().apply { simpleData = 15 }
                setData += FillableObject().apply { simpleData = 16 }
                mapData[FillableObject().apply { simpleData = 17 }] = FillableObject().apply { simpleData = 18 }
            }
            nullableNotNull!!.apply {
                data.simpleData = 19
                nullableNotNull!!.simpleData = 20
                listData += FillableObject().apply { simpleData = 21 }
                setData += FillableObject().apply { simpleData = 22 }
                mapData[FillableObject().apply { simpleData = 23 }] = FillableObject().apply { simpleData = 24 }
            }
        }
        val session = PackingSession()
        val pointer = DeepReferencingObject_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer }.dataMap

        val reference1 = packed["data"] as ReferencePointer
        val reference2 = packed["abstractData"] as ReferencePointer
        val reference3 = packed["interfaceData"] as ReferencePointer
        val reference4 = packed["nullableNotNull"] as ReferencePointer
        assert(reference1.name.startsWith("ReferencingObject"))
        assert(reference2.name.startsWith("ReferencingObject"))
        assert(reference3.name.startsWith("ReferencingObject"))
        assert(reference4.name.startsWith("ReferencingObject"))
        assertEquals(4, setOf(reference1, reference2, reference3, reference4).size)
        assertFalse(packed.containsKey("nullableNull"))

        val unpacked = DeepReferencingObject_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(1, unpacked.data.data.simpleData)
        assertEquals(2, unpacked.data.nullableNotNull!!.simpleData)
        assertEquals(null, unpacked.data.nullableNull)
        assertEquals(3, unpacked.data.listData[0].simpleData)
        assertEquals(4, unpacked.data.setData.first().simpleData)
        assertEquals(5, unpacked.data.mapData.keys.first().simpleData)
        assertEquals(6, unpacked.data.mapData.values.first().simpleData)

        assertEquals(7, unpacked.abstractData.data.simpleData)
        assertEquals(8, unpacked.abstractData.nullableNotNull!!.simpleData)
        assertEquals(null, unpacked.abstractData.nullableNull)
        assertEquals(9, unpacked.abstractData.listData[0].simpleData)
        assertEquals(10, unpacked.abstractData.setData.first().simpleData)
        assertEquals(11, unpacked.abstractData.mapData.keys.first().simpleData)
        assertEquals(12, unpacked.abstractData.mapData.values.first().simpleData)

        assertEquals(13, unpacked.interfaceData.data.simpleData)
        assertEquals(14, unpacked.interfaceData.nullableNotNull!!.simpleData)
        assertEquals(null, unpacked.interfaceData.nullableNull)
        assertEquals(15, unpacked.interfaceData.listData[0].simpleData)
        assertEquals(16, unpacked.interfaceData.setData.first().simpleData)
        assertEquals(17, unpacked.interfaceData.mapData.keys.first().simpleData)
        assertEquals(18, unpacked.interfaceData.mapData.values.first().simpleData)

        assertEquals(19, unpacked.nullableNotNull!!.data.simpleData)
        assertEquals(20, unpacked.nullableNotNull!!.nullableNotNull!!.simpleData)
        assertEquals(null, unpacked.nullableNotNull!!.nullableNull)
        assertEquals(21, unpacked.nullableNotNull!!.listData[0].simpleData)
        assertEquals(22, unpacked.nullableNotNull!!.setData.first().simpleData)
        assertEquals(23, unpacked.nullableNotNull!!.mapData.keys.first().simpleData)
        assertEquals(24, unpacked.nullableNotNull!!.mapData.values.first().simpleData)
    }

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
            id = 1
        }
        val data2 = SelfreferencingObject().apply {
            id = 2
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
        data1.id = 10
        data2.id = 20

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
        assertEquals(10, packed["id"])
        assertEquals(20, referencedData1["id"])
        assertEquals(20, referencedData2["id"])
        assertEquals(20, referencedData3["id"])
        assertEquals(20, referencedData4["id"])

        val unpacked = SelfreferencingObject_Packer().unpack(
            pointer1,
            UnpackingSession(session.referencedData)
        )

        assertEquals(10, unpacked.id)
        assertEquals(20, unpacked.listData[0].id)
        assertEquals(20, unpacked.setData.first().id)
        assertEquals(20, unpacked.mapData.keys.first().id)
        assertEquals(20, unpacked.mapData.values.first().id)
    }

    @Test
    fun referenceOnlyTests() {
        val data = ReferenceOnlyData().apply {
            producedData.simpleData = 1
            stateData.listData += producedData
        }

        val session = PackingSession()
        val pointer = ReferenceOnlyData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer }.dataMap

        assert(!packed.containsKey("data"))

        val unpacked = ReferenceOnlyData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        ).apply {
            producedData.simpleData = 1
        }

        assertEquals(1, unpacked.producedData.simpleData)
    }
}