# IkSerialization
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/subchannel13/IkSerialization/test.yml)
![GitHub Sponsors](https://img.shields.io/github/sponsors/subchannel13)

Serialization library that handles circular references and cross module polymorphism with just annotations.
```
@SerializableClass
abstract class BaseClass(
    @SerializationData val foo: String
)

@SerializableClass
class DerivedClass(
    foo: String,
    @SerializationData val bar: String
): BaseClass(foo)
```

# How?

The library provides KSP based compiler plugin that scans for `@SerializableClass` and `@SerializationData` annotations and generates serialization code tailor-made for the annotated classes and properties.

In a way, the objects are not serialized by reference rather than value. Each object gets assigned a name (anchor) and those name are used in place of object references. This allows not only to serialize objects with circular references but to also fully restore object relations after deserialization (no object duplication in deserialization if two objects used to reference the same third object). As a consequence serializations results with a list of objects, not just one object.

Internally deserialization works in two steps to in order restore original object relationships. First objects get instantiated with a minimum of data required to call their constructors and then they get hydrated with the rest of data.


Additionally serialization and deserialization work in two stages
 Packing stage will generate

# Examlpe

Serialization works in two stages: packing and serialization proper. Packing stage converts concrete objects to generic maps and lists that can then be serialized to a format of choice.

```
// Object to serialize
val obj = ObjectSample(10)

// Packing to generic maps and lists
val session = PackingSession()
val reference = ObjectSample_Packer().pack(obj, session)

val json = JSONArray()
// Reference to the root object
json.put(serializationMapper(reference))
// Serialization of all relevant objects to JSON
session.referencedData.forEach {
    json.put(serializationMapper(it))
}
```

To start serialization you need a fresh instance of `PackingSession` and call `pack` on and an appropriate packer class (generated for `@SerializableClass` annotated class). This will populate the packing session and return the reference of the "root" object. Then you can use those data to create output in the format of choice. This does move require from library user to write that final piece of logic, but that is not as complex as it sounds. Here is an example of `serializationMapper` function for converting packed objects to JSON for Android:


```
fun serializationMapper(obj: Any, path: String = ""): Any = when(obj) {
    is Map<*, *> -> JSONArray().apply {
        put("map")
        obj.forEach {
            put(serializationMapper(it.key!!, path + ":" + it.key!!.toString()))
            put(serializationMapper(it.value!!, path + "." + it.key!!.toString()))
        }
    }
    is Iterable<*> -> JSONArray().apply {
        put("list")
        obj.forEachIndexed { i, it -> put(serializationMapper(it!!, "$path[$i]")) }
    }
    is ReferencePointer -> JSONObject().apply {
        put("ref", obj.name)
    }
    is ReferenceAnchor -> JSONObject().apply {
        put("ref", obj.pointer.name)
        put("data", serializationMapper(obj.value, "$path[${obj.pointer.name}]"))
    }
    is Double ->
        if (obj.isNaN())
            throw Exception("NaN at $path = $obj")
        else
            obj
    is Float ->
        if (obj.isNaN())
            throw Exception("NaN at $path = $obj")
        else
            obj
    else -> obj
}
```
