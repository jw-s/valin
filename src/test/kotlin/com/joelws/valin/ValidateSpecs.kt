package com.joelws.valin
/*
Copyright 2016 Joel Whittaker-Smith

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals


class ValidateSpecs : Spek({
    given("a map") {

        on("calling validate") {
            val result = createValidation(email = "example,@example.com", password = "abcdefgh")

            it("should return map consisting of errors") {
                assertEquals(mapOf("email" to listOf("Email address is invalid"), "password" to listOf("Password must contain at least 1 Uppercase character")), result)
            }
            on("putting uppercase in password but reducing to sub 8 characters") {
                val nextResult = createValidation(email = "example,@example.com", password = "abCdefg")

                it("should return new error and removed old") {
                    assertEquals(mapOf("email" to listOf("Email address is invalid"), "password" to listOf("Password must be at least 8 characters")), nextResult)
                }
            }

        }
    }
    given("a map") {
        val map = mapOf("x" to 20)

        on("calling validate") {
            val result = map.validate { validate("x", "Not a Number") { x: Int -> x is Number } }

            it("should return empty map") {
                assertEquals(emptyMap(), result)
            }
        }
    }

    given("a map") {
        val map = mapOf("x" to 14)

        on("calling validate") {
            val result = map.validate {
                validate("x", "Not greater than 15") { x: Int -> x > 15 }
                validate("x", "Not greater than 20") { x: Int -> x > 20 }
            }

            it("should return 2 errors associated with the x key") {
                assertEquals(mapOf("x" to listOf("Not greater than 15", "Not greater than 20")), result)
            }
        }
    }

})

private fun createValidation(email: String, password: String): Map<String, List<String>> = mapOf("email" to email, "password" to password).validate {
    validate("email", "Email address is invalid") { x: String -> isValidEmailAddress(x) }
    validate("password", "Password must be at least 8 characters") { p: String -> p.length >= 8 }
    validate("password", "Password must contain at least 1 Uppercase character") { p: String -> p.count { it.isUpperCase() } > 0 }
}
