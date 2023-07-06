import com.pi4j.library.pigpio.PiGpio

class Test {

}

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    println(PiGpio.newNativeInstance())

    if (Test::class.java.classLoader.getResource("resources_root") == null) {
        throw NullPointerException("Cannot find resources_root. This indicates that the necessary JAR resources are inaccessible.")
    }
}