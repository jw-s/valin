# Valin
Valin is a validation library for Kotlin, used to validate keys within any type of map.

# Usage

Contains the map extension function `Validate.kt/Map<K, V>.validate`. It uses the following
syntax:

    map.validate {
    validate (key, errorString) Function1
    validate (key, errorString) { it == 5 }
    validate (key, errorString) Function1
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
      validate ("name", "Name must be Joel") { x: String -> x == "Joel" }
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
  <groupId>com.joelws</groupId>
  <artifactId>valin</artifactId>
  <version>1.1</version>
  <type>pom</type>
</dependency>
```

## Gradle
```repositories {
        jcenter()
    }```  
```compile 'com.joelws:valin:1.1'```
