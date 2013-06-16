package frins

// TODO - make generic of all number types
class Number(val value:Double, val units: UnitT) {
  override def toString() = value.toString + " " + units.foldLeft("") {(acc, kv) => acc + kv._1 + "^" + kv._2 + " "}

  def cleanUnits(um: UnitT) = um filter {_._2 != 0}

  def addUnits(um1: UnitT, um2: UnitT) =
    um1 ++ um2.map{ case (k,v) => k -> (v + um1.getOrElse(k,0)) }

  def +(that: Number) =
    if (units == that.units)
      new Number(value + that.value, units)
    else
      throw new IllegalArgumentException("units doesn't match")

  def *(that: Number) =
    new Number(value * that.value, addUnits(units, that.units))

  override def equals(that: Any) = that match {
    case that: Number => value == that.value && units == that.units
    case _            => false
  }

  // TODO; hashCode
}

object Number{
  def apply(): Number = apply(0.0)
  // TODO; instead of Map, do we want (String,Int)* here?
  def apply(v: Double): Number = apply(v, Map())
  def apply(units: UnitT): Number = apply(0.0, units)
  def apply(v: Double, units: UnitT): Number = new Number(v, units)

  // TODO; where does this go?
  implicit def doubleToNumber(d: Double) = apply(d)

}
