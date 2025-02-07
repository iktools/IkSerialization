package com.ikcode.serialization.core.session

import com.ikcode.serialization.core.references.ReferenceAnchor

class UnpackingSession(dataAnchors: Iterable<ReferenceAnchor>) {
    private val references = dataAnchors.associateBy { it.pointer.name }
    private val instantiatedObjects = HashMap<String, Any>()
    private val objectData = HashMap<Any, Any>()

    fun dereference(name: String): Any = this.references[name]!!.value
    fun getData(obj: Any) = this.objectData[obj]!!

    fun getInstance(name: String) = this.instantiatedObjects[name]

    fun rememberInstance(obj: Any, name: String) {
        if (instantiatedObjects.containsKey(name))
            return

        this.instantiatedObjects[name] = obj
        this.objectData[obj] = this.references[name]!!.value
    }
}