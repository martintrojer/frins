//  Copyright (c) Martin Trojer. All rights reserved.
//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

package frins

/** Namespace for functions operating on Units and Numbers
  * Mainly used to resolving unit prefixes and converting units.
  */
object Calc {

  /** Finds the longest prefix in a unit name and replaces it with with factor
    * A prefix only matches if the remaining string is a vaild unit.
    * If no match is found return the unit
    * {{{
    *   "kilom" ==> (1000, "m")
    *   "foom" ==> (1, "foom")
    * }}}
    *
    * @param name Name of the Unit
    * @return Tuple of factor (scalar factor of the prefix) and the remaining unit with prefix stripped off
    */
  def resolvePrefixedUnit(name: String) = Units.getUnit(name) match {
    case Some(u)  => (Number(1), name)
    case None     => {
      val pfx = Prefixes.allNames
        .toList
        .filter { name.startsWith(_) }
        .sortBy { _.size }
        .reverse
        .filter { n => Units.isUnit(name.substring(n.size)) }
      if (pfx != List())
        (Prefixes.getPrefix(pfx.head).get, name.substring(pfx.head.size))
      else
        (Number(1.0), name)
    }
  }

  /** Replaces all units with prefix + new unit
    * {{{
    *   Number(1, Map("pfxU1"->1, "pfxU2"->-1)) ==> Number(pfxFact1 * pfxFact2, Map("U2"->1, "U2"->-1))
    * }}}
    *
    * @param units UnitMap to me resolved
    * @return Number containing the factor and new unit map
    */
  def resolveUnitPrefixes(units: UnitT) =
    units.foldLeft(Number(1)) { (acc, kv) =>
      val (prefix, exp) = kv
      val (factNum, newU) = resolvePrefixedUnit(prefix)
      val fact = factNum ** exp.abs
      acc * Number(1, Map(newU -> exp)) * (if (exp > 0) fact else 1 / fact)
    }

  /** Replaces units with already defined ones, and remove zero units
    * {{{
    *   Number('inch) ==> Number(0.0254, Map("m" -> 1))
    * }}}
    *
    * @param n Number to be normalized
    * @return Normalized number
    */
  def normalizeUnits(n: NumberT) =
    n.units.foldLeft(n) { (acc, kv) =>
      val (name, exp) = kv
      Units.getUnit(name).orElse(Prefixes.getStandAlonePrefix(name)) match {
        case Some(num: NumberT)   =>
          if (Units.isFundamental(name)) acc
          else {
            val fact = num ** exp.abs
            (if (exp > 0) acc * fact else acc / fact) / Number(1, Map(name -> exp))
          }
        case None        => acc
      }
    }.cleanUnits

  /** Resolve all units with prefixes, and normalize the result */
  def resolveAndNormalizeUnits(us: UnitT) =
    normalizeUnits(resolveUnitPrefixes(us))
  def resolveAndNormalizeUnits(n: NumberT) =
    normalizeUnits(resolveUnitPrefixes(n.units))

  /** Converts a Number to a given unit, will resolve and normalize. Will reverse if units 'mirrored'
    * {{{
    *   Number('miles, '_hour) ('km, '_hour) ==>> N(1.6093439999999999)
    *   Number('miles, '_hour) ('_km, 'hour) ==>> N(0.6213711922373341)
    * }}}
    * @param n Number to convert from
    * @param us Units to convert to
    * @return New converted Number (will be dimensionless is conversion was successful)
    */
  def convert(n: NumberT, us: UnitT) = {
    val normN = resolveAndNormalizeUnits(n)
    val normU = resolveAndNormalizeUnits(us)
    val newNum = normN * n.value
    if (newNum.units == normU.units)
      newNum / normU
    else if ((1 / newNum).units == normU.units)
      1 / newNum / normU
    else throw new IllegalArgumentException("cannot convert to a different unit")
  }
}
