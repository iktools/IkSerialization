package com.ikcode.serialization.core.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class SerializationData(val referenceOnly: Boolean = false)