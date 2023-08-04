package me.genel.handydisplay.core

import me.genel.handydisplay.core.plugin.AbstractPlugin
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

private val hdRunLogger = org.apache.logging.log4j.kotlin.logger("me.genel.handydisplay.core.HdRunDirectoryHelper")


/**
 * The directory in which program files should be stored
 */
val runDir = File("hdrun/")


/**
 * Convenience method to get a file on the disk within the `hdrun/` directory. All access to this directory should be
 * done through this method.
 *
 * If a file does not exist on the disk, the function will check whether it exists in one of the available `ClassLoaders`. If a match is found that
 * resource will be copied ('deployed') from the JAR to the disk into the file where it was expected to be.
 *
 * If `plugin` is provided:
 *  - The file path will be: `"hdrun/plugins/${plugin.registryName}/${path}"`
 *  - Searching for the resource will be done using the plugin's ClassLoader (probably the `PluginLoader`'s `URLClassLoader`)
 *
 * If `plugin` is null:
 *  - The file path will be: `"hdrun/${path}"`
 *  - Searching for the resource will be done using `AbstractPlugin`'s `ClassLoader` (probably the main `AppClassLoader`)
 *
 * @param plugin If non-null, the file will be searched for within this plugin's files within `hdrun/plugins/`
 * @param name The name of the file to search for, e.g. `path/to/file.txt`
 * @param deployIfNotPresent Whether to, if the given file does not already exist, attempt to load a default version of it from within the given
 * plugin's JAR if a plugin is given, or from the core JAR's resources if one is not.
 */
fun hdRunFile(plugin: AbstractPlugin?, name: String, deployIfNotPresent: Boolean = true): File {
    val path = if (plugin == null) name else "plugins/${plugin.registryName}/$name"
    val file = File(runDir.absolutePath, path)
    file.parentFile?.mkdirs()
    if (!file.exists() && deployIfNotPresent) {
        deployFile(plugin, name)
    }
    return file
}


/**
 * Copies a file from the plugin's JAR to the disk to, for example, set up configuration defaults.
 */
private fun deployFile(plugin: AbstractPlugin?, name: String) {
    hdRunLogger.debug("Deploying file $name from ${plugin?.registryName ?: "<core>"}")

    val resource: InputStream = if (plugin != null) {
        val path = "hdrun/plugins/${plugin.registryName}/$name"
        plugin.javaClass.classLoader.getResourceAsStream(path)
                ?: throw NullPointerException("Cannot deploy non-existent plugin file $path from plugin ${plugin.registryName}")
    } else {
        val path = "hdrun/$name"
        AbstractPlugin::class.java.classLoader.getResourceAsStream(path) ?: throw NullPointerException("Cannot deploy non-existent core file $path.")
    }
    val destinationFile: File = hdRunFile(plugin, name, deployIfNotPresent = false) // Plugin is sometimes null and this is a good thing!!

    resource.use { stream ->
        Files.copy(stream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}