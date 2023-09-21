package uk.iatom.handydisplay.helpers

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.logging.*
import uk.iatom.handydisplay.services.plugin.AbstractPlugin
import uk.iatom.handydisplay.services.plugin.ModulePluginLoader

private val hdRunLogger = Logger.getLogger("me.genel.handydisplay.core.HdRunDirectoryHelper")

/** The directory in which program files should be stored */
val runDir = File("hdrun/")

/**
 * Convenience method to get a file on the disk within the `hdrun/` directory. All access to this
 * directory should be done through this method.
 *
 * If a file does not exist on the disk, the function will check whether it exists in one of the
 * available `ClassLoaders`. If a match is found that resource will be copied ('deployed') from the
 * JAR to the disk into the file where it was expected to be.
 *
 * If `plugin` is provided:
 * - The file path will be: `"hdrun/plugins/${plugin.registryName}/${path}"`
 * - Searching for the resource will be done using the plugin's ClassLoader (probably the
 *   `PluginLoader`'s `URLClassLoader`)
 *
 * If `plugin` is null:
 * - The file path will be: `"hdrun/${path}"`
 * - Searching for the resource will be done using `AbstractPlugin`'s `ClassLoader` (probably the
 *   main `AppClassLoader`)
 *
 * @param plugin If non-null, the file will be searched for within this plugin's files within
 *   `hdrun/plugins/`
 * @param name The name of the file to search for, e.g. `path/to/file.txt`
 * @param deployIfNotPresent Whether to, if the given file does not already exist, attempt to load a
 *   default version of it from within the given plugin's JAR if a plugin is given, or from the core
 *   JAR's resources if one is not.
 */
fun hdRunFile(plugin: AbstractPlugin?, name: String, deployIfNotPresent: Boolean = true): File {
  val path = if (plugin == null) name else "plugins/${plugin.registryName}/$name"
  val destFile = File(runDir.absolutePath, path)
  val isProbablyDirectory =
      name.trim().let { it.endsWith('/') || it.endsWith(System.lineSeparator()) }

  if (destFile.isDirectory || isProbablyDirectory) {
    destFile.mkdirs()
  } else if (!destFile.exists() && deployIfNotPresent) {
    destFile.parentFile?.mkdirs()
    deployFile(plugin, name)
  }
  return destFile
}

/**
 * Copies a file from the plugin's JAR to the disk to, for example, set up configuration defaults.
 */
private fun deployFile(plugin: AbstractPlugin?, name: String) {
  hdRunLogger.fine("Deploying file $name from ${plugin?.registryName ?: "<core>"}")

  val path: String =
      if (plugin == null) "/hdrun/$name" else "/hdrun/plugins/${plugin.registryName}/$name"

  // @formatter:off
  // Try many, many, many ways to find that resource!
  // It's alright if this is computationally intensive as this won't be called very often!
  val resource =
      ClassLoader.getSystemClassLoader().getResource(path)
          ?: StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
              .callerClass
              .getResource(path)
              ?: StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
              .callerClass
              .classLoader
              .getResource(path)
              ?: ModulePluginLoader.javaClass.getResource(path)
              ?: ModulePluginLoader.javaClass.classLoader.getResource(path)
  // @formatter:on

  val destinationFile: File =
      hdRunFile(
          plugin,
          name,
          deployIfNotPresent = false) // Plugin is sometimes null and this is a good thing!!

  resource.openStream().use { stream ->
    Files.copy(stream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
  }
}
