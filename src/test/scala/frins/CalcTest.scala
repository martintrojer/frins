package frins

import org.scalatest.FunSuite

class CalcTest extends FunSuite {

  Prefixes.addPrefix("d", 1.0/10)
  Prefixes.addPrefix("c", 1.0/100)
  Prefixes.addPrefix("k", 1000)
  Prefixes.addStandalonePrefix("kilo", 1000)
  Units.addUnit("m", 1)
  Units.addUnit("s", 1)
  Units.addUnit("pi", 3.14)
  Units.addUnit("inch", Number(127.0/5000, Map("m" -> 1)))
  Units.addFundamentalUnit("m", Number(1).units)
  Units.addFundamental("m")
  Units.addFundamental("s")

  test("resolvePrefixedUnit") {
    expectResult((Number(1.0/100), "m")) { Calc.resolvePrefixedUnit("cm")}
    expectResult((Number(1), "cK")) { Calc.resolvePrefixedUnit("cK")}
    expectResult((Number(1), "centim")) { Calc.resolvePrefixedUnit("centim")}
    expectResult((Number(1), "pi")) { Calc.resolvePrefixedUnit("pi")}
  }

  test("resolveUnitPrefixes") {
    expectResult(Number(1)) { Calc.resolveUnitPrefixes(Map()) }
    expectResult(Number(1, Map("m" -> 1))) { Calc.resolveUnitPrefixes(Map("m" -> 1)) }
    expectResult(Number(1.0/100, Map("m" -> 1))) { Calc.resolveUnitPrefixes(Map("cm" -> 1)) }
    expectResult(Number(1, Map("m" -> 1, "s" -> -1))) {
      Calc.resolveUnitPrefixes(Map("cm" -> 1, "cs" -> -1)) }
    expectResult(Number(10, Map("s" -> -1))) { Calc.resolveUnitPrefixes(Map("ds" -> -1)) }
  }

  test("normalizeUnits") {
    expectResult(Number(1)) { Calc.normalizeUnits(1) }
    val n = Number(1, Map("m" -> 1, "s" -> -1))
    expectResult(n) { Calc.normalizeUnits(n) }
    expectResult(Number(3.14)) { Calc.normalizeUnits(Number(1,Map("pi" -> 1)))}
    expectResult(Number(3.14 * 3.14)) { Calc.normalizeUnits(Number(1,Map("pi" -> 2)))}
    expectResult(Number(1 / 3.14)) { Calc.normalizeUnits(Number(1,Map("pi" -> -1)))}
    expectResult(Number(1 / 3.14 / 3.14)) { Calc.normalizeUnits(Number(1,Map("pi" -> -2)))}
    expectResult(Number(3.14, Map("m" -> 2))) { Calc.normalizeUnits(Number(1, Map("pi" -> 1, "m" -> 2))) }
    expectResult(Number(1000, Map("m" -> 1))) { Calc.normalizeUnits(Number(1, Map("kilo" -> 1, "m" -> 1))) }
  }

  test("resolveAndNormalizeUnits") {
    expectResult(Number(1)) { Calc.resolveAndNormalizeUnits(1) }
    expectResult(Number(3140, Map("m" -> 1))) { Calc.resolveAndNormalizeUnits(Map("pi" -> 1, "km" -> 1)) }
  }

  test("convert") {
    expectResult(Number(1)) { Calc.convert(1, Map()) }
    val us = Map("m" -> 1, "s" -> -1)
    expectResult(Number(3.14, Map("m" -> 0, "s" -> 0))) { Calc.convert(Number(3.14, us), us) }
    expectResult(Number(1.0/100, Map("m" -> 0))) { Calc.convert(Number(1, Map("cm" -> 1)), Map("m" -> 1)) }
    expectResult(Number(100, Map("m" -> 0))) { Calc.convert(Number(1, Map("m" -> 1)), Map("cm" -> 1)) }
    expectResult(Number(1.0/50, Map("m" -> 0))) { Calc.convert(Number(2, Map("cm" -> 1)), Map("m" -> 1)) }
//    expectResult(Number(1.0/254, Map("m" -> 0))) { Calc.convert(Number(1, Map("dm" -> 1)), Map("kinch" -> 1)) }

    intercept[IllegalArgumentException] { Calc.convert(Number(1, Map("m" -> 1)), Map("s" -> 1)) }

    expectResult(Number(1.0 / 2, Map("m" -> 0))) { Calc.convert(Number(2, Map("m" -> 1)), Map("m" -> -1)) }
    expectResult(Number(1.0 / 3, Map("m" -> 0, "s" -> 0))) { Calc.convert(Number(3, Map("m" -> -1, "s" -> 1)), us) }
    intercept[IllegalArgumentException] { Calc.convert(Number(3, Map("m" -> -1, "s" -> -1)), us) }

  }



}
