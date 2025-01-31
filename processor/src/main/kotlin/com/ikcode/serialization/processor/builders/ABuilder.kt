package com.ikcode.serialization.processor.builders

import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.PackerInfo
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

abstract class ABuilder(
    protected val classInfo: PackerInfo
) {
    abstract fun pack(funBuilder: FunSpec.Builder)
    abstract fun unpack(funBuilder: FunSpec.Builder)
    abstract fun instantiate(funBuilder: FunSpec.Builder)
    abstract fun fill(funBuilder: FunSpec.Builder)
    open fun extras(typeBuilder: TypeSpec.Builder) {}

    protected open val typeBuilder get() = TypeSpec.classBuilder(this.classInfo.outFileName)

    fun file(): FileSpec {
        val packFunc = FunSpec.builder("pack")
            .addParameter("obj", this.classInfo.kpType)
            .addParameter("session", PackingSession::class)
            .returns(Any::class)
        this.pack(packFunc)

        val unpackFunc = FunSpec.builder("unpack")
            .addParameter("packedData", Any::class)
            .addParameter("session", UnpackingSession::class)
            .returns(this.classInfo.kpType)
        this.unpack(unpackFunc)

        val instantiateFunc = FunSpec.builder("instantiate")
            .addParameter("packedData", Any::class)
            .addParameter("session", UnpackingSession::class)
            .returns(this.classInfo.kpType)
        this.instantiate(instantiateFunc)

        val fillDataFunc = FunSpec.builder("fillData")
            .addParameter("obj", this.classInfo.kpType)
            .addParameter("session", UnpackingSession::class)
        this.fill(fillDataFunc)

        val type = typeBuilder
            .addFunction(packFunc.build())
            .addFunction(unpackFunc.build())
            .addFunction(instantiateFunc.build())
            .addFunction(fillDataFunc.build())
        this.extras(type)

        return FileSpec.builder(this.classInfo.namespace, this.classInfo.outFileName)
            .addType(type.build())
            .build()
    }
}