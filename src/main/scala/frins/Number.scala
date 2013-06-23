package frins

class Number[T](val value:T, val units: UnitT)(implicit num: Fractional[T]) {

  // ----
  // Helper functions, where do these actually go?

  def mergeUnits (f: (Int, Int) => Int) (us: UnitT) =
    units ++ us.map { case (k, v) => k -> f(units.getOrElse(k, 0), v) }

  val addUnits = mergeUnits(_+_) _
  val subUnits = mergeUnits(_-_) _

  def enforceUnits(n: Number[T]) =
    if (units != n.units)
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

  // not Fractional[T] anymore...

  import Number.buildNumber

  def to(that: NumberT): NumberT =
    Calc.convert(this.asInstanceOf[NumberT], that.units) / that.value
  def to(us: Symbol*): NumberT = to(buildNumber(1, us.map{_.name}))

  // ----

  // override def toString() = value.toString + " : " + units.toString

  override def toString() = {
    val n = cleanUnits
    n.value.toString + " " +
      n.units.map { case (k,v) => k + "^" + v }.mkString(" ") +
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
  def apply(d: Double): NumberT = new Number(d, Map())
  def apply(v: Ratio): NumberT = new Number(v, Map())
  def apply(units: UnitT): NumberT = apply(0, units)
  def apply(v: Ratio, units: UnitT): NumberT = new Number(v, units)

  def buildNumber(v: Ratio, us: Seq[String]): NumberT =
    us.foldLeft(new Number(v, Map()))
    { (acc, u) =>
      if (u.startsWith("_")) acc / Calc.resolveAndNormalizeUnits(Map(u.tail -> 1))
      else acc * Calc.resolveAndNormalizeUnits(Map(u -> 1)) }

  def apply(us: Symbol*): NumberT = buildNumber(1, us.map{_.name})
  def apply(v: Ratio, us: Symbol*): NumberT = buildNumber(v, us.map{_.name})
}

object N {

  import Number.buildNumber

  def apply(us: Symbol*): NumberT = buildNumber(1, us.map{_.name})
  def apply(v: Ratio, us: Symbol*): NumberT = buildNumber(v, us.map{_.name})
}