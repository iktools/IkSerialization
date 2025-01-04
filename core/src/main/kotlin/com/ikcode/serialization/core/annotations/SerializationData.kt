package com.ikcode.serialization.core.annotations

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class SerializationData(val referenceOnly: Boolean = false)