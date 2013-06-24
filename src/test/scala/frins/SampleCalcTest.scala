//  Copyright (c) Martin Trojer. All rights reserved.
//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

package frins

import org.scalatest.FunSuite

class SampleCalcTest extends FunSuite {

  initDatabases

  def truncate(d: Double) = d - (d % 0.00000001)

  def checkNumber(n1: NumberT, n2: NumberT) = {
    truncate(n1.value) == truncate(n2.value) &&
    n1.cleanUnits.units == n2.cleanUnits.units
  }

  // ---

  test("water") {
    assert(checkNumber(
      Number(552960.0/77),
      N(10, 'feet) * N(12, 'feet) * N(8, 'feet) to 'gallons
    ))

    assert(checkNumber(
      Number(2718417272832.0/45359237),
      N(10, 'feet) * N(12, 'feet) * N(8, 'feet) * 'water to 'pounds
    ))

    assert(checkNumber(
      Number(5669904625.0/10618817472.0),
      N(2, 'tons) / (N(10 * 12) * 'feet * 'feet * 'water) to 'feet
    ))
  }

  Units.addUnit(
    "burnrate",
    N(86481 - 41601, 'thousand, 'dollars) / ('$2001_06_30 - '$2000_12_31))

  test("dates") {
    assert(checkNumber(
      Number(60224381.0/359040),
      N(41601, 'thousand, 'dollars) / 'burnrate to 'days
    ))

    expectResult("Fri Dec 14 16:41:38 GMT 2001") {
      (N('$2001_06_30) + (N(41601, 'thousand, 'dollars) / 'burnrate) toDate).toString

    }
  }

  test("barrels") {
    assert(checkNumber(
      N(368175625.0/129048129),
      N('half, 'ton) to('barrels, 'water)
    ))

    assert(checkNumber(
      N(46037384521821.0/19375000000000.0),
      N(2, 'fathoms, 'water) * 'gravity * 'barrel to N(40, 'watts, 'minutes)
    ))
  }

  test("Calories") {
    assert(checkNumber(
      N(15345794840607.0/11266562500000.0),
      N(2, 'fathoms, 'water) * 'gravity * 'barrel to 'Calories
    ))

    assert(checkNumber(
      N(1163.0/12),
      N(2000, 'Calories, '_day) to 'watts
    ))

    assert(checkNumber(
      N(800000000000.0/43161375789.0),
      N(1100, 'W) * 30 * 'sec / (N(27, 'oz) * 'calorie / 'gram * '_degC) to 'degF
    ))
  }
}
