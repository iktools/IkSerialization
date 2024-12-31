package com.ikcode.serialization.core.references

class ReferencePointer(val name: String) {
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