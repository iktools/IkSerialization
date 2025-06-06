package com.ikcode.serialization.core.session

import com.ikcode.serialization.core.references.ReferenceAnchor
import com.ikcode.serialization.core.references.ReferencePointer

class UnpackingSession(dataAnchors: Iterable<ReferenceAnchor>) {
    private val references = dataAnchors.associateBy { it.pointer.name }
    private val instantiatedObjects = HashMap<String, Any>()
    private val objectData = HashMap<ByReference, Any>()
    private val finishedObjects = HashSet<ByReference>()

    fun dereference(name: String): Any = this.references[name]!!.value
    fun getData(obj: Any) = this.objectData[ByReference(obj)]!!

    fun getInstance(name: String) = this.instantiatedObjects[name]

    fun rememberInstance(obj: Any, name: String) {
        if (instantiatedObjects.containsKey(name))
            return

        this.instantiatedObjects[name] = obj
        this.objectData[ByReference(obj)] = this.references[name]!!.value
    }

    fun rememberData(obj: Any, data: Any) {
        if (data is ReferencePointer)
            this.objectData[ByReference(obj)] = this.references[data.name]!!.value
        else
            this.objectData[ByReference(obj)] = data
    }

    fun rememberProduced(obj: Any, factory: String, property: String) {
        val reference = this.references.entries.first {
            it.value.factory?.reference == factory && it.value.factory?.property == property
        }.value.pointer

        instantiatedObjects[reference.name] = obj
        finishedObjects += ByReference(obj)
    }

    fun fillGuard(obj: Any) =
        finishedObjects.contains(ByReference(obj)).also {
            if (!it) finishedObjects.add(ByReference(obj))
        }
}