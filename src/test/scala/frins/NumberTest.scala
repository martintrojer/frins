package frins

import org.scalatest.FunSuite

class NumberTest extends FunSuite {

  test("add no units") {
    expectResult(Number(2.0)) {
      Number(1.0) + Number(1.0)
    }
  }

  test("add some units") {
    val us = Map("m" -> 1)
    expectResult(Number(2.0, us)) {
      Number(1.0, us) + Number(1.0, us)
    }
  }

  test("add non-matching units") {
    intercept[IllegalArgumentException] {
      Number(1.0, Map("m" -> 1)) + Number(1.0, Map("s" -> -1))
    }
  }

  test("add no units implicit") {
    expectResult(Number(2.0)) {
      Number(1.0) + 1.0
    }
  }

  // ---

  test("mul no units") {
    expectResult(Number(2.25)) {
      Number(1.5) * Number(1.5) * 1.0
    }
  }

  test("mul some units") {
    val us1 = Map("m" -> 1)
    val us2 = Map("s" -> -1)
    expectResult(Number(2.25, us1 ++ us2)) {
      Number(1.5, us1) * Number(1.5, us2)
    }
  }
}
