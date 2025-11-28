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
import com.squareup.kotlinpoet.FileSpec

//TODO remove
lateinit var logger: KSPLogger

class SerializableProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    private val services = mutableMapOf<SuperclassInfo, MutableList<PackerInfo>>()

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
            environment.logger.info("Making packer", it)
            val info = PackerInfo(it, types, symbols)
            info.justType to info
        }

        packers.values.forEach { packer ->
            environment.logger.info("Making builder", packer.declaration)
            val fileText = when {
                packer.isEnum -> EnumBuilder(packer)
                packer.proxy != null -> ProxyBuilder(packer, types)
                packer.isAbstract -> AbstractBuilder(packer)
                else -> StandardBuilder(packer)
            }.file()

            val otherDependencies = packer.superclasses.filter {
                it.isOpen
            }.mapNotNull {
                packers[it.type]
            }.toMutableSet()

            if (packer.isAbstract) {
                val interfaceFileText = InterfaceBuilder(packer).file()
                write(packer.namespace, packer.interfaceFileName, interfaceFileText, Dependencies(false, packer.file))
                otherDependencies += packer.subclasses.mapNotNull { packers[it.ksType] }
            }

            write(
                packer.namespace,
                packer.outFileName,
                fileText,
                Dependencies(
                    packer.isAbstract,
                    *((otherDependencies + packer).map { it.file }.toTypedArray())
                )
            )
        }

        packers.values.forEach { packer ->
            packer.superclasses.filter {
                it.isOpen
            }.forEach {
                this.services.computeIfAbsent(it) {
                    mutableListOf()
                }.add(packer)
            }
        }

        return symbols.filterNot { it.validate() }.toList()
    }

    override fun finish() {
        this.services.forEach { (service, implementations) ->
            environment.codeGenerator.createNewFileByPath(
                Dependencies(true, *implementations.map { it.file }.toTypedArray()),
                "META-INF/services/${service.namespace}.I${service.name}_Packer", //TODO
                ""
            ).apply {
                bufferedWriter().use { writer ->
                    implementations.forEach {
                        writer.write("${it.namespace}.${it.name}_Packer")
                        writer.newLine()
                    }
                }
                close()
            }
        }
    }

    private fun write(namespace: String, fileName: String, fileText: FileSpec, dependencies: Dependencies) {
        environment.codeGenerator.createNewFile(
            dependencies = dependencies,
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
