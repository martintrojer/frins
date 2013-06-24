package frins

import java.text.SimpleDateFormat
import java.util.Date

class Number[T](val value:T, val units: UnitT)(implicit num: Fractional[T]) {

  // ----
  // Helper functions, where do these actually go?

  def mergeUnits (f: (Int, Int) => Int) (us: UnitT) =
    units ++ us.map { case (k, v) => k -> f(units.getOrElse(k, 0), v) }

  val addUnits = mergeUnits(_+_) _
  val subUnits = mergeUnits(_-_) _

  def enforceUnits(n: Number[T]) =
    if (cleanUnits.units != n.cleanUnits.units)
      throw new IllegalArgumentException("units doesn't match")

  // ----

  def cleanUnits() = new Number(value, units filter { _._2 != 0 })

  def +(that: Number[T]) = {
    enforceUnits(that)
    new Number(num.plus(value, that.value), units)
  }

  def -(that: Number[T]) = {
    enforceUnits(that)
    new Number(num.minus(value, that.value), units)
  }

  def *(that: Number[T]) =
    new Number(num.times(value, that.value), addUnits(that.units))

  def /(that: Number[T]) =
    new Number(num.div(value, that.value), subUnits(that.units))

  def **(exp: Int) =
    (0 until exp.abs).foldLeft(this / this) { (acc, _) => if (exp < 0) acc / this else acc * this }

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

  // ----

  // goodbye nice Number[T], hello hardcoded NumberT

  import Number.buildNumber

  def to(that: NumberT): NumberT =
    Calc.convert(this.asInstanceOf[NumberT], that.units) / that.value
  def to(us: Symbol*): NumberT = to(buildNumber(1.0, us.map{_.name}))

  def toDate =
    if (units == Map("s" -> 1)) new Date(value.asInstanceOf[Double].toLong * 1000)
    else throw new IllegalArgumentException("cannot convert unit to date")

  // ----

  // override def toString() = value.toString + " : " + units.toString

  override def toString() = {
    val n = cleanUnits
    n.value.toString + " " +
      n.units.map { case (k,v) => k + (if (v != 1) "^" + v else "") }.mkString(" ") +
      " [" + Units.getFundamentalUnit(n.units).getOrElse("") +  "]"
  }

  override def equals(that: Any) = that match {
    case that: Number[T] => value == that.value && units == that.units
    case _            => false
  }

  override val hashCode = 41 * units.hashCode() + value.hashCode()
}

object Number {
  def apply(): NumberT = apply(0)
  def apply(v: Double): NumberT = new Number(v, Map())
  def apply(units: UnitT): NumberT = apply(0, units)
  def apply(v: Double, units: UnitT): NumberT = new Number(v, units)

  def buildNumber(v: Double, us: Seq[String]): NumberT =
    us.foldLeft(new Number(v, Map()))
    { (acc, u) =>
      if (u.startsWith("_")) acc / Calc.resolveAndNormalizeUnits(Map(u.tail -> 1))
      else if (u.startsWith("$")) {
        val s: String = u.tail
        val df = new SimpleDateFormat("yyyy_MM_dd")
        val data = if (s.toLowerCase == "now") (new Date) else (df.parse(s))
        acc * new Number(data.getTime / 1000.0, Map("s" -> 1))
      }
      else acc * Calc.resolveAndNormalizeUnits(Map(u -> 1)) }

  def apply(us: Symbol*): NumberT = buildNumber(1.0, us.map{_.name})
  def apply(v: Double, us: Symbol*): NumberT = buildNumber(v, us.map{_.name})
}

object N {

  import Number.buildNumber

  def apply(us: Symbol*): NumberT = buildNumber(1.0, us.map{_.name})
  def apply(v: Double, us: Symbol*): NumberT = buildNumber(v, us.map{_.name})
}