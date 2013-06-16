package frins

class Number[T](val value:T, val units: UnitT)(implicit num: Fractional[T]) {
  override def toString() = value.toString + " " + units.foldLeft("")
                              {(acc, kv) => acc + kv._1 + "^" + kv._2 + " "}

  def cleanUnits(um: UnitT) = um filter {_._2 != 0}

  def addUnits(um1: UnitT, um2: UnitT) =
    um1 ++ um2.map{ case (k,v) => k -> (v + um1.getOrElse(k,0)) }

  def +(that: Number[T]) =
    if (units == that.units)
      new Number(num.plus(value, that.value), units)
    else
      throw new IllegalArgumentException("units doesn't match")

  def *(that: Number[T]) =
    new Number(num.times(value, that.value), addUnits(units, that.units))

  override def equals(that: Any) = that match {
    case that: Number[T] => value == that.value && units == that.units
    case _            => false
  }

  // TODO; hashCode
}

object Number{
  def apply(): Number[BigDecimal] = apply(0)
  // TODO; instead of Map, do we want (String,Int)* here?
  def apply(v: BigDecimal): Number[BigDecimal] = apply(v, Map())
  def apply(units: UnitT): Number[BigDecimal] = apply(0, units)
  def apply(v: BigDecimal, units: UnitT): Number[BigDecimal] = new Number(v, units)

  // TODO; where does this go?
  implicit def bigdecToNumber(b: BigDecimal) = apply(b)
  implicit def doubleToNumber(d: Double) = apply(d)

}
