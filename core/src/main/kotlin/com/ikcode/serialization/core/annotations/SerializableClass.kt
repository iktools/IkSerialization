package com.ikcode.serialization.core.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class SerializableClass(
    val isOpen: Boolean = false,
    val service: KClass<*> = SerializableClass::class,
    val prefix: String = "_Packer",
    val extensionName: String = "packer"
)