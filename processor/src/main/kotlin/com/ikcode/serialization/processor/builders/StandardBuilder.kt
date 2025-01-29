package com.ikcode.serialization.processor.builders

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
import com.ikcode.serialization.processor.PackerInfo
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

class StandardBuilder(
    classInfo: PackerInfo
): ABuilder(classInfo) {
    //TODO
    private val objInterface = null

    override fun pack(funBuilder: FunSpec.Builder) {
        funBuilder.beginControlFlow("return session.referenceFor(obj, \"${classInfo.name}\") { packMap ->")
            .addStatement("packMap.putAll(packOwnData(obj as ${classInfo.name}, session))")
        //TODO
        /*for(superClass in superclasses)
            funBuilder.addStatement("packMap.putAll(${superClass.fullName}_Packer().packOwnData(obj, session))")*/
        /*if (classInfo.subclasses.isNotEmpty()) {
            funBuilder.beginControlFlow("when(obj)")
            for (subclass in classInfo.subclasses)
                funBuilder.addStatement("is ${subclass.fullName} -> packMap.putAll(${subclass.fullName}_Packer().packOwnData(obj, session))", subclass.)
            if (classInfo.isOpen)
                funBuilder.addStatement("else -> packMap.putAll(otherSubclasses[obj.javaClass]?.packOwnData(obj, session) ?: throw Exception(\"Unknown·${classInfo.name}·subtype\"))")
            else
                funBuilder.addStatement("else -> throw Exception(\"Unknown ${classInfo.name} subtype\")")
            funBuilder.endControlFlow()
        }*/
        funBuilder.endControlFlow()
    }

    override fun unpack(funBuilder: FunSpec.Builder) {
        funBuilder.addStatement("val obj = this.instantiate(packedData, session)")
            .addStatement("this.fillData(obj, session)")
            .addStatement("return obj")
        if (this.objInterface != null)
            funBuilder.addModifiers(KModifier.OVERRIDE)
    }

    override fun instantiate(funBuilder: FunSpec.Builder) {
        val multilineConstructor = 1

        funBuilder.addStatement("val name = (packedData as %T).name", ReferencePointer::class)
            .addStatement("val objData = session.dereference(name) as Map<*, *>")
            .addStatement("val existingObj = session.getInstance(name)")
            .addStatement("if (existingObj != null) return existingObj as ${classInfo.name}")
        if (this.objInterface != null)
            funBuilder.addModifiers(KModifier.OVERRIDE)
        when {
            //TODO
            /*subclasses.any() -> {
                instantiateFunc.beginControlFlow("val obj = when(objData[\"@type\"])")
                for(subclass in subclasses)
                    instantiateFunc.addStatement("\"${subclass.name}\" -> ${subclass.fullName}_Packer().instantiate(packedData, session)")
                if (classInfo.isOpen)
                    instantiateFunc.addStatement("else -> subclassPackerNames[objData[\"@type\"]]?.instantiate(packedData, session) ?: throw Exception(\"Unknown·${classInfo.name}·subtype\")")
                else
                    instantiateFunc.addStatement("else -> throw Exception(\"Unknown ${classInfo.name} subtype\")")
                instantiateFunc.endControlFlow()
            }*/
            classInfo.constructorParams.isEmpty() -> funBuilder.addStatement("val obj = ${classInfo.name}()")
            else -> {
                val code = CodeBlock.builder()
                code.add("val obj = ${classInfo.name}(")

                val paramCount = classInfo.constructorParams.size
                if (paramCount > multilineConstructor)
                    code.indent()

                classInfo.constructorParams.forEachIndexed { i, paramName ->
                    val field = classInfo.allProperties.firstOrNull {
                        paramName.name == it.ksName
                    }!!
                    val data = "objData[\"${field.name}\"]!!"

                    if (field.type.isNullable)
                        code.add("if (objData.containsKey(\"${field.name}\")) ")

                    field.type.instantiate(code, data)

                    if (field.type.isNullable)
                        code.add(" else null")

                    if (i < paramCount - 1)
                        code.add(",")

                    if (paramCount > multilineConstructor)
                        code.add("\n")
                    else
                        code.add(" ")
                }
                /*code.append(classInfo.constructorParams
                    .joinToString(", ") { paramName ->
                        val field = classInfo.allProperties.firstOrNull {
                            paramName.name == it.ksName
                        }!!

                        val instantiation = instantiateParamFunc(
                            field.type,
                            "objData[\"${field.name}\"]!!"
                        )
                        if (field.type.isNullable)
                            "if (objData.containsKey(\"${field.name}\")) $instantiation else null"
                        else
                            instantiation
                    })*/
                if (paramCount > multilineConstructor)
                    code.unindent()
                code.add(")\n")
                funBuilder.addCode(code.build())
            }
        }
        funBuilder.addStatement("session.rememberInstance(obj, name)")
        /*classInfo.referenceOnlyFields.forEach { field ->
            instantiateFunc.addStatement("session.rememberInstance(obj.${field.name}, (objData[\"${field.name}\"]!! as %T).name)", ReferencePointer::class)
            instantiateFunc.addStatement("session.fillGuard(obj.${field.name})")
        }*/
        funBuilder.addStatement("return obj")
    }

    override fun fill(funBuilder: FunSpec.Builder) {
        //TODO
        /*if (!subclasses.any())
            fillFunc.addStatement("if (session.fillGuard(obj)) return")*/
        funBuilder.addStatement("val objData = session.getData(obj) as Map<*, *>")

        if (objInterface != null) {
            funBuilder.addModifiers(KModifier.OVERRIDE)
                .addStatement("obj as $this.classInfo.kpType")
        }

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
                if (!field.inConstructor)
                    funBuilder.addStatement("else obj.${field.name} = null")
            }

        }

        /*if (subclasses.any()) {
            fillFunc.beginControlFlow("when(obj)")
            for (subclass in subclasses)
                fillFunc.addStatement("is ${subclass.fullName} -> ${subclass.fullName}_Packer().fillData(obj, session)")
            if (classInfo.isOpen)
                fillFunc.addStatement("else -> otherSubclasses[obj.javaClass]?.fillData(obj, session) ?: throw Exception(\"Unknown·${classInfo.name}·subtype\")")
            else
                fillFunc.addStatement("else -> throw Exception(\"Unknown ${classInfo.name} subtype\")")
            fillFunc.endControlFlow()
        }*/
    }

    override fun extras(typeBuilder: TypeSpec.Builder) {
        val packOwnFunc = FunSpec.builder("packOwnData")
            .addParameter("obj", this.objInterface ?: this.classInfo.kpType)
            .addParameter("session", PackingSession::class)
            .returns(Map::class.parameterizedBy(String::class, Any::class))
            .addStatement("val packMap = HashMap<String, Any>()")
        //TODO
        /*if (objInterface != null) {
            packOwnFunc
                .addModifiers(KModifier.OVERRIDE)
                .addStatement("obj as $targetClass")
        }*/
        for (field in classInfo.allProperties) {
            val name = field.name

            val getValue = if (field.isMutable && field.type.isNullable)
                name.also { packOwnFunc.addStatement("val $name = obj.$name") }
            else
                "obj.$name"

            if (field.type.isNullable)
                packOwnFunc.beginControlFlow("if ($getValue != null)")

            if (field in classInfo.referenceOnlyFields)
                packOwnFunc.addStatement("packMap[\"$name\"] = session.registerProduced($getValue, %T.typeName(), obj)", "${field.type.name}_Packer()")
            else {
                val codeBuilder = CodeBlock.builder().add("packMap[\"$name\"] = ")
                field.type.pack(codeBuilder, getValue)
                codeBuilder.add("\n")
                packOwnFunc.addCode(codeBuilder.build())
            }

            if (field.type.isNullable)
                packOwnFunc.endControlFlow()
        }
        if (classInfo.superclasses.any() || objInterface != null || classInfo.referenceOnlyFields.any())
            packOwnFunc.addStatement("packMap[\"@type\"] = \"${classInfo.name}\"")
        packOwnFunc.addStatement("return packMap")

        val typeNameFunc = FunSpec.builder("typeName")
            .returns(String::class)
            .addStatement("return \"${classInfo.name}\"")

        //TODO
        /*if (classInfo.service != null) {
            val interfacePacker = "${classInfo.service.packageName}.I${classInfo.service.simpleName}_Packer"
            interfaceImplementations.putIfAbsent(interfacePacker, mutableSetOf())
            interfaceImplementations[interfacePacker]!! += "${classInfo.namespace}.$fileName"
        }

        if (objInterface != null) {
            val packerInterface = ClassName(objInterface.packageName, "I${objInterface.simpleName}_Packer")
            typeNameFunc.addModifiers(KModifier.OVERRIDE)
            type
                .addSuperinterface(packerInterface)
                .addFunction(FunSpec.builder("objType")
                    .addModifiers(KModifier.OVERRIDE)
                    .returns(ClassName("java.lang", "Class").parameterizedBy(WildcardTypeName.producerOf(objInterface)))
                    .addStatement("return ${classInfo.name}::class.java")
                    .build())
        }

        if (classInfo.referenceOnlyFields.any()) {
            typeNameFunc.addModifiers(KModifier.OVERRIDE)
            type
                .addSuperinterface(IFactoryPacker::class)
                .addFunction(FunSpec.builder("produce")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("pointer", ReferencePointer::class)
                    .addParameter("session", UnpackingSession::class)
                    .addStatement("return this.instantiate(pointer, session)")
                    .returns(Any::class)
                    .build()
                )

            val service = IFactoryPacker::class.qualifiedName!!
            interfaceImplementations.putIfAbsent(service,  mutableSetOf())
            interfaceImplementations[service]!! += "${classInfo.namespace}.$fileName"
        }*/

        typeBuilder.addFunction(packOwnFunc.build())
            .addFunction(typeNameFunc.build())
    }
}