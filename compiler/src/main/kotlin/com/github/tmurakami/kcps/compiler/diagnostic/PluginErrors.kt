package com.github.tmurakami.kcps.compiler.diagnostic

import com.github.tmurakami.kcps.Dumpable
import org.jetbrains.kotlin.diagnostics.Diagnostic
import org.jetbrains.kotlin.diagnostics.DiagnosticFactory0
import org.jetbrains.kotlin.diagnostics.Errors
import org.jetbrains.kotlin.diagnostics.Severity
import org.jetbrains.kotlin.diagnostics.rendering.DefaultErrorMessages
import org.jetbrains.kotlin.diagnostics.rendering.DiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.reportFromPlugin
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.resolve.checkers.DeclarationCheckerContext

internal object PluginErrors {
    @JvmField
    val DATA_ONLY: DiagnosticFactory0<KtAnnotationEntry> = DiagnosticFactory0.create<KtAnnotationEntry>(Severity.ERROR)

    init {
        Errors.Initializer.initializeFactoryNames(PluginErrors::class.java)
    }
}

internal fun <D : Diagnostic> DeclarationCheckerContext.reportFromPlugin(diagnostic: D) =
    trace.reportFromPlugin(diagnostic, PluginErrorsRendering)

private object PluginErrorsRendering : DefaultErrorMessages.Extension {
    private val MAP = DiagnosticFactoryToRendererMap().apply {
        put(PluginErrors.DATA_ONLY, "@${Dumpable::class.java.simpleName} can only be annotated on a data class")
        setImmutable()
    }

    override fun getMap(): DiagnosticFactoryToRendererMap = MAP
}
