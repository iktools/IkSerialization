package com.ikcode.serialization.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.ikcode.serialization.core.annotations.SerializableClass

//TODO remove
lateinit var logger: KSPLogger

class SerializableProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation(SerializableClass::class.java.canonicalName)
            .filterIsInstance<KSClassDeclaration>()

        if (!symbols.iterator().hasNext())
            return emptyList()

        val types = TypeUtil(resolver)

        logger = environment.logger
        val packers = symbols.map {
            PackerInfo(it, types)
        }

        val allSourceFiles = resolver.getAllFiles().toList().toTypedArray()
        packers.forEach { packer ->
            val fileText = when {
                packer.isEnum -> EnumBuilder(packer)
                packer.isProxy -> ProxyBuilder(packer)
                else -> StandardBuilder(packer)
            }.file()

            val file = environment.codeGenerator.createNewFile(
                dependencies = Dependencies(false, *allSourceFiles),
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