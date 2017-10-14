package rs.dodalovic.asciidoc

import org.asciidoctor.AsciiDocDirectoryWalker
import org.asciidoctor.Asciidoctor.Factory.create
import org.asciidoctor.Options
import org.asciidoctor.OptionsBuilder.options
import org.asciidoctor.SafeMode
import rs.dodalovic.asciidoc.ProcessorFactory.ApplicationMode.DIR
import rs.dodalovic.asciidoc.ProcessorFactory.ApplicationMode.FILE
import java.io.File

fun main(args: Array<String>) {
    ProcessorFactory.getProcessor().process()
}

private fun configOptions(): Options {
    val optionsBuilder = options()
            .safe(SafeMode.UNSAFE)
            .mkDirs(true)
            .backend(System.getProperty("backend") ?: "html")
    System.getProperty("outputDir")?.let { optionsBuilder.toDir(File(it)) }
    return optionsBuilder.get()
}

object ProcessorFactory {
    enum class ApplicationMode {
        FILE, DIR
    }

    private val mode = detectMode()

    fun getProcessor(): Processor {
        return when (mode) {
            FILE -> object : Processor {
                override fun process() {
                    val adocFiles = System.getProperty("files").split(",").map { File(it) }
                    adocFiles.forEach { create().convertFile(it, configOptions()) }
                }
            }
            DIR -> object : Processor {
                override fun process() {
                    System.getProperty("dirs").split(",")
                            .forEach { create().convertDirectory(AsciiDocDirectoryWalker(it), configOptions()) }
                }
            }
        }
    }

    private fun detectMode(): ApplicationMode {
        return try {
            ApplicationMode.valueOf(System.getProperty("mode"))
        } catch (ex: IllegalArgumentException) {
            return FILE
        }
    }
}

interface Processor {
    fun process()
}