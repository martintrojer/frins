package frins

import org.scalatest.FunSuite

class PrefixesTest extends FunSuite {

  val units = Units()
  val prefixes = Prefixes()

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

  test("isPrefix") {
    expectResult(true) { prefixes.isPrefix("d") }
    expectResult(true) { prefixes.isPrefix("kilo") }
    expectResult(false) { prefixes.isPrefix("kalle") }
  }

  test("getPrefix") {
    expectResult(Some(Number(1.0/10))) { prefixes.getPrefix("d") }
    expectResult(Some(Number(1000))) { prefixes.getPrefix("kilo") }
    expectResult(None) { prefixes.getPrefix("kalle") }
  }

  test("resolvePrefixedUnit") {
    expectResult((Number(1.0/100), "m")) { prefixes.resolvePrefixedUnit("cm", units)}
    expectResult((Number(1), "cK")) { prefixes.resolvePrefixedUnit("cK", units)}
    expectResult((Number(1), "centim")) { prefixes.resolvePrefixedUnit("centim", units)}
    expectResult((Number(1), "pi")) { prefixes.resolvePrefixedUnit("pi", units)}
  }

  test("resolveUnitPrefixes") {
    expectResult(Number(1)) { prefixes.resolveUnitPrefixes(Map(), units) }
    expectResult(Number(1, Map("m" -> 1))) { prefixes.resolveUnitPrefixes(Map("m" -> 1), units) }
    expectResult(Number(1.0/100, Map("m" -> 1))) { prefixes.resolveUnitPrefixes(Map("cm" -> 1), units) }
    expectResult(Number(1, Map("m" -> 1, "s" -> -1))) {
      prefixes.resolveUnitPrefixes(Map("cm" -> 1, "cs" -> -1), units) }
    expectResult(Number(10, Map("s" -> -1))) { prefixes.resolveUnitPrefixes(Map("ds" -> -1), units) }
  }


}