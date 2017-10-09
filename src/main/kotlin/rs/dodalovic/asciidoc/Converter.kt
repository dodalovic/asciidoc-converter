package rs.dodalovic.asciidoc

import org.asciidoctor.Asciidoctor.Factory.create
import org.asciidoctor.Options
import org.asciidoctor.OptionsBuilder.options
import org.asciidoctor.SafeMode
import java.io.File

fun main(args: Array<String>) = adocFiles.forEach { create().convertFile(it, configOptions()) }

private fun configOptions(): Options {
    val options = options()
            .safe(SafeMode.SAFE)
            .backend(System.getProperty("backend") ?: "html")
    System.getProperty("outputDir")?.let { options.baseDir(File(it)) }
    return options.get()
}

private val adocFiles: List<File> = System.getProperty("files").split(",").map { File(it) }