package io.kotest.generation.actual

import io.kotest.generation.common.ImportsGenerator
import io.kotest.generation.common.variableNameOf
import io.kotest.generation.generators.actual.NamedCode
import kotlin.math.exp

class DataRowGenerator {
    private val factory = ActualInstanceFactory()
    private val rows = mutableListOf<String>()
    private val exports = mutableSetOf<String>()

    fun addRow(vararg instances: Any) {
        val serializedInstances = instances.map {
            NamedCode(
                variableNameOf(it::class),
                factory.serializeInstance(it)
            )
        }
        exports.addAll(ImportsGenerator().generate(serializedInstances.map { it.code }))
        val row = """row(${serializedInstances.map { instance -> instance.code.sourceCodeAsOneString() }.joinToString(", ")}),"""
        rows.add(row)
    }

    fun rows() = rows.toList()
    fun exports() = exports.toList()

    override fun toString(): String {
        return """
            |${exports().joinToString(", ") { "import $it" }}
            |
            |${rows().joinToString("\n")}
        """.trimMargin()
    }
}