import handy_display.RunCommand
import picocli.CommandLine

fun main(args: Array<String>) {

    println("Starting Handy Display!")
    println("Program arguments: ${args.joinToString()}")

    checkForResources()

    CommandLine(RunCommand()).execute(*args)
}

fun checkForResources() {
    if (RunCommand::class.java.classLoader.getResource("resources_root") == null) {
        throw NullPointerException("Cannot find resources_root. This indicates that the necessary JAR resources are inaccessible.")
    }

    println("Resources are intact!")
}