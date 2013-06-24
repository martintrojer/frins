//  Copyright (c) Martin Trojer. All rights reserved.
//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

package frins

import org.scalatest.FunSuite

class NumberTest extends FunSuite {

  // helpers

  test("cleanUnits") {
    expectResult(Number(1)) { Number(1).cleanUnits() }
    val n = Number(1, Map("m" -> 1))
    expectResult(n) { n.cleanUnits() }
    val u2 = Map("m" -> 1, "s" -> -1)
    expectResult(Number(1, u2)) { Number(1, u2 + ("foo" -> 0)).cleanUnits() }
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

  // pow

  test("pow") {
    expectResult(Number(1)) { Number(1) ** 0 }
    expectResult(Number(1)) { Number(2) ** 0 }
    val n = Number(2, Map("m" -> 2, "s" -> -1))
    expectResult(Number(1, Map("m" -> 0, "s" -> 0))) { n ** 0 }
    expectResult(n) { n ** 1 }
    expectResult(n * n) { n ** 2 }
    expectResult(Number(4, Map("m" -> 4, "s" -> -2))) { n ** 2 }
    expectResult(1 / n) { n ** -1 }
    expectResult(1 / n / n) { n ** -2 }
    expectResult(Number(0.25, Map("m" -> -4, "s" -> 2))) { n ** -2 }
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
