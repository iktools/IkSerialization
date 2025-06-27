package com.ikcode.serialization.core.annotations

/**
 * Marks property as reference only in serialization.
 *
 * The property value will not be serialized but can be referenced by other
 * serialized objects.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class ReferenceOnly