package io.kotest.generation.property

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.row
import io.kotest.generation.generators.actual.serializeToKotlin
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import io.kotest.datatest.withData

class SaveGeneratedValuesTest: StringSpec() {
    init {
        "generate".config(enabled = false) {

            checkAll<String, String>(5) { a, b ->
//                a + b shouldBe b + a
                println("a: $a, b: $b, sum: ${a + b}")
                serializeToKotlin(a)
            }
        }
    }
}

class MyDataTest: FunSpec(
    {
        context("Pythag triples tests") {
            withData(
                row(1, 2, 3),
                row(2, 3, 5),
            ) { (a, b, c) ->
                a + b shouldBe c
            }
        }
    }
)