package com.ikcode.serialization.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

class SerializableProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation(SerializableClass::class.java.canonicalName)
            .filterIsInstance<KSClassDeclaration>()

        if (!symbols.iterator().hasNext())
            return emptyList()

        val packers = symbols.map { PackerInfo(it) }

        packers.forEach { packer ->
            val fileName = packer .name + "_Packer"
            val packFunc = FunSpec.builder("pack")
                .addParameter("obj", packer.kpType)
                .addParameter("session", PackingSession::class)
                .returns(Any::class)
                .addStatement("TODO()")

            val unpackFunc = FunSpec.builder("unpack")
                .addParameter("packedData", Any::class)
                .addParameter("session", UnpackingSession::class)
                .returns(packer.kpType)
                .addStatement("TODO()")

            val type = TypeSpec.classBuilder(fileName)
                .addFunction(packFunc.build())
                .addFunction(unpackFunc.build())

            val fileText = FileSpec.builder(packer .namespace, fileName)
                .addType(type.build())

            val file = environment.codeGenerator.createNewFile(
                dependencies = Dependencies(false /*, *resolver.getAllFiles().toList().toTypedArray()*/),
                packageName = packer .namespace,
                fileName = fileName
            )

            file.bufferedWriter().use {
                fileText.build().writeTo(it)
            }

            file.close()
        }

        return symbols.filterNot { it.validate() }.toList()
    }
}