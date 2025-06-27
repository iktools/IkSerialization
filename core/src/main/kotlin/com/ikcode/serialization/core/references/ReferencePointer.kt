package com.ikcode.serialization.core.references

/**
 * Pointer to a named object.
 */
class ReferencePointer(
    /**
     * Name of the object's anchor.
     */
    val name: String
) {
    /**
     * Constructs a pointer out of class name and instance number
     */
    constructor(type: String, index: Int): this(type + index.toString())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReferencePointer

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}