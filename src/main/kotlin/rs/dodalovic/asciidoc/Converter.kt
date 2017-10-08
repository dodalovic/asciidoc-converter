package rs.dodalovic.asciidoc

import org.asciidoctor.Asciidoctor.Factory.create
import org.asciidoctor.OptionsBuilder.options
import org.asciidoctor.SafeMode
import java.io.File

fun main(args: Array<String>) = asciidocFiles().forEach { create().convertFile(it, configOptions()) }

private fun configOptions() = options().safe(SafeMode.SAFE).backend(System.getProperty("backend") ?: "html")

private fun asciidocFiles() = System.getProperty("asciidocFiles").split(",").map { File(it) }