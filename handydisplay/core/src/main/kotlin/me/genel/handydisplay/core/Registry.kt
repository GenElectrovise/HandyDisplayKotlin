package me.genel.handydisplay.core

import kotlin.reflect.KClass

val registryEntries: MutableMap<KClass<out IRegisterable<*>>, MutableMap<String, IRegisterable<*>>> = LinkedHashMap()
val registryLogger = org.apache.logging.log4j.kotlin.logger("me.genel.handydisplay.core.Registry")

inline fun <reified R : IRegisterable<R>> register(item: R) {
    assertRegistryNameValid(item.registryName)

    var map = registryEntries[R::class]
    if (map == null) {
        map = LinkedHashMap()
        registryEntries[R::class] = map
    }
    val old = map[item.registryName]
    if (old != null) {
        val ex = DuplicateRegistrationException(R::class, item.registryName, old, item)
        registryLogger.fatal(ex)
        throw ex
    }

    registryLogger.debug("Registering ${item.registryName}=<${R::class.simpleName}>${item.javaClass.name}")
    map[item.registryName] = item
}

inline fun <reified R : IRegisterable<R>> registerAll(items: Set<R>) {
    items.forEach { register<R>(it) }
}

inline fun <reified R : IRegisterable<R>> get(name: String): R? {
    return registryEntries[R::class]
        ?.get(name) as R?
}

@Suppress("UNCHECKED_CAST")
inline fun <reified R : IRegisterable<R>> getAll(): Set<R>? {
    return registryEntries[R::class]
        ?.toList()
        ?.map { it.second }
        ?.toSet() as Set<R>?
}

@Throws(AssertionError::class)
fun assertRegistryNameValid(registryName: String) {
    assert(
        registryName.asSequence()
            .all { it.isLetterOrDigit() }) { "Internal widget names may only contain letters and digits. $registryName is invalid." }
    assert(registryName.length in 1..32) { "Internal widget names must have [1,32] characters. $registryName is invalid." }
}

interface IRegisterable<out IRegisterable> {
    val registryName: String
}

class DuplicateRegistrationException(registryType: KClass<*>, key: String, entry1: Any, entry2: Any) : Exception(
    StringBuilder()
        .appendLine("Cannot register two items with the same registry name and registry type.")
        .appendLine("Registry Type: ${registryType.qualifiedName}")
        .appendLine("Registry Key: $key")
        .appendLine("Entry #1 Type: ${entry1.javaClass.name}")
        .appendLine("Entry #1 toString: $entry1")
        .appendLine("Entry #2 Type: ${entry2.javaClass.name}")
        .appendLine("Entry #2 toString: $entry2")
        .toString()
)