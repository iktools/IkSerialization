package com.ikcode.serialization.processor.builders

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.core.session.UnpackingSession
import com.ikcode.serialization.processor.LineBreaker
import com.ikcode.serialization.processor.PackerInfo
import com.ikcode.serialization.processor.types.ATypeInfo
import com.ikcode.serialization.processor.types.ClassInfo
import com.ikcode.serialization.processor.types.ProxyTypeInfo
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

class StandardBuilder(
    classInfo: PackerInfo
): ABuilder(classInfo) {
    //TODO remove packOwnData function
    override fun pack(funBuilder: FunSpec.Builder) {
        funBuilder.beginControlFlow("return session.referenceFor(obj, \"${classInfo.name}\") { packMap ->")
            .addStatement("packMap.putAll(packOwnData(obj, session))")
        funBuilder.endControlFlow()
    }

    override fun unpack(funBuilder: FunSpec.Builder) {
        if (this.classInfo.superclasses.any { it.isOpen })
            funBuilder.addModifiers(KModifier.OVERRIDE)

        funBuilder.addStatement("val obj = this.instantiate(packedData, session)")
            .addStatement("this.fillData(obj, session)")
            .addStatement("return obj")
    }

    override fun instantiate(funBuilder: FunSpec.Builder) {
        if (this.classInfo.superclasses.any { it.isOpen })
            funBuilder.addModifiers(KModifier.OVERRIDE)

        funBuilder.addStatement("val name = (packedData as %T).name", ReferencePointer::class)
            .addStatement("val objData = session.dereference(name) as Map<*, *>")
            .addStatement("val existingObj = session.getInstance(name)")
            .addStatement("if (existingObj != null) return existingObj as %T", classInfo.kpType)

        if (classInfo.constructorParams.isEmpty())
            funBuilder.addStatement("val obj = %T()", classInfo.kpType)
        else {
            val code = CodeBlock.builder()
            code.add("val obj = %T(", classInfo.kpType)

            val lineBreaker = LineBreaker(classInfo.constructorParams.size, 1)
            if (lineBreaker.multiline)
                code.indent()

            classInfo.constructorParams.forEach { paramName ->
                val field = classInfo.allProperties.firstOrNull {
                    paramName.name == it.ksName
                }!!
                val data = "objData[\"${field.name}\"]!!"

                if (field.type.isNullable)
                    code.add("if (objData.containsKey(\"${field.name}\")) ")

                field.type.instantiate(code, data)

                if (field.type.isNullable)
                    code.add(" else null")

                if (lineBreaker.separate())
                    code.add(",")

                code.add(if (lineBreaker.multiline) "\n" else " ")
            }

            if (lineBreaker.multiline)
                code.unindent()
            code.add(")\n")
            funBuilder.addCode(code.build())
        }

        classInfo.producedData.forEach { property ->
            if (property.type.isNullable)
                funBuilder.beginControlFlow("if (objData.containsKey(\"${property.name}\")) ")

            funBuilder.addStatement("session.rememberProduced(obj.${property.name}!!, name, \"${property.name}\")")

            if (property.type.isNullable)
                funBuilder.endControlFlow()
        }

        funBuilder.addStatement("this.remember(obj, name, session)")
        funBuilder.addStatement("return obj")
    }

    override fun remember(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("val objData = session.dereference(name) as Map<*, *>")

        classInfo.allProperties.filter { property ->
            !property.isMutable
                    && property.type.fillable
                    && needsRemembering(property.type)
                    && classInfo.constructorParams.all {
                it.name != property.ksName
            }
        }.forEach { property ->
            if (property.type.isNullable)
                funBuilder.beginControlFlow("if (objData.containsKey(\"${property.name}\")) ")

            property.type.remember(funBuilder, "obj.${property.name}!!", "(objData[\"${property.name}\"] as ReferencePointer).name, session")

            if (property.type.isNullable)
                funBuilder.endControlFlow()
        }
        funBuilder.addStatement("session.rememberInstance(obj, name)")
    }

    override fun fill(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("if (session.fillGuard(obj)) return")
        funBuilder.addStatement("val objData = session.getData(obj) as Map<*, *>")

        for (field in classInfo.allProperties) {
            val instantiate = field.isMutable && !field.inConstructor
            val fill = field.type.fillable

            if (!instantiate && !fill)
                continue

            if (field.type.isNullable)
                funBuilder.beginControlFlow("if (objData.contains(\"${field.name}\"))")

            val code = CodeBlock.builder()
            if (instantiate)
                code.add("obj.${field.name} = ")

            field.type.fill(
                code,
                "objData[\"${field.name}\"]!!",
                "obj.${field.name}${field.type.nullAssert}",
                instantiate
            )
            code.add("\n")
            funBuilder.addCode(code.build())

            if (field.type.isNullable) {
                funBuilder.endControlFlow()
                if (!field.inConstructor && field.isMutable)
                    funBuilder.addStatement("else obj.${field.name} = null")
            }
        }
    }

    override fun extras(typeBuilder: TypeSpec.Builder) {
        this.classInfo.superclasses.filter {
            it.isOpen
        }.forEach {
            val interfaceFileName = "I${it.declaration.simpleName.asString()}_Packer"
            val interfaceType = ClassName(it.declaration.packageName.asString(), interfaceFileName)
            typeBuilder.addSuperinterface(interfaceType)

            typeBuilder.addFunction(FunSpec.builder("pack")
                .addParameter("obj", it.kpType)
                .addParameter("session", PackingSession::class)
                .addModifiers(KModifier.OVERRIDE)
                .returns(Any::class)
                .addStatement("return this.pack(obj as %T, session)", this.classInfo.kpType)
                .build()
            )

            typeBuilder.addFunction(FunSpec.builder("remember")
                .addParameter("obj", it.kpType)
                .addParameter("name", String::class)
                .addParameter("session", UnpackingSession::class)
                .addModifiers(KModifier.OVERRIDE)
                .addStatement("this.remember(obj as %T, name, session)", this.classInfo.kpType)
                .build()
            )

            typeBuilder.addFunction(FunSpec.builder("fillData")
                .addParameter("obj", it.kpType)
                .addParameter("session", UnpackingSession::class)
                .addModifiers(KModifier.OVERRIDE)
                .addStatement("this.fillData(obj as %T, session)", this.classInfo.kpType)
                .build()
            )
        }

        //TODO remove packOwnFunc
        val packOwnFunc = FunSpec.builder("packOwnData")
            .addParameter("obj", this.classInfo.kpType)
            .addParameter("session", PackingSession::class)
            .returns(Map::class.parameterizedBy(String::class, Any::class))
            .addStatement("val packMap = HashMap<String, Any>()")

        for (field in classInfo.allProperties) {
            val name = field.name

            val getValue = if (field.isMutable && field.type.isNullable)
                name.also { packOwnFunc.addStatement("val $name = obj.$name") }
            else
                "obj.$name"

            if (field.type.isNullable)
                packOwnFunc.beginControlFlow("if ($getValue != null)")

            val codeBuilder = CodeBlock.builder().add("packMap[\"$name\"] = ")
            field.type.pack(codeBuilder, getValue)
            codeBuilder.add("\n")
            packOwnFunc.addCode(codeBuilder.build())

            if (field.type.isNullable)
                packOwnFunc.endControlFlow()
        }
        for (field in classInfo.producedData) {
            if (field.type.isNullable)
                packOwnFunc.beginControlFlow("if (obj.${field.name} != null)")

            //TODO
            packOwnFunc.addStatement("session.register(obj.${field.name}, \"${field.type.name}\", session.referenceFor(obj).name, \"${field.name}\")")

            if (field.type.isNullable)
                packOwnFunc.endControlFlow()
        }
        if (classInfo.superclasses.any())
            packOwnFunc.addStatement("packMap[\"@type\"] = \"${classInfo.name}\"")
        packOwnFunc.addStatement("return packMap")

        val typeNameFunc = FunSpec.builder("typeName")
            .returns(String::class)
            .addStatement("return \"${classInfo.name}\"")

        if (this.classInfo.superclasses.any { it.isOpen }) {
            typeNameFunc.addModifiers(KModifier.OVERRIDE)

            typeBuilder.addFunction(FunSpec.builder("objType")
                .addModifiers(KModifier.OVERRIDE)
                .returns(Class::class.asClassName().parameterizedBy(STAR))
                .addStatement("return %T::class.java", this.classInfo.kpType)
                .build()
            )
        }

        typeBuilder.addFunction(packOwnFunc.build())
            .addFunction(typeNameFunc.build())
    }

    companion object {
        private fun needsRemembering(typeInfo: ATypeInfo): Boolean = when(typeInfo) {
            is ClassInfo -> true
            is ProxyTypeInfo -> needsRemembering(typeInfo.proxy)
            else -> false
        }
    }
}