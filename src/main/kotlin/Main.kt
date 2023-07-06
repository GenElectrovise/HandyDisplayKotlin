import com.pi4j.library.pigpio.PiGpio
import org.apache.logging.log4j.kotlin.Logging

class Test() : Logging {

    init {
        println("Testing logging...")
        logger.debug("Debug!")
        logger.info("Info!")
        logger.fatal("Fatal!")
        println("Test over!")
    }
}

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    println(PiGpio.newNativeInstance())

    checkForResources()
    Test()
}

fun checkForResources() {
    if (Test::class.java.classLoader.getResource("resources_root") == null) {
        throw NullPointerException("Cannot find resources_root. This indicates that the necessary JAR resources are inaccessible.")
    }

    println("Resources are intact!")
}