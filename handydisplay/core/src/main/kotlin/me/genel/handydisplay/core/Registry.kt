package me.genel.handydisplay.core

import me.genel.handydisplay.core.plugin.AbstractPlugin
import me.genel.handydisplay.core.plugin.AbstractWidget
import kotlin.reflect.KClass


object Registry {


    /**
     * Entries in the registry. DON'T TOUCH THIS!! Use `register(..)/registerAll(..)` and `get(..)
     * /getAll(..)` for safe access. Must be public for reified methods.
     */
    val registryEntries: MutableMap<KClass<out IRegistrable<*>>, MutableMap<String, IRegistrable<*>>> =
            LinkedHashMap()
    val registryLogger =
            org.apache.logging.log4j.kotlin.logger("me.genel.handydisplay.core.Registry")


    /**
     * Add the given item of type `R` to the sub-map of type `R`.
     *
     *
     * In Java pseudocode:
     * ```
     * allRegistryEntries = Map<Class, Map<String, IRegistrable>>;
     * sub-map = allRegistryEntries.get(R.class);
     * sub-map.put(name, item)
     * ```
     *
     * @param item The item to add (register)
     * @param R The type (implementing `IRegistrable<R>`) of the sub-map into which the item should be registered.
     */
    inline fun <reified R: IRegistrable<R>> register(item: R) {
        val err = getRegistryNameErrors(item.registryName)
        if (err != null) throw err

        var map = registryEntries[R::class]
        if (map == null) {
            map = LinkedHashMap()
            registryEntries[R::class] = map
        }
        val old = map[item.registryName]
        if (old != null && item !== old) {
            val ex = DuplicateRegistrationException(
                    R::class,
                    item.registryName,
                    old,
                    item
                                                   )
            registryLogger.fatal(ex)
            throw ex
        }

        registryLogger.debug("Registering ${item.registryName}=<${R::class.simpleName}>${item.javaClass.name}")
        map[item.registryName] = item
    }


    /**
     * Convenience method which calls `register(..)` for each item in the given series.
     */
    inline fun <reified R: IRegistrable<R>> registerAll(items: Iterable<R>) {
        items.forEach { register<R>(it) }
    }


    /**
     * Retrieve an item of type `R` from the sub-map of type `R` with the given name.
     *
     * @param name The unique identifier for the item to retrieve, within `R`'s sub-map.
     * @param R The type (a type implementing `IRegistrable<R>`) of the sub-map to query.
     *
     * @see AbstractPlugin
     * @see AbstractWidget
     */
    inline fun <reified R: IRegistrable<R>> get(name: String): R? {
        return registryEntries[R::class]?.get(name) as R?
    }


    /**
     * Retrieve all entries in the sub-map of type `R`.
     *
     * @param R The type (a type implementing `IRegistrable<R>`) of the sub-map to query.
     */
    @Suppress("UNCHECKED_CAST") inline fun <reified R: IRegistrable<R>> getAll(): Set<R>? {
        return registryEntries[R::class]
                ?.toList()
                ?.map { it.second }
                ?.toSet() as Set<R>?
    }
}


/**
 * @return Whether the given name is a valid name for a name of a registered item.
 */
fun getRegistryNameErrors(registryName: String): RegistryNameException? {

    if (!registryName
                    .asSequence()
                    .all { it.isLetterOrDigit() }
    ) {
        return RegistryNameException(
                registryName,
                "Internal widget names may " + "only contain letters and digits."
                                    )
    }
    if (registryName.length !in 1 .. 32) {
        return RegistryNameException(
                registryName,
                "Internal widget names must have [1,32] characters" + ". Found ${registryName.length}"
                                    )
    }
    return null
}


/**
 * Declares that a type may be registered.
 *
 * @sample AbstractPlugin
 */
interface IRegistrable<out IRegistrable> {


    val registryName: String
}


/**
 * An exception thrown when the name of an entry in the registry is somehow invalid.
 *
 * @param registryName The name with which there is an error.
 * @param errorReason An explanation of the error.
 */
class RegistryNameException(
        registryName: String,
        errorReason: String
                           ): Exception(
        StringBuilder()
                .appendLine("$registryName is an invalid name for a registry entry.")
                .appendLine(errorReason)
                .toString()
                                       )


/**
 * An exception which is thrown when calling `register(..)` to add an item to a registry sub-map
 * which already contains an entry for that name.
 */
class DuplicateRegistrationException(
        registryType: KClass<*>,
        key: String,
        entry1: Any,
        entry2: Any
                                    ): Exception(
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