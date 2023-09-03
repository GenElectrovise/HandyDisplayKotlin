package uk.iatom.handydisplay.services.widget

import javafx.fxml.FXMLLoader
import org.apache.logging.log4j.kotlin.Logging
import java.io.FileNotFoundException

/**
 * Helper object for loading FXML files for widgets.
 */
object FXMLHelper: Logging {


    /**
     * Convenience method to load a JavaFX pane of type `T` from an FXML file located at the given path within the JAR containing this class. This
     * method avoids nasty issues with ClassLoaders, or JavaFX being unable to find a controller class.
     *
     * This may or may not work if the controller class was loaded by a different ClassLoader. This is dependant on whether the parent ClassLoader
     * will search its siblings/parents/children if it does not find a class. Fortunately, all plugin JARs in this app are loaded by the same
     * URLClassLoader, whose parent is the main AppClassLoader (or equivalent in other environments), so controllers from other plugins should be
     * perfectly accessible.
     *
     * TODO Check whether controllers within other ClassLoaders can be used.
     */
    fun <C, L> loadFXML(resourcePath: String): LoadResult<C, L> {
        try {
            val mcl = ClassLoader.getSystemClassLoader()
            val url = mcl.getResource(resourcePath) ?: throw FileNotFoundException(
                    "Cannot find resource at path $resourcePath in ClassLoader $mcl"
                                                                                  )
            val loader = FXMLLoader()
            loader.location = url

            // ++++++
            loader.classLoader = mcl  // This line is very, very important!!
            // ++++++

            val loaded: L = loader.load()

            return LoadResult(
                    loader.getController(),
                    loaded
                             )
        } catch (cnf: ClassNotFoundException) {
            logger.fatal("Error loading FXML content from '$resourcePath'")
            logger.fatal("ClassNotFoundException *may* indicate that a controller was designated in the given FXML.")
            logger.fatal("Remove this designation and try again.")
            logger.fatal(cnf)
            throw cnf
        }
    }


    /**
     * Returned by `loadFXML(..)`. Handy way of getting access to otherwise obscured properties
     * like the FXML controller.
     */
    data class LoadResult<C, T>(
            val controller: C,
            val rootComponent: T
                               )
}