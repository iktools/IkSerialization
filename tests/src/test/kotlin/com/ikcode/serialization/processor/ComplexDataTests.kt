package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.examples.simple.ReferencingObject
import com.ikcode.serialization.processor.examples.simple.ReferencingObject_Packer
import kotlin.test.Test
import kotlin.test.assertEquals

class ComplexDataTests {
    @Test
    fun finalFillableTests() {
        val data = ReferencingObject().apply {
            data.simpleData = 1
        }
        val session = PackingSession()
        val pointer = ReferencingObject_Packer().pack(data, session) as ReferencePointer
        val packed = session.referencedData.first { it.pointer == pointer }.dataMap

        val reference1 = (packed["data"] as ReferencePointer)
        val referencedPackedData = session.referencedData.first { it.pointer == reference1}.dataMap
        assert(reference1.name.startsWith("FillableObject"))
        assertEquals(1, referencedPackedData["simpleData"])

        val unpacked = ReferencingObject_Packer().unpack(
            pointer,
            UnpackingSession(session.referencedData)
        )

        assertEquals(1, unpacked.data.simpleData)
    }
}