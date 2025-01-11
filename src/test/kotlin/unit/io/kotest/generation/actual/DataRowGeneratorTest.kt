package io.kotest.generation.actual

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.collection
import io.kotest.property.forAll
import java.time.LocalDate

class DataRowGeneratorTest: StringSpec() {
    init {
        "DataRowGenerator" {
            val generator = DataRowGenerator()
            checkAll<Int, LocalDate, String>(5) { a, b, c ->
                generator.addRow(a, b, c, "c=$c, ${b.plusDays(a.toLong())}")
            }
            println(generator.toString())
            generator.exports() shouldContainExactlyInAnyOrder listOf("java.time.LocalDate", "kotlin.Int", "kotlin.String")
//            generator.rows() shouldContainExactly listOf("")
        }
        "specific" {
            val generator = DataRowGenerator()
            forAll(
                Exhaustive.collection(listOf(1L,2L)),
                Exhaustive.collection(listOf(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1))),
            ) { a, b ->
                generator.addRow(a, b, b.atStartOfDay().plusMinutes(a))
                true
            }
            println(generator.toString())
            generator.exports() shouldContainExactlyInAnyOrder listOf(
                "java.time.LocalDate",
                "kotlin.Long",
                "java.time.LocalDateTime"
            )
            generator.rows() shouldContainExactlyInAnyOrder listOf(
                "row(1L, LocalDate.of(2021, 1, 1), LocalDateTime.of(2021, 1, 1, 0, 1, 0, 0)),",
                "row(1L, LocalDate.of(2022, 1, 1), LocalDateTime.of(2022, 1, 1, 0, 1, 0, 0)),",
                "row(2L, LocalDate.of(2021, 1, 1), LocalDateTime.of(2021, 1, 1, 0, 2, 0, 0)),",
                "row(2L, LocalDate.of(2022, 1, 1), LocalDateTime.of(2022, 1, 1, 0, 2, 0, 0)),",
                )
        }
    }
}
