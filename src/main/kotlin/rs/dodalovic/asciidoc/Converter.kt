package rs.dodalovic.asciidoc

import org.asciidoctor.AsciiDocDirectoryWalker
import org.asciidoctor.Asciidoctor.Factory.create
import org.asciidoctor.Options
import org.asciidoctor.OptionsBuilder.options
import org.asciidoctor.SafeMode
import java.io.File

fun main(args: Array<String>) {
    RequestProcessorFactory.getProcessor().process()
}

private fun configOptions(): Options {
    val optionsBuilder = options()
            .safe(SafeMode.UNSAFE)
            .mkDirs(true)
            .backend(System.getProperty("backend") ?: "html")
    System.getProperty("outputDir")?.let { optionsBuilder.toDir(File(it)) }
    return optionsBuilder.get()
}

object RequestProcessorFactory {
    enum class ApplicationMode {
        FILE, DIR
    }

    private val mode = detectMode()

    fun getProcessor(): RequestProcessor {
        return when (mode) {
            ApplicationMode.FILE -> FileProcessor()
            ApplicationMode.DIR -> DirProcessor()
            else -> FileProcessor()
        }
    }

    private fun detectMode(): ApplicationMode {
        return try {
            ApplicationMode.valueOf(System.getProperty("mode"))
        } catch (ex: IllegalArgumentException) {
            return ApplicationMode.FILE
        }
    }
}

interface RequestProcessor {
    fun process()
}

class FileProcessor : RequestProcessor {
    override fun process() {
        val adocFiles = System.getProperty("files").split(",").map { File(it) }
        adocFiles.forEach {
            create().convertFile(it, configOptions())
        }
    }
}

class DirProcessor : RequestProcessor {
    override fun process() {
        System.getProperty("dirs").split(",")
                .forEach { create().convertDirectory(AsciiDocDirectoryWalker(it), configOptions()) }
    }
}