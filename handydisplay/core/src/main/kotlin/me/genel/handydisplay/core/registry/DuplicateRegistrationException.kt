package me.genel.handydisplay.core.registry

import kotlin.reflect.KClass

/**
 * An exception which is thrown when calling `register(..)` to add an item to a registry sub-map.json
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