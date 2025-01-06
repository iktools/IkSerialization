package com.ikcode.serialization.processor

import com.ikcode.serialization.core.references.ReferencePointer
import com.ikcode.serialization.core.session.PackingSession
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
            funBuilder.addStatement("packMap.putAll(${superClass.fullName}_Packer().packOwnData(obj, session))")
        if (subclasses.any()) {
            funBuilder.beginControlFlow("when(obj)")
            for (subclass in subclasses)
                funBuilder.addStatement("is ${subclass.fullName} -> packMap.putAll(${subclass.fullName}_Packer().packOwnData(obj, session))")
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
            }
            classInfo.constructorParams.isEmpty() -> instantiateFunc.addStatement("val obj = ${classInfo.name}()")*/
            else -> {
                val code = StringBuilder("val obj = ${classInfo.name}(")
                code.append(classInfo.constructorParams
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
                    })
                code.append(")")
                funBuilder.addStatement(code.toString())
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

        classInfo.allProperties.filter { field ->
            field.isMutable || field.inConstructor
        }.forEach { field ->
            val instantiate = field.isMutable && !field.inConstructor
            val fill = !field.type.isPrimitive

            funBuilder.addComment("TODO $instantiate $fill ${field.type.isNullable}")
            //TODO remove empty blocks for enums
            if (field.type.isNullable)
                funBuilder.beginControlFlow("if (objData.contains(\"${field.name}\"))")

            fillParamType(funBuilder, field.type, "obj.${field.name}", "objData[\"${field.name}\"]!!", instantiate, instantiate, fill)

            //TODO
            /*when {
                /*field.type.isPrimitive -> {
                    when {
                        field.inConstructor -> { /* no operation */ }
                        field.type.isNumber -> funBuilder.addStatement(
                            "obj.${field.name} = (objData[\"${field.name}\"] as Number).to${field.type.name}()"
                        )
                        else -> funBuilder.addStatement(
                            "obj.${field.name} = objData[\"${field.name}\"] as ${field.type.name}"
                        )
                    }
                }*/
                /*field.type.rawType = "java.util.Collection", "java.lang.Iterable" -> {
                    if (!classInfo.constructorFields.contains(field)) {
                        if (field.isNullable)
                            fillFunc.beginControlFlow("if (objData.contains(\"${field.name}\"))")
                        fillFunc.beginControlFlow("obj.${field.name} = (objData[\"${field.name}\"] as List<*>).map { itemData ->")
                        fillFunc.addStatement(instantiateParamFunc(field.type.genericParams[0], "itemData!!"))
                        fillFunc.endControlFlow()
                        if (field.isNullable) {
                            fillFunc.endControlFlow()
                            fillFunc.addStatement("else obj.${field.name} = null")
                        }
                    }
                    if (field.type.genericParams[0].fillable) {
                        fillFunc.beginControlFlow("obj.${field.name}.forEach { item ->")
                        fillParamType(fillFunc, field.type.genericParams[0], "item")
                        fillFunc.endControlFlow()
                    }
                }
                field.type.rawType = "java.util.ArrayList", "kotlin.collections.List", "java.util.HashSet", "java.util.Set" -> {
                    if (!classInfo.constructorFields.contains(field)) {
                        if (field.isNullable) {
                            fillFunc.beginControlFlow("if (objData.contains(\"${field.name}\"))")
                            when (field.type.rawType) {
                                "java.util.ArrayList" -> fillFunc.addStatement("obj.${field.name} = ArrayList()")
                                "java.util.HashSet" -> fillFunc.addStatement("obj.${field.name} = HashSet()")
                            }
                        }
                        fillFunc.beginControlFlow("(objData[\"${field.name}\"] as List<*>).forEach { itemData ->")
                        fillFunc.addStatement("obj.${field.name}${if (field.isNullable) "!!" else ""}.add(${instantiateParamFunc(field.type.genericParams[0], "itemData!!")})")
                        fillFunc.endControlFlow()
                        if (field.isNullable) {
                            fillFunc.endControlFlow()
                            fillFunc.addStatement("else obj.${field.name} = null")
                        }
                    }
                    if (field.type.genericParams[0].fillable) {
                        fillFunc.beginControlFlow("obj.${field.name}${if (field.isNullable) "?." else "."}forEach { item ->")
                        fillParamType(fillFunc, field.type.genericParams[0], "item")
                        fillFunc.endControlFlow()
                    }
                }
                field.type.rawType = "java.util.Map" -> {
                    if (!classInfo.constructorFields.contains(field)) {
                        if (field.isNullable)
                            fillFunc.beginControlFlow("if (objData.contains(\"${field.name}\"))")
                        if (field.isMutable)
                            fillFunc.addStatement("obj.${field.name} = (objData[\"${field.name}\"] as Map<*, *>).map { itemData -> ${instantiateParamFunc(field.type.genericParams[0], "itemData.key!!")}·to ${instantiateParamFunc(field.type.genericParams[1], "itemData.value!!")} }.toMap()")
                        else
                            fillFunc.addStatement("(objData[\"${field.name}\"] as Map<*, *>).forEach { itemData -> obj.${field.name}[${instantiateParamFunc(field.type.genericParams[0], "itemData.key!!")}] = ${instantiateParamFunc(field.type.genericParams[1], "itemData.value!!")} }")
                        if (field.isNullable) {
                            fillFunc.endControlFlow()
                            fillFunc.addStatement("else obj.${field.name} = null")
                        }
                    }
                    if (field.type.genericParams[0].fillable || field.type.genericParams[1].fillable) {
                        fillFunc.beginControlFlow("obj.${field.name}.forEach { itemData ->")
                        if (field.type.genericParams[0].fillable)
                            fillParamType(fillFunc, field.type.genericParams[0], "itemData.key")
                        if (field.type.genericParams[1].fillable)
                            fillParamType(fillFunc, field.type.genericParams[1], "itemData.value!!")
                        fillFunc.endControlFlow()
                    }
                }
                field.type.rawType = "java.util.HashMap" -> {
                    if (!classInfo.constructorFields.contains(field)) {
                        if (field.isNullable) {
                            fillFunc.beginControlFlow("if (objData.contains(\"${field.name}\"))")
                            fillFunc.addStatement("obj.${field.name} = HashMap()")
                        }
                        fillFunc.beginControlFlow("(objData[\"${field.name}\"] as Map<*, *>).forEach { itemData ->")
                        fillFunc.addStatement("obj.${field.name}${if (field.isNullable) "!!" else ""}[${instantiateParamFunc(field.type.genericParams[0], "itemData.key!!")}]·= ${instantiateParamFunc(field.type.genericParams[1], "itemData.value!!")}")
                        fillFunc.endControlFlow()
                        if (field.isNullable) {
                            fillFunc.endControlFlow()
                            fillFunc.addStatement("else obj.${field.name} = null")
                        }
                    }
                    if (field.type.genericParams[0].fillable || field.type.genericParams[1].fillable) {
                        fillFunc.beginControlFlow("obj.${field.name}.forEach { itemData ->")
                        if (field.type.genericParams[0].fillable)
                            fillParamType(fillFunc, field.type.genericParams[0], "itemData.key")
                        if (field.type.genericParams[1].fillable)
                            fillParamType(fillFunc, field.type.genericParams[1], "itemData.value!!")
                        fillFunc.endControlFlow()
                    }
                }*/
                field.isMutable && !field.inConstructor -> {
                    funBuilder.addStatement("obj.${field.name} = ${field.type.name}_Packer().unpack(objData[\"${field.name}\"]!!, session)")
                }
                !field.isMutable && !field.inConstructor -> {
                    // no op, handled in instantiate
                }
                else -> {
                    var getValue = "obj.${field.name}"
                    if (field.type.isNullable) {
                        getValue = field.name
                        funBuilder.addStatement("val ${field.name} = obj.${field.name}")
                        funBuilder.beginControlFlow("if (${field.name} != null)")
                    }
                    if (!field.inConstructor)
                        funBuilder.addStatement("session.rememberInstance($getValue, (objData[\"${field.name}\"]!! as %T).name)", ReferencePointer::class)

                    funBuilder.addStatement("${field.type.name}_Packer().fillData($getValue, session)")
                }
            }*/

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
        for (field in classInfo.ownProperties) {
            val name = field.name

            val getValue = if (field.isMutable && field.type.isNullable)
                name.also { packOwnFunc.addStatement("val $name = obj.$name") }
            else
                "obj.$name"

            if (field.type.isNullable)
                packOwnFunc.beginControlFlow("if ($getValue != null)")

            if (field in classInfo.referenceOnlyFields)
                packOwnFunc.addStatement("packMap[\"$name\"] = session.registerProduced($getValue, ${field.type.fullName}_Packer().typeName(), obj)")
            else
                packOwnFunc.addStatement("packMap[\"$name\"] = ${this.packType(field.type, getValue)}")

            if (field.type.isNullable)
                packOwnFunc.endControlFlow()
        }
        /*if (superclasses.any() || objInterface != null || classInfo.referenceOnlyFields.any())
            packOwnFunc.addStatement("packMap[\"@type\"] = \"${classInfo.name}\"")*/
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