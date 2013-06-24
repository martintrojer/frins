//  Copyright (c) Martin Trojer. All rights reserved.
//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

package object frins {

  type UnitT = Map[String, Int]

  type NumberT = Number[Double]

  type UnitMapT = Map[String, NumberT]
  type RevUnitMapT = Map[UnitT, String]

  type PrefixT = Map[String, NumberT]

  // ---

  /** Initialize the Units and Prefix databases */
  def initDatabases() = {

    val in = getClass.getClassLoader.getResourceAsStream("units.edn")
    val reader = new java.io.BufferedReader(new java.io.InputStreamReader(in))
    val r = EDNReader.readAll(reader).asInstanceOf[Map[String,Any]]

    Units.resetUnits(r(":units").asInstanceOf[UnitMapT])
    Units.resetFundamentals(r(":fundamentals").asInstanceOf[Set[String]])
    Units.resetFundamentalUnits(
      r(":fundamental-units").asInstanceOf[Map[Map[String, Double], String]]
      .map { case (u, n) => (u.map { case (k ,v) => (k, v.toInt)}, n)})

    Prefixes.resetPrefixes(r(":prefixes").asInstanceOf[PrefixT])
    Prefixes.resetStandalonePrefixes(r(":standalone-prefixes").asInstanceOf[PrefixT])
  }

  implicit def doubleToNumber(d: Double) = Number(d)
  implicit def intToNumber(i: Int) = Number(i)
  implicit def stringToNumber(s: String) = Number.buildNumber(1, List(s))
  implicit def symbolToNumber(s: Symbol) = Number.buildNumber(1, List(s.name))

}
