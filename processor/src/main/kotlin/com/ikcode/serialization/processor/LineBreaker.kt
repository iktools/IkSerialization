package com.ikcode.serialization.processor

class LineBreaker(
    val itemCount: Int,
    val threshold: Int = 0
) {
    private var index = 0
    val multiline get() = itemCount > threshold

    fun separate(): Boolean {
        this.index++

        return this.index - 1 < this.itemCount - 1
    }
}