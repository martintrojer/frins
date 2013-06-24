# frins

Frins is a practical unit-of-measure calculator DSL for Scala.

Key features;

* Tracks units of measure through all calculations allowing you to mix units of measure transparently
* Comes with a huge database of units and conversion factors
* Inspired by the [Frink project](http://futureboy.us/frinkdocs/) and [Frinj](https://github.com/martintrojer/frinj)
* Tries to combine Frink's fluent calculation style with idiomatic Scala* Supports infix calculation style

## Usage

Add the following lines to your build.sbt


```
resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "io.github.martintrojer" % "frins-scala_2.10" % "0.1-SNAPSHOT"
```

```scala
$ sbt console
scala> import frins._
scala> initDatabases
scala> N(1, 'teaspoon, 'water) * ('c ** 2) to ('gallons, 'gasoline)
res0: frins.NumberT = 3164209.8628361006  [dimensionless]
// That's the energy contained in a teaspoon of water according to E=mc^2 converted to equivalent energy
// from burning a gallon of gasoline (it's alot).
```

Start calculating!

## Examples

* See [example calculations](https://github.com/martintrojer/frins/blob/master/src/main/scala/frins/ExampleCalculations.scala) for ideas (and some laughs)...

* [Video of a frinj (this projects clojure sibling) talk](http://skillsmatter.com/podcast/home/frinj-having-fun-with-units-3861)

Also, check the Frinj [wiki](https://github.com/martintrojer/frinj/wiki) for related information.

## License

Copyright (C) 2013 Martin Trojer

Distributed under the Eclipse Public License.
