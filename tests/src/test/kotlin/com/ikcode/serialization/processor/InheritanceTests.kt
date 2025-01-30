package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.inheritance.AbstractData_Packer
import com.ikcode.serialization.processor.examples.inheritance.ConcreteData
import com.ikcode.serialization.processor.examples.inheritance.ConcreteData_Packer
import com.ikcode.serialization.processor.examples.inheritance.ImplementationData
import com.ikcode.serialization.processor.examples.inheritance.ImplementationData_Packer
import com.ikcode.serialization.processor.examples.simple.ObjectSample
import kotlin.test.Test
import kotlin.test.assertEquals

class InheritanceTests {
    //TODO test unpacking from base type

    @Test
    fun interfaceImplementationTests() {
        val referencedData1 = ObjectSample(1)
        val referencedData2 = ObjectSample(2)
        val data = ImplementationData(
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
        val pointer = ImplementationData_Packer().pack(data, session) as ReferencePointer
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

        val unpacked = ImplementationData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

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

    @Test
    fun concreteClassTests() {
        val referencedData1 = ObjectSample(1)
        val referencedData2 = ObjectSample(2)
        val referencedData3 = ObjectSample(3)
        val referencedData4 = ObjectSample(4)
        val data = ConcreteData(
            referencedData1, referencedData1, null, referencedData1,
            referencedData2, referencedData2, null, referencedData2,
            referencedData3, referencedData3, null, referencedData3,
            referencedData4, referencedData4, null, referencedData4
        ).apply {
            baseMutable1 = referencedData1
            baseNullableNull1 = null
            baseNullableValue1 = referencedData1

            baseMutable2 = referencedData2
            baseNullableNull2 = null
            baseNullableValue2 = referencedData2

            derivedMutable1 = referencedData3
            derivedNullableNull1 = null
            derivedNullableValue1 = referencedData3

            derivedMutable2 = referencedData4
            derivedNullableNull2 = null
            derivedNullableValue2 = referencedData4
        }

        val session = PackingSession()
        val pointer = ConcreteData_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer}.dataMap

        val reference1 = (packed["baseReadonlyC1"] as ReferencePointer).name
        assert(reference1.startsWith("ObjectSample"))
        assertEquals(reference1, (packed["baseMutableC1"] as ReferencePointer).name)
        assertEquals(reference1, (packed["baseNullableValueC1"] as ReferencePointer).name)
        assertEquals(reference1, (packed["baseMutable1"] as ReferencePointer).name)
        assertEquals(reference1, (packed["baseNullableValue1"] as ReferencePointer).name)
        assert(!packed.containsKey("baseNullableNullC1"))
        assert(!packed.containsKey("baseNullableNull1"))

        val reference2 = (packed["baseReadonlyC2"] as ReferencePointer).name
        assert(reference2.startsWith("ObjectSample"))
        assertEquals(reference2, (packed["baseMutableC2"] as ReferencePointer).name)
        assertEquals(reference2, (packed["baseNullableValueC2"] as ReferencePointer).name)
        assertEquals(reference2, (packed["baseMutable2"] as ReferencePointer).name)
        assertEquals(reference2, (packed["baseNullableValue2"] as ReferencePointer).name)
        assert(!packed.containsKey("baseNullableNullC2"))
        assert(!packed.containsKey("baseNullableNull2"))

        val reference3 = (packed["derivedReadonlyC1"] as ReferencePointer).name
        assert(reference3.startsWith("ObjectSample"))
        assertEquals(reference3, (packed["derivedMutableC1"] as ReferencePointer).name)
        assertEquals(reference3, (packed["derivedNullableValueC1"] as ReferencePointer).name)
        assertEquals(reference3, (packed["derivedMutable1"] as ReferencePointer).name)
        assertEquals(reference3, (packed["derivedNullableValue1"] as ReferencePointer).name)
        assert(!packed.containsKey("derivedNullableNullC1"))
        assert(!packed.containsKey("derivedNullableNull1"))

        val reference4 = (packed["derivedReadonlyC2"] as ReferencePointer).name
        assert(reference4.startsWith("ObjectSample"))
        assertEquals(reference4, (packed["derivedMutableC2"] as ReferencePointer).name)
        assertEquals(reference4, (packed["derivedNullableValueC2"] as ReferencePointer).name)
        assertEquals(reference4, (packed["derivedMutable2"] as ReferencePointer).name)
        assertEquals(reference4, (packed["derivedNullableValue2"] as ReferencePointer).name)
        assert(!packed.containsKey("derivedNullableNullC2"))
        assert(!packed.containsKey("derivedNullableNull2"))

        val unpacked = ConcreteData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        val obj1 = unpacked.baseReadonlyC1
        assertEquals(obj1, unpacked.baseMutableC1)
        assertEquals(obj1, unpacked.baseNullableValueC1)
        assertEquals(obj1, unpacked.baseMutable1)
        assertEquals(obj1, unpacked.baseNullableValue1)
        assertEquals(null, unpacked.baseNullableNullC1)
        assertEquals(null, unpacked.baseNullableNull1)

        val obj2 = unpacked.baseReadonlyC2
        assert(obj1 != obj2)
        assertEquals(obj2, unpacked.baseMutableC2)
        assertEquals(obj2, unpacked.baseNullableValueC2)
        assertEquals(obj2, unpacked.baseMutable2)
        assertEquals(obj2, unpacked.baseNullableValue2)
        assertEquals(null, unpacked.baseNullableNullC2)
        assertEquals(null, unpacked.baseNullableNull2)

        val obj3 = unpacked.derivedReadonlyC1
        assertEquals(obj3, unpacked.derivedMutableC1)
        assertEquals(obj3, unpacked.derivedNullableValueC1)
        assertEquals(obj3, unpacked.derivedMutable1)
        assertEquals(obj3, unpacked.derivedNullableValue1)
        assertEquals(null, unpacked.derivedNullableNullC1)
        assertEquals(null, unpacked.derivedNullableNull1)

        val obj4 = unpacked.derivedReadonlyC2
        assert(obj3 != obj4)
        assertEquals(obj4, unpacked.derivedMutableC2)
        assertEquals(obj4, unpacked.derivedNullableValueC2)
        assertEquals(obj4, unpacked.derivedMutable2)
        assertEquals(obj4, unpacked.derivedNullableValue2)
        assertEquals(null, unpacked.derivedNullableNullC2)
        assertEquals(null, unpacked.derivedNullableNull2)
    }

    @Test
    fun baseConcretizationTests() {
        val referencedData1 = ObjectSample(1)
        val referencedData2 = ObjectSample(2)
        val referencedData3 = ObjectSample(3)
        val referencedData4 = ObjectSample(4)
        val data = ConcreteData(
            referencedData1, referencedData1, null, referencedData1,
            referencedData2, referencedData2, null, referencedData2,
            referencedData3, referencedData3, null, referencedData3,
            referencedData4, referencedData4, null, referencedData4
        ).apply {
            baseMutable1 = referencedData1
            baseNullableNull1 = null
            baseNullableValue1 = referencedData1

            baseMutable2 = referencedData2
            baseNullableNull2 = null
            baseNullableValue2 = referencedData2

            derivedMutable1 = referencedData3
            derivedNullableNull1 = null
            derivedNullableValue1 = referencedData3

            derivedMutable2 = referencedData4
            derivedNullableNull2 = null
            derivedNullableValue2 = referencedData4
        }

        val session = PackingSession()
        val pointer = ConcreteData_Packer().pack(data, session) as ReferencePointer

        val unpacked = AbstractData_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        val obj1 = unpacked.baseReadonlyC1
        assertEquals(obj1, unpacked.baseMutableC1)
        assertEquals(obj1, unpacked.baseNullableValueC1)
        assertEquals(obj1, unpacked.baseMutable1)
        assertEquals(obj1, unpacked.baseNullableValue1)
        assertEquals(null, unpacked.baseNullableNullC1)
        assertEquals(null, unpacked.baseNullableNull1)

        val obj2 = unpacked.baseReadonlyC2
        assert(obj1 != obj2)
        assertEquals(obj2, unpacked.baseMutableC2)
        assertEquals(obj2, unpacked.baseNullableValueC2)
        assertEquals(obj2, unpacked.baseMutable2)
        assertEquals(obj2, unpacked.baseNullableValue2)
        assertEquals(null, unpacked.baseNullableNullC2)
        assertEquals(null, unpacked.baseNullableNull2)

        unpacked as ConcreteData

        val obj3 = unpacked.derivedReadonlyC1
        assertEquals(obj3, unpacked.derivedMutableC1)
        assertEquals(obj3, unpacked.derivedNullableValueC1)
        assertEquals(obj3, unpacked.derivedMutable1)
        assertEquals(obj3, unpacked.derivedNullableValue1)
        assertEquals(null, unpacked.derivedNullableNullC1)
        assertEquals(null, unpacked.derivedNullableNull1)

        val obj4 = unpacked.derivedReadonlyC2
        assert(obj3 != obj4)
        assertEquals(obj4, unpacked.derivedMutableC2)
        assertEquals(obj4, unpacked.derivedNullableValueC2)
        assertEquals(obj4, unpacked.derivedMutable2)
        assertEquals(obj4, unpacked.derivedNullableValue2)
        assertEquals(null, unpacked.derivedNullableNullC2)
        assertEquals(null, unpacked.derivedNullableNull2)
    }
}