package io.kotest.generation.generators.actual

import io.kotest.generation.actual.ActualInstanceFactory
import io.kotest.generation.common.*
import java.io.File

fun serializeToKotlin(vararg instances: Any) =
    serializeToKotlin(
        "Actual${simpleNameOfFirstInstance(*instances)}.kt",
        *instances
    )

fun serializeToKotlin(filename: String,
                            vararg instances: Any
) = serializeToKotlinWithCustomization(
    ActualInstanceFactory(),
    filename,
    *instances
)

fun ActualInstanceFactory.serializeToKotlin(filename: String,
                                            vararg instances: Any
) = serializeToKotlinWithCustomization(
    this,
    filename,
    *instances
)

private fun serializeToKotlinWithCustomization(
    factory: ActualInstanceFactory,
    filename: String,
    vararg instances: Any
) {
    val serializedInstances = instances.map {
        NamedCode(
            variableNameOf(it::class),
            factory.serializeInstance(it)
        )
    }
    val classesToImport = ImportsGenerator().generate(serializedInstances.map { it.code })
    val code = """package generated.code

${classesToImport.joinToString("\n") {"import $it" }}

// generated by io.kotest.generation:kotests-generator
object ${className(filename)} {
${serializedInstances.mapIndexed() { index, instance -> "val ${instance.name}$index = ${instance.code.sourceCodeAsOneString()}" }.joinToString("\n\n")}
}
        """.trimIndent()
    File(filename).writeText(code)
}

data class NamedCode(val name: String, val code: CodeSnippet)

fun simpleNameOfFirstInstance(vararg instances: Any) =
    instances.asSequence().firstOrNull()?.let { it::class.simpleName } ?: "NoArguments"