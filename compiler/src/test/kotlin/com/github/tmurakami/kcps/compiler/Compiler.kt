package com.github.tmurakami.kcps.compiler

import org.jetbrains.kotlin.cli.common.CLICompiler
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.CommonCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.K2JSCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.PlainTextMessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.js.K2JSCompiler
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import org.jetbrains.kotlin.incremental.classpathAsList
import org.jetbrains.kotlin.incremental.destinationAsFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread
import kotlin.test.assertEquals

private const val KEY_JS_LIBRARIES = "com.github.tmurakami.kcps.compiler.jsLibraries"
private const val KEY_JVM_CLASSPATH = "java.class.path"

interface Compiler {
    fun compile(vararg files: KotlinFile): Compilation
}

data class KotlinFile(val name: String, val source: String) {
    init {
        require(name.indexOfAny(charArrayOf('/', File.separatorChar)) == -1) { "name must not contain separator chars" }
        require(name.endsWith(".kt")) { "name must end with '.kt'" }
    }
}

data class Compilation(val exitCode: ExitCode, val message: String)

fun Compilation.assertSucceeded() = assertEquals(ExitCode.OK, exitCode)

fun jsCompiler(): Compiler = object : AbstractCompiler<K2JSCompilerArguments>(K2JSCompiler()) {
    override fun createArguments(outputDir: File, classpathFiles: List<File>): K2JSCompilerArguments =
        K2JSCompilerArguments().apply {
            noStdlib = true
            outputFile = File(outputDir, "out.js").path
            libraries = System.getProperty(KEY_JS_LIBRARIES)
        }
}

fun jvmCompiler(): Compiler = jvmCompiler(useIr = false)
fun jvmIrCompiler(): Compiler = jvmCompiler(useIr = true)

private fun jvmCompiler(useIr: Boolean): Compiler = object : AbstractCompiler<K2JVMCompilerArguments>(K2JVMCompiler()) {
    override fun createArguments(outputDir: File, classpathFiles: List<File>): K2JVMCompilerArguments =
        K2JVMCompilerArguments().apply {
            noStdlib = true
            destinationAsFile = outputDir
            classpathAsList = classpathFiles
            useIR = useIr
        }
}

private object NameMessageRenderer : PlainTextMessageRenderer() {
    override fun getPath(location: CompilerMessageLocation): String = File(location.path).name
    override fun getName(): String = "Name"
}

private abstract class AbstractCompiler<A : CommonCompilerArguments> protected constructor(
    private val compiler: CLICompiler<A>
) : Compiler {
    override fun compile(vararg files: KotlinFile): Compilation {
        val tempDir = createTempDir("KotlinCompilerPluginSample", "").apply {
            Runtime.getRuntime().addShutdownHook(thread(start = false) { deleteRecursively() })
        }
        val srcDir = File(tempDir, "src").apply { mkdir() }
        val outDir = File(tempDir, "out").apply { mkdir() }
        val classpath = System.getProperty(KEY_JVM_CLASSPATH)
            .splitToSequence(File.pathSeparatorChar)
            .map(::File)
            .filter(File::exists)
            .toList()
        val args = createArguments(outDir, classpath).apply {
            freeArgs = files.map { (name, source) -> srcDir.resolve(name).apply { writeText(source) }.path }
            pluginClasspaths = classpath.map(File::getPath).toTypedArray()
        }
        val out = ByteArrayOutputStream()
        val messageCollector = PrintingMessageCollector(PrintStream(out), NameMessageRenderer, false)
        val exitCode = compiler.execImpl(messageCollector, Services.EMPTY, args)
        return Compilation(exitCode, out.toString(StandardCharsets.UTF_8.name()))
    }

    protected abstract fun createArguments(outputDir: File, classpathFiles: List<File>): A
}
