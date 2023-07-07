import handy_display.RunCommand
import picocli.CommandLine

fun main(args: Array<String>) {

    println("Starting Handy Display!")
    println("Program arguments: ${args.joinToString()}")

    checkForResources()

    CommandLine(RunCommand()).execute(*args)
}

fun checkForResources() {
    val root = RunCommand::class.java.classLoader.getResource("resources_root")
        ?: throw NullPointerException("Cannot find resources_root. This indicates that the necessary JAR resources are inaccessible.")
    println("Found resources_root: $root")
    println("Resources are intact!")
}