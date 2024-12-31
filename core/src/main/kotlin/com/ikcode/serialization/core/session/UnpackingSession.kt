package com.ikcode.serialization.core.session

import com.ikcode.serialization.core.references.ReferenceAnchor

class UnpackingSession(dataAnchors: Iterable<ReferenceAnchor>) {
    private val references = dataAnchors.associateBy { it.pointer.name }
    private val instantiatedObjects = HashMap<String, Any>()
    private val objectData = HashMap<Any, Any>()
    private val finishedObjects = HashSet<Any>()

    fun dereference(name: String): Any = this.references[name]!!.value
    fun getData(obj: Any) = this.objectData[obj]!!

    fun getInstance(name: String) = this.instantiatedObjects[name] ?:
        references[name]?.factory?.let {
            val factoryData = this.references[it.name]!!.value as Map<*, *>
            factoryPackers[factoryData["@type"]]!!.produce(it, this)
            this.instantiatedObjects[name]
        }

    fun rememberInstance(obj: Any, name: String) {
        if (instantiatedObjects.containsKey(name))
            return

        this.instantiatedObjects[name] = obj
        this.objectData[obj] = this.references[name]!!.value
    }

    fun fillGuard(obj: Any) =
        finishedObjects.contains(obj).also {
            if (!it) finishedObjects.add(obj)
        }

    companion object {
        private val factoryPackers = java.util.ServiceLoader.load(IFactoryPacker::class.java).associateBy {
            it.typeName()
        }
    }
}