package com.ikcode.serialization.core.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class SerializableClass(
    val isOpen: Boolean = false,
    val prefix: String = "_Packer"
)