package com.ikcode.serialization.core.session

import com.ikcode.serialization.core.references.ReferenceAnchor
import com.ikcode.serialization.core.references.ReferencePointer

class PackingSession {
    private val references = HashMap<Any, ReferenceAnchor>()
    private val nextReference = HashMap<String, Int>()

    val referencedData
        get() = references.values

    fun referenceFor(obj: Any, key: String, packer: (HashMap<String, Any>) -> Unit) =
        this.references[obj]?.pointer ?: makeReference(obj, key, packer)

    fun referenceFor(obj: Any) = this.references[obj]!!.pointer

    fun register(obj: Any, key: String, factory: String, property: String) {
        val anchor = this.references[obj]
        val factoryData = listOf(factory, property)
        if (anchor != null)
            (anchor.dataMap as MutableMap<Any, Any>)["@factory"] = factoryData
        else
            this.references[obj] = ReferenceAnchor(
                mutableMapOf<Any, Any>("@factory" to factoryData),
                makePointer(key),
            )

    }

    private fun makeReference(obj: Any, key: String, packer: (HashMap<String, Any>) -> Unit) =
        makePointer(key).also { pointer ->
            val dataContainer = HashMap<String, Any>()

            this.references[obj] = ReferenceAnchor(dataContainer, pointer)
            packer(dataContainer)
            if (dataContainer.values.any { it is Float && it.isNaN() || it is Double && it.isNaN() })
                throw Exception("NaN is not allowed")
        }

    private fun makePointer(key: String): ReferencePointer {
        if (!this.nextReference.containsKey(key))
            this.nextReference[key] = 0

        return ReferencePointer(key, this.nextReference[key]!!).also {
            this.nextReference[key] = this.nextReference[key]!! + 1
        }
    }
}