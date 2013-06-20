package frins

import org.scalatest.FunSuite

class PrefixesTest extends FunSuite {

  val units = Units()
  val prefixes = Prefixes()

  prefixes.addPrefix("d", Number(1.0/10))
  prefixes.addPrefix("c", Number(1.0/100))
  prefixes.addPrefix("k", Number(1000))
  prefixes.addStandalonePrefix("kilo", Number(1000))
  units.addUnit("m", Number(1))
  units.addUnit("s", Number(1))
  units.addUnit("pi", Number(3.14))
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
    expectResult((Number(3.14), "")) { prefixes.resolvePrefixedUnit("pi", units)}
  }


}