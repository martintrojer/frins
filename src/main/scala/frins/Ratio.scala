package frins

class Ratio(n: BigInt, d: BigInt) {

  require(denom != 0)
  private val g = gcd(n.abs, d.abs)
  val numer: BigInt = n / g
  val denom: BigInt = d / g

  private def gcd(a: BigInt, b: BigInt): BigInt =
    if (b == 0) a else gcd(b, a % b)

  val bigdecValue: BigDecimal = BigDecimal(numer) / BigDecimal(denom)

  // ---

  def +(that: Ratio): Ratio =
    new Ratio(
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )
  def -(that: Ratio): Ratio =
    new Ratio(
      numer * that.denom - that.numer * denom,
      denom * that.denom
    )
  def *(that: Ratio): Ratio =
    new Ratio(numer * that.numer, denom * that.denom)
  def /(that: Ratio): Ratio =
    new Ratio(numer * that.denom, denom * that.numer)

  // ---

  override def toString = numer + (if (denom != 1) ("/"+ denom) else "")

  override def equals(that: Any) = that match {
    case that: Ratio  => numer == that.numer && denom == that.denom
    case _            => false
  }

  override val hashCode = 41 * numer.hashCode() + denom.hashCode()
}
object Ratio {
  def apply(i: Int): Ratio = new Ratio(i,1)
  def apply(n: Int, d: Int): Ratio = new Ratio(n ,d)
  def apply(d: Double): Ratio = {

    // naive, slow and inaccurate

    var up = 1
    var low = 1
    var df = 1.0
    val threshold = 0.0001
    while((d % threshold) != (df % threshold)) {
      if (df < d) up += 1
      else {
        low += 1
        up = d.toInt * low
      }
      df = up.toDouble / low
    }
    new Ratio(up, low)
  }

  implicit object RatioIsFractionalAndOrdered extends RatioIsFractional with RatioOrdering

  implicit def intToRatio(i: Int) = apply(i)
  implicit def doubleToRatio(d: Double) = apply(d)
}

trait RatioIsFractional extends Fractional[Ratio] {
  def plus(x: Ratio, y: Ratio): Ratio = x + y
  def minus(x: Ratio, y: Ratio): Ratio = x - y
  def times(x: Ratio, y: Ratio): Ratio = x * y
  def div(x: Ratio, y: Ratio): Ratio = x / y
  def negate(x: Ratio): Ratio = -x
  def fromInt(x: Int): Ratio = Ratio(x)
  def toInt(x: Ratio): Int = x.bigdecValue.toInt
  def toLong(x: Ratio): Long = x.bigdecValue.toLong
  def toFloat(x: Ratio): Float = x.bigdecValue.toFloat
  def toDouble(x: Ratio): Double = x.bigdecValue.toDouble
}

trait RatioOrdering extends Ordering[Ratio] {
  def compare(a: Ratio, b: Ratio) = a.bigdecValue compare b.bigdecValue
}