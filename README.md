# Valin
Valin is a validation library for Kotlin, used to validate keys within any type of map.

# Usage

Contains the map extension function `Validate.kt/Map<K, V>.validate`. It uses the following
syntax:

    map.validate {
    validate { Triple(key, Function1, errorString) }
    validate { Triple(key, Function1, errorString) }
    validate { Triple(key, Function1, errorString) }
    ....
    }

For each validate call, the key is used to look up a value in the map. The map value
is then tested with the predicate function. If the predicate fails, the error
message is included in the map of errors returned by the `validate` extension function. A
key may be tested multiple times with different predicates and errors.

If no predicate fails, `emptyMap()` is returned. If at least one predicate fails, a map of keys to error is returned:

    {key1=listOf(error1),
     key2=listOf(error2, error3),
     ...
     keyn=listOf(errorn)} => this is what toString() returns

For example:

    val map = mapOf("name" to "Bob")
    
    map.validate {
      validate { Triple("name", { x: String -> x == "Joel" }, "Name must be Joel") }
      }

    => {name=("Name must be Joel")}

## Maven

You must configure your ```pom.xml``` file using JCenter repository

```xml  
<repository>
    <id>central</id>
    <name>bintray</name>
    <url>http://jcenter.bintray.com</url>
</repository>
```

```xml
<dependency>
  <groupId>com.github.joelws</groupId>
  <artifactId>valin</artifactId>
  <version>1.0</version>
</dependency>
```

## Gradle
```repositories {
        jcenter()
    }```  
```compile 'com.github.joelws:valin:1.0'```
