package frins

import org.scalatest.FunSuite

class SampleCalcTest extends FunSuite {

  initDatabases()

  def truncate(d: Double) = d - (d % 0.00000001)

  def checkNumber(n1: NumberT, n2: NumberT) = {
    truncate(n1.value) == truncate(n2.value) &&
    n1.cleanUnits.units == n2.cleanUnits.units
  }

  // ---

  test("water") {
    assert(checkNumber(
      Number(552960.0/77),
      N(10, 'feet) * N(12, 'feet) * N(8, 'feet) to ('gallons)
    ))

    assert(checkNumber(
      Number(2718417272832.0/45359237),
      N(10, 'feet) * N(12, 'feet) * N(8, 'feet) * 'water to ('pounds)
    ))

    assert(checkNumber(
      Number(5669904625.0/10618817472.0),
      N(2, 'tons) / (N(10 * 12) * 'feet * 'feet * 'water) to ('feet)
    ))
  }

  Units.addUnit(
    "burnrate",
    N(86481 - 41601, 'thousand, 'dollars) / ('$2001_06_30 - '$2000_12_31))

  test("dates") {
    expectResult("Fri Dec 14 16:41:38 GMT 2001") {
      (N('$2001_06_30) + (N(41601, 'thousand, 'dollars) / 'burnrate) toDate).toString

    }
  }


}
