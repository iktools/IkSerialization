package com.ikcode.serialization.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.processor.builders.AbstractBuilder
import com.ikcode.serialization.processor.builders.EnumBuilder
import com.ikcode.serialization.processor.builders.InterfaceBuilder
import com.ikcode.serialization.processor.builders.ProxyBuilder
import com.ikcode.serialization.processor.builders.StandardBuilder
import com.ikcode.serialization.processor.types.TypeUtil

//TODO remove
lateinit var logger: KSPLogger

class SerializableProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation(SerializableClass::class.java.canonicalName)
            .filterIsInstance<KSClassDeclaration>()
            .toSet()

        if (!symbols.iterator().hasNext())
            return emptyList()

        val types = TypeUtil(resolver)

        logger = environment.logger
        val packers = symbols.associate {
            val info = PackerInfo(it, types, symbols)
            info.justType to info
        }

        val allSourceFiles = resolver.getAllFiles().toList().toTypedArray()
        packers.values.forEach { packer ->
            val fileText = when {
                packer.isEnum -> EnumBuilder(packer)
                packer.isProxy -> ProxyBuilder(packer)
                packer.isAbstract -> AbstractBuilder(packer)
                else -> StandardBuilder(packer, packers)
            }.file()

            environment.codeGenerator.createNewFile(
                dependencies = Dependencies(false, *allSourceFiles),
                packageName = packer .namespace,
                fileName = packer.outFileName
            ).apply {
                bufferedWriter().use {
                    fileText.writeTo(it)
                }
                close()
            }

            if (packer.isAbstract) {
                val interfaceFileText = InterfaceBuilder(packer).file()

                environment.codeGenerator.createNewFile(
                    dependencies = Dependencies(false, *allSourceFiles),
                    packageName = packer.namespace,
                    fileName = packer.interfaceFileName
                ).apply {
                    bufferedWriter().use {
                        interfaceFileText.writeTo(it)
                    }
                    close()
                }
            }
        }

        /*val resourceFile = "META-INF/services/$service"
        val filer = processingEnv.filer
        try {
            val existingFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile)
            if (existingFile.lastModified != 0L) {
                val reader = BufferedReader(InputStreamReader(existingFile.openInputStream(), UTF_8))
                interfaceImplementations[service]!! += reader.readLines()
                    .map { it.substringBefore('#').trim() }
                    .filter { it.isNotEmpty() }

                reader.close()
            }
        } catch (e: IOException) {
            processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, e.message + e.stackTraceToString())
        }

        val metaFile = filer
            .createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/services/$service")
            .openWriter()
        implementations.forEach {
            metaFile.write(it)
            metaFile.appendLine()
        }
        metaFile.flush()
        metaFile.close()*/

        return symbols.filterNot { it.validate() }.toList()
    }
}
