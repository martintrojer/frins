package frins

import org.scalatest.FunSuite

class CalcTest extends FunSuite {

  val units = UnitDb()
  val prefixes = PrefixDb()
  val calc = new Calc(units, prefixes)

  prefixes.addPrefix("d", 1.0/10)
  prefixes.addPrefix("c", 1.0/100)
  prefixes.addPrefix("k", 1000)
  prefixes.addStandalonePrefix("kilo", 1000)
  units.addUnit("m", 1)
  units.addUnit("s", 1)
  units.addUnit("pi", 3.14)
  units.addUnit("inch", Number(127.0/5000, Map("m" -> 1)))
  units.addFundamentalUnit("m", Number(1).units)
  units.addFundamental("m")
  units.addFundamental("s")

  test("resolvePrefixedUnit") {
    expectResult((Number(1.0/100), "m")) { calc.resolvePrefixedUnit("cm")}
    expectResult((Number(1), "cK")) { calc.resolvePrefixedUnit("cK")}
    expectResult((Number(1), "centim")) { calc.resolvePrefixedUnit("centim")}
    expectResult((Number(1), "pi")) { calc.resolvePrefixedUnit("pi")}
  }

  test("resolveUnitPrefixes") {
    expectResult(Number(1)) { calc.resolveUnitPrefixes(Map()) }
    expectResult(Number(1, Map("m" -> 1))) { calc.resolveUnitPrefixes(Map("m" -> 1)) }
    expectResult(Number(1.0/100, Map("m" -> 1))) { calc.resolveUnitPrefixes(Map("cm" -> 1)) }
    expectResult(Number(1, Map("m" -> 1, "s" -> -1))) {
      calc.resolveUnitPrefixes(Map("cm" -> 1, "cs" -> -1)) }
    expectResult(Number(10, Map("s" -> -1))) { calc.resolveUnitPrefixes(Map("ds" -> -1)) }
  }

  test("normalizeUnits") {
    expectResult(Number(1)) { calc.normalizeUnits(1) }
    val n = Number(1, Map("m" -> 1, "s" -> -1))
    expectResult(n) { calc.normalizeUnits(n) }
    expectResult(Number(3.14)) { calc.normalizeUnits(Number(1,Map("pi" -> 1)))}
    expectResult(Number(3.14 * 3.14)) { calc.normalizeUnits(Number(1,Map("pi" -> 2)))}
    expectResult(Number(1 / 3.14)) { calc.normalizeUnits(Number(1,Map("pi" -> -1)))}
    expectResult(Number(1 / 3.14 / 3.14)) { calc.normalizeUnits(Number(1,Map("pi" -> -2)))}
    expectResult(Number(3.14, Map("m" -> 2))) { calc.normalizeUnits(Number(1, Map("pi" -> 1, "m" -> 2))) }
    expectResult(Number(1000, Map("m" -> 1))) { calc.normalizeUnits(Number(1, Map("kilo" -> 1, "m" -> 1))) }
  }

  test("resolveAndNormalizeUnits") {
    expectResult(Number(1)) { calc.resolveAndNormalizeUnits(1) }
    expectResult(Number(3140, Map("m" -> 1))) { calc.resolveAndNormalizeUnits(Map("pi" -> 1, "km" -> 1)) }
  }

  test("convert") {
    expectResult(Number(1)) { calc.convert(1, Map()) }
    val us = Map("m" -> 1, "s" -> -1)
    expectResult(Number(3.14, Map("m" -> 0, "s" -> 0))) { calc.convert(Number(3.14, us), us) }
    expectResult(Number(1.0/100, Map("m" -> 0))) { calc.convert(Number(1, Map("cm" -> 1)), Map("m" -> 1)) }
    expectResult(Number(100, Map("m" -> 0))) { calc.convert(Number(1, Map("m" -> 1)), Map("cm" -> 1)) }
    expectResult(Number(1.0/50, Map("m" -> 0))) { calc.convert(Number(2, Map("cm" -> 1)), Map("m" -> 1)) }
//    expectResult(Number(1.0/254, Map("m" -> 0))) { calc.convert(Number(1, Map("dm" -> 1)), Map("kinch" -> 1)) }

    intercept[IllegalArgumentException] { calc.convert(Number(1, Map("m" -> 1)), Map("s" -> 1)) }

    expectResult(Number(1.0 / 2, Map("m" -> 0))) { calc.convert(Number(2, Map("m" -> 1)), Map("m" -> -1)) }
    expectResult(Number(1.0 / 3, Map("m" -> 0, "s" -> 0))) { calc.convert(Number(3, Map("m" -> -1, "s" -> 1)), us) }
    intercept[IllegalArgumentException] { calc.convert(Number(3, Map("m" -> -1, "s" -> -1)), us) }

  }



}
