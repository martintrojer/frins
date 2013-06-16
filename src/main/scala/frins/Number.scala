package frins

class Number[T](val value:T, val units: UnitT)(implicit num: Fractional[T]) {

  // ----

  override def toString() = value.toString + " " + units.foldLeft("")
                              {(acc, kv) => acc + kv._1 + "^" + kv._2 + " "}

  override def equals(that: Any) = that match {
    case that: Number[T] => value == that.value && units == that.units
    case _            => false
  }
  // TODO; hashCode

  // ----

  def cleanUnits(um: UnitT) = um filter {_._2 != 0}

  def addUnits(um1: UnitT, um2: UnitT) =
    um1 ++ um2.map{ case (k,v) => k -> (v + um1.getOrElse(k,0)) }
  // TODO; clean up
  def subUnits(um1: UnitT, um2: UnitT) =
    um1 ++ um2.map{ case (k,v) => k -> (v - um1.getOrElse(k,0)) }

  def enforceUnits(n: Number[T]) =
    if (units != n.units)
      throw new IllegalArgumentException("units doesn't match")

  // ----

  def +(that: Number[T]) = {
    enforceUnits(that)
    new Number(num.plus(value, that.value), units)
  }

  def -(that: Number[T]) = {
    enforceUnits(that)
    new Number(num.minus(value, that.value), units)
  }

  def *(that: Number[T]) =
    new Number(num.times(value, that.value), addUnits(units, that.units))

  def /(that: Number[T]) =
    new Number(num.div(value, that.value), subUnits(units, that.units))

  def ==(that: Number[T]) = {
    enforceUnits(that)
    num.equiv(value, that.value)
  }

  def >=(that: Number[T]) = {
    enforceUnits(that)
    num.gteq(value, that.value)
  }

  def >(that: Number[T]) = {
    enforceUnits(that)
    num.gt(value, that.value)
  }

  def <(that: Number[T]) = {
    enforceUnits(that)
    num.lt(value, that.value)
  }

  def <=(that: Number[T]) = {
    enforceUnits(that)
    num.lteq(value, that.value)
  }

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
