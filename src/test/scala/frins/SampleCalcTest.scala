package frins

import org.scalatest.FunSuite

class SampleCalcTest extends FunSuite {

  initDatabases()

  def truncate(d: Double) = d - (d % 0.00000001)

  def checkNumber(n1: NumberT, n2: NumberT) = {
    truncate(n1.value) == truncate(n2.value) &&
    n1.cleanUnits.units == n2.cleanUnits.units
  }

  test("samples") {
    assert(checkNumber(
      Number(552960.0/77),
      N(10, 'feet) * N(12, 'feet) * N(8, 'feet) to('gallons)))
  }
}
