package com.ikcode.serialization.core.references

/**
 * Anchor of a named object that pointers can point to.
 */
class ReferenceAnchor(
    /**
     * Serialization data for the object.
     */
    val value: Any,
    /**
     * Name of the object.
     */
    val pointer: ReferencePointer
) {
    /**
     * Serialization data as a map.
     */
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