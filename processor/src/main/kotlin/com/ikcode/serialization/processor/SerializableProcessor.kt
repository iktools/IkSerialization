package com.ikcode.serialization.processor

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.core.session.IProxyPacked
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import kotlin.reflect.typeOf

class SerializableProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation(SerializableClass::class.java.canonicalName)
            .filterIsInstance<KSClassDeclaration>()

        if (!symbols.iterator().hasNext())
            return emptyList()

        val proxyType = resolver
            .getClassDeclarationByName(IProxyPacked::class.qualifiedName!!)!!
            .asStarProjectedType()
        val packers = symbols.map {
            environment.logger.warn("${it.qualifiedName?.asString()} is ${it.classKind}")
            PackerInfo(it, proxyType)
        }

        packers.forEach { packer ->
            val fileText = when {
                packer.isEnum -> EnumBuilder(packer)
                else -> TodoBuilder(packer)
            }.file()

            val file = environment.codeGenerator.createNewFile(
                dependencies = Dependencies(false /*, *resolver.getAllFiles().toList().toTypedArray()*/),
                packageName = packer .namespace,
                fileName = packer.outFileName
            )

            file.bufferedWriter().use {
                fileText.writeTo(it)
            }

            file.close()
        }

        return symbols.filterNot { it.validate() }.toList()
    }
}