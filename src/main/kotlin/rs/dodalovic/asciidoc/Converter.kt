package rs.dodalovic.asciidoc

import org.asciidoctor.Asciidoctor.Factory.create
import org.asciidoctor.Options
import org.asciidoctor.OptionsBuilder.options
import org.asciidoctor.SafeMode
import java.io.File

fun main(args: Array<String>) = adocFiles.forEach { create().convertFile(it, configOptions()) }

private fun configOptions(): Options {
    val optionsBuilder = options()
            .safe(SafeMode.UNSAFE)
            .backend(System.getProperty("backend") ?: "html")
    System.getProperty("outputDir")?.let { optionsBuilder.mkDirs(true).toDir(File(it)) }
    return optionsBuilder.get()
}

private val adocFiles: List<File> = System.getProperty("files").split(",").map { File(it) }