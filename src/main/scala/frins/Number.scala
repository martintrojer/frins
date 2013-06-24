//  Copyright (c) Martin Trojer. All rights reserved.
//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

package frins

import java.text.SimpleDateFormat
import java.util.Date

/** Represents a value and a unit
  * Supports all operators you would expect of a scalar number
  *
  * @param value Numerical value of the Number
  * @param units All units of the Number
  * @param num
  * @tparam T
  */
class Number[T](val value:T, val units: UnitT)(implicit num: Fractional[T]) {

  // ----

  /** Merges 2 sets of units */
  def mergeUnits (f: (Int, Int) => Int) (us: UnitT) =
    units ++ us.map { case (k, v) => k -> f(units.getOrElse(k, 0), v) }

  val addUnits = mergeUnits(_+_) _
  val subUnits = mergeUnits(_-_) _

  /** Make sure provided number have the same units as this one */
  def enforceUnits(n: Number[T]) =
    if (cleanUnits.units != n.cleanUnits.units)
      throw new IllegalArgumentException("units doesn't match")

  // ----

  /** Return a new Number with any 0 exponent units removed */
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

  import Number.buildNumber

  /** Convert this Number to the units contained in provided NumberT */
  def to(that: NumberT): NumberT =
    Calc.convert(this.asInstanceOf[NumberT], that.units) / that.value
  def to(us: Symbol*): NumberT = to(buildNumber(1.0, us.map{_.name}))

  /** Convert this Number to a date (if the units if of signature s^1)^*/
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

/** Convenient methods for building Numbers
  * {{{
  *   Number()        ==> Number(1,Map())
  *   Number(v)       ==> Number(v, Map())
  *   Number(Map(..)) ==> Number(1, Map(..))
  *   Number('m, '_s) ==> Number(fact, Map("m"->1,"s"->-1))
  *   Number(v, 'm, '_s) ==> Number(v * fact, Map("m"->1,"s"->-1))
  * }}}
  *
  * Units can be expressed as Symbols (or Strings) and be implicitly converted to NumberT.
  * If the unit has a leading underscore (_) is will be treated as a inversion.
  * If the units has a leading dollar ($) it will be treated as a date with the following format yyyy_MM_DD
  *   $now yields the current date (and time)
  *
   */
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

/** Same as Number but with even less typing (yaj!) */
object N {
  def apply(): NumberT = apply(0)
  def apply(v: Double): NumberT = new Number(v, Map())
  def apply(units: UnitT): NumberT = apply(0, units)
  def apply(v: Double, units: UnitT): NumberT = new Number(v, units)

  import Number.buildNumber

  def apply(us: Symbol*): NumberT = buildNumber(1.0, us.map{_.name})
  def apply(v: Double, us: Symbol*): NumberT = buildNumber(v, us.map{_.name})
}
