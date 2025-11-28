package com.ikcode.serialization.core.annotations

/**
 * Marks type as serializable
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class SerializableClass(
    /**
     * Indicates whether a type can be extended in other modules.
     */
    val crossModuleOpen: Boolean = false,
    val prefix: String = "_Packer"
)