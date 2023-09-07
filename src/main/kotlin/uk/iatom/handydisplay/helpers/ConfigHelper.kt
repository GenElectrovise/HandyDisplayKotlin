package uk.iatom.handydisplay.helpers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromStringMap
import java.io.File

inline fun <reified T: Any> jsonConfig(file: File): T {
    return Json.decodeFromString<T>(file.readText())
}


@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T: Any> propertiesConfig(file: File): T {
    val stringMap = file
            .readLines()
            .associate {
                val parts = it.split(
                        "=",
                        limit = 2
                                    )
                parts[0] to parts[1]
            }
    return Properties.decodeFromStringMap<T>(stringMap)
}
