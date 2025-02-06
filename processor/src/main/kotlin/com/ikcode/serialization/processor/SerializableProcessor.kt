package com.ikcode.serialization.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.ikcode.serialization.core.annotations.SerializableClass
import com.ikcode.serialization.processor.builders.AbstractBuilder
import com.ikcode.serialization.processor.builders.EnumBuilder
import com.ikcode.serialization.processor.builders.InterfaceBuilder
import com.ikcode.serialization.processor.builders.ProxyBuilder
import com.ikcode.serialization.processor.builders.StandardBuilder
import com.ikcode.serialization.processor.types.TypeUtil
import com.squareup.kotlinpoet.FileSpec

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

        //TODO proxy serialization
        //TODO reference only properties
        val allSourceFiles = resolver.getAllFiles().toList().toTypedArray()
        packers.values.forEach { packer ->
            val fileText = when {
                packer.isEnum -> EnumBuilder(packer)
                packer.isProxy -> ProxyBuilder(packer)
                packer.isAbstract -> AbstractBuilder(packer)
                else -> StandardBuilder(packer, packers)
            }.file()

            write(packer.namespace, packer.outFileName, fileText, allSourceFiles)

            if (packer.isAbstract) {
                val interfaceFileText = InterfaceBuilder(packer).file()
                write(packer.namespace, packer.interfaceFileName, interfaceFileText, allSourceFiles)
            }
        }

        packers.values.flatMap { packer ->
            packer.superclasses.filter { it.isOpen }.map { it to packer }
        }.groupBy {
            it.first
        }.forEach { (service, implementations) ->
            environment.codeGenerator.createNewFile(
                Dependencies.ALL_FILES,
                "",
                "META-INF/services/${service.namespace}.I${service.name}_Packer", //TODO
                ""
            ).apply {
                bufferedWriter().use { writer ->
                    implementations.forEach {
                        writer.write("${it.second.namespace}.${it.second.name}_Packer")
                        writer.newLine()
                    }
                }
                close()
            }
        }

        return symbols.filterNot { it.validate() }.toList()
    }

    private fun write(namespace: String, fileName: String, fileText: FileSpec, dependencies: Array<KSFile>) {
        environment.codeGenerator.createNewFile(
            dependencies = Dependencies.ALL_FILES,
            packageName = namespace,
            fileName = fileName
        ).apply {
            bufferedWriter().use {
                fileText.writeTo(it)
            }
            close()
        }
    }
}
