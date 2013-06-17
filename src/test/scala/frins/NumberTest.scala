package frins

import org.scalatest.FunSuite

class NumberTest extends FunSuite {

  // helpers

  test("cleanUnits") {
    expectResult(Number(1).units) { Number(1).cleanUnits() }
    val u = Map("m" -> 1)
    expectResult(u) { Number(1,u).cleanUnits() }
    val u2 = Map("m" -> 1, "s" -> -1)
    expectResult(u2) { Number(1,u2 + ("foo" -> 0)).cleanUnits() }
  }

  // add/sub

  test("add w/o units") {
    expectResult(Number(2)) { Number(1) + Number(1) + Number(0) + 0 }
    expectResult(Number(1)) { Number(1) + Number(1) + Number(0) + 0 - Number(1)}
  }

  test("add with units") {
    val us = Map("m" -> 1)
    expectResult(Number(2.0, us)) { Number(1.0, us) + Number(1.0, us) }
    expectResult(Number(-1, us)) { Number(1.0, us) - Number(2.0, us) }
  }

  test("add non-matching units") {
    intercept[IllegalArgumentException] { Number(1.0, Map("m" -> 1)) + Number(1.0, Map("s" -> -1)) }
    intercept[IllegalArgumentException] { Number(1.0, Map("s" -> -1)) - 3.14 }
  }

  // mul/div

  test("mul w/o units") {
    expectResult(Number(2.25)) { Number(1.5) * Number(1.5) * 1.0 }
    expectResult(Number(0.5)) { Number(1) / 2 / Number(1) }
  }

  test("mul with units") {
    val us1 = Map("m" -> 1)
    val us2 = Map("s" -> -1)
    expectResult(Number(2.25, us1 ++ us2)) { Number(1.5, us1) * Number(1.5, us2) }
    expectResult(Number(1, us1 + ("s" -> 1))) { Number(1.5, us1) / Number(1.5, us2) }
    expectResult(Number(1, us1 + ("s" -> 0))) { Number(1.5, us1 ++ us2) / Number(1.5, us2) }
  }

  // comp
  test("comp w/o units") {
    expectResult(false) { Number(1) == Number(1.1) }
    expectResult(false) { Number(1) >= Number(1.1) }
    expectResult(true) { Number(1) < Number(1.1) }
  }

  test("comp with units") {
    val us = Map("m" -> 1)
    expectResult(false) { Number(1, us) == Number(1.1, us) }
    expectResult(false) { Number(1, us) >= Number(1.1, us) }
    expectResult(true) { Number(1, us) < Number(1.1, us) }
  }

  test("comp non-matching units") {
    val us = Map("m" -> 1)
    intercept[IllegalArgumentException] { 1 > Number (0, us) }
    intercept[IllegalArgumentException] { Number (0, us) <= 42 }
  }
}
