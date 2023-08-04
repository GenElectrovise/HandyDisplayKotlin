package me.genel.handydisplay.core.registry

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