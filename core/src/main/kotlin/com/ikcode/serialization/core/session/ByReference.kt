package com.ikcode.serialization.core.session

class ByReference(val data: Any) {
    override fun equals(other: Any?): Boolean = other != null
            && other is ByReference
            && other.data === this.data

    override fun hashCode(): Int {
        return this.data.hashCode()
    }
}