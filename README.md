# frins

Frins is a practical unit-of-measure calculator DSL for Scala.

Key features;

* Tracks units of measure through all calculations allowing you to mix units of measure transparently
* Comes with a huge database of units and conversion factors
* Inspired by the [Frink project](http://futureboy.us/frinkdocs/) and [Frinj](https://github.com/martintrojer/frinj)
* Tries to combine Frink's fluent calculation style with idiomatic Scala

## Artifacts

Add the following lines to your build.sbt

```
resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "io.github.martintrojer" % "frins_2.10" % "0.1-SNAPSHOT"
```

## Usage

Turn your sbt console into an (even more) powerful calculator;

```scala
$ sbt console
scala> import frins._
scala> initDatabases
```

You are now ready to go! Frins keeps it's values in instances of the [Number](https://github.com/martintrojer/frins/blob/master/src/main/scala/frins/Number.scala) class, which contains a Double value and a Map of Units. Once you have a Number object you can use it in calculations as you would normally use Doubles.

There are convenience Frins Number builders using the form `N(value, 'unit, 'unit, ...)` (value can be omitted) and implicits conversions from other number types and scala symbols (denoting the unit) to Frins Numbers.

```scala
scala> N('teaspoon, 'water) * ('c ** 2)
res0: frins.Number[Double] = 4.429893807970541E14 m^2 kg s^-2 [energy]
```

That's the energy contained in one teaspoon of water (using `E=mc^2`). Wow, big number, how much is that "in real money"?

```scala
scala> res0 to ('gallons, 'gasoline)
res1: frins.NumberT = 3164209.8628361006  [dimensionless]
```

There you have it, the energy in one teaspoon of water (according to Einstein) is equivalent to burning 3 million (!) gallons of gasoline.

For more examples and explanations of the Number builders (and more!) see the example calculations below.

## Examples

* See [example calculations](https://github.com/martintrojer/frins/blob/master/src/main/scala/frins/ExampleCalculations.scala) for ideas (and some laughs)...

* [Video of a Frinj talk](http://skillsmatter.com/podcast/home/frinj-having-fun-with-units-3861) (Frinj is Frins' sibling project)

Also, check the Frinj [wiki](https://github.com/martintrojer/frinj/wiki) for related information.

## License

Copyright (C) 2013 Martin Trojer

Distributed under the Eclipse Public License.
