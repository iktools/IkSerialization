package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.inheritance.ImplementationData2
import com.ikcode.serialization.processor.examples.inheritance.ImplementationData2_Packer
import com.ikcode.serialization.processor.examples.inheritance.OpenInterfaceSample_Packer
import com.ikcode.serialization.processor.examples.simple.ObjectSample
import kotlin.test.Test
import kotlin.test.assertEquals

class CrossModuleTests {
    //TODO test unpacking from base type

    @Test
    fun crossModuleImplementationTests() {
        val referencedData1 = ObjectSample(1)
        val referencedData2 = ObjectSample(2)
        val data = ImplementationData2(
            referencedData1, referencedData1, null, referencedData1,
            referencedData2, referencedData2, null, referencedData2
        ).apply {
            mutable1 = referencedData1
            nullableNull1 = null
            nullableValue1 = referencedData1

            mutable2 = referencedData2
            nullableNull2 = null
            nullableValue2 = referencedData2
        }
        val session = PackingSession()
        val pointer = ImplementationData2_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        val reference1 = (packed["readonlyC1"] as ReferencePointer).name
        assert(reference1.startsWith("ObjectSample"))
        assertEquals(reference1, (packed["mutableC1"] as ReferencePointer).name)
        assertEquals(reference1, (packed["nullableValueC1"] as ReferencePointer).name)
        assertEquals(reference1, (packed["mutable1"] as ReferencePointer).name)
        assertEquals(reference1, (packed["nullableValue1"] as ReferencePointer).name)
        assert(!packed.containsKey("nullableNullC1"))
        assert(!packed.containsKey("nullableNull1"))

        val reference2 = (packed["readonlyC2"] as ReferencePointer).name
        assert(reference2.startsWith("ObjectSample"))
        assert(reference1 != reference2)
        assertEquals(reference2, (packed["mutableC2"] as ReferencePointer).name)
        assertEquals(reference2, (packed["nullableValueC2"] as ReferencePointer).name)
        assertEquals(reference2, (packed["mutable2"] as ReferencePointer).name)
        assertEquals(reference2, (packed["nullableValue2"] as ReferencePointer).name)
        assert(!packed.containsKey("nullableNullC2"))
        assert(!packed.containsKey("nullableNull2"))

        val unpacked = OpenInterfaceSample_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        ) as ImplementationData2

        val obj1 = unpacked.readonlyC1
        assertEquals(obj1, unpacked.mutableC1)
        assertEquals(obj1, unpacked.nullableValueC1)
        assertEquals(obj1, unpacked.mutable1)
        assertEquals(obj1, unpacked.nullableValue1)
        assertEquals(null, unpacked.nullableNullC1)
        assertEquals(null, unpacked.nullableNull1)

        val obj2 = unpacked.readonlyC2
        assert(obj1 != obj2)
        assertEquals(obj2, unpacked.mutableC2)
        assertEquals(obj2, unpacked.nullableValueC2)
        assertEquals(obj2, unpacked.mutable2)
        assertEquals(obj2, unpacked.nullableValue2)
        assertEquals(null, unpacked.nullableNullC2)
        assertEquals(null, unpacked.nullableNull2)
    }
}