package com.github.joelws.valin
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
import java.util.*

class Validator<K, V>() {

    val validators = ArrayList<Triple<K, Function1<V, Boolean>, String>>()

    inline fun validate(validations: Validator<K, V>.() -> Triple<K, Function1<V, Boolean>, String>) {
        validators.add(validations())
    }
}

fun <K, V> validateTarget(key: K, predicate: Function1<V, Boolean>, error: String): (Map<K, V>) -> Map<K, MutableList<String>> {

    return fun(valueMap: Map<K, V>): Map<K, MutableList<String>> {
        val value = valueMap[key]

        val functionResult = value?.let(predicate) ?: false

        return if (functionResult) emptyMap() else mapOf(Pair(key, mutableListOf(error)))

    }

}

fun <K> mergeErrors(errorMaps: List<Map<K, MutableList<String>>>) = errorMaps.reduce { map1, map2 -> mergeWith(map1, map2) }

fun <K, V> validate(valueMap: Map<K, V>, validations: List<Triple<K, Function1<V, Boolean>, String>>) =
        mergeErrors(validations.map { validate -> validateTarget(validate.first, validate.second, validate.third) }.mapNotNull { f -> f(valueMap) })

inline fun <K, V> Map<K, V>.validate(body: Validator<K, V>.() -> Unit): Map<K, List<String>> {
    val validators = Validator<K, V>()
    validators.body()
    return validate(this, validators.validators)
}


private fun <K> mergeWith(from: Map<K, MutableList<String>>, other: Map<K, MutableList<String>>): Map<K, MutableList<String>> {
    val result = LinkedHashMap<K, MutableList<String>>().apply {
        putAll(from)
    }

    for ((k, v) in other) {
        val existing = result[k]

        if (existing == null) {
            result[k] = v
        } else {
            result[k]?.addAll(v)
        }
    }

    return result
}
