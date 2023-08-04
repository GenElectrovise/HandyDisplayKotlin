package me.genel.handydisplay.core

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceOrFileSource
import java.io.File

/**
 * Read configuration from an arbitrary file into a new object of type T.
 */
inline fun <reified T: Any> fileConfig(file: File): T {
    val builder = ConfigLoaderBuilder.default()
    builder.addResourceOrFileSource(file.absolutePath)
    return builder.build().loadConfigOrThrow<T>()
}