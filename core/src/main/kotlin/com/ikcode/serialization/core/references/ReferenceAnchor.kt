package com.ikcode.serialization.core.references

class ReferenceAnchor(val value: Any, val pointer: ReferencePointer, val factory: FactoryPointer? = null) {
    val dataMap get() = this.value as HashMap<*, *>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReferenceAnchor

        return pointer == other.pointer
    }

    override fun hashCode(): Int {
        return pointer.hashCode()
    }
}