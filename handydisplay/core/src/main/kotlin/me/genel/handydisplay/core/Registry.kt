package me.genel.handydisplay.core

import kotlin.reflect.KClass

val entries: MutableMap<KClass<out IRegisterable<*>>, MutableMap<String, IRegisterable<*>>> = LinkedHashMap()

inline fun <reified R : IRegisterable<R>> register(item: R) {
    assertRegistryNameValid(item.registryName)

    var map = entries[R::class]
    if (map == null) {
        map = LinkedHashMap()
        entries[R::class] = map
    }
    map[item.registryName] = item
}

inline fun <reified R : IRegisterable<R>> registerAll(items: Set<R>) {
    items.forEach { register<R>(it) }
}

inline fun <reified R : IRegisterable<R>> get(name: String): R? {
    return entries[R::class]
        ?.get(name) as R?
}

@Suppress("UNCHECKED_CAST")
inline fun <reified R : IRegisterable<R>> getAll(): Set<R>? {
    return entries[R::class]
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
