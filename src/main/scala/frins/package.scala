
package object frins {

  type UnitT = Map[String, Int]

  type NumberT = Number[Ratio]

  type UnitMapT = Map[String, NumberT]
  type RevUnitMapT = Map[UnitT, String]

  type PrefixT = Map[String, NumberT]

  // ---

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

//  implicit def doubleToNumber(d: Double) = Number(d)
  implicit def intToNumber(i: Int) = Number(i)
  implicit def ratioToNumber(r: Ratio) = Number(r)
  implicit def doubleToNumber(d: Double) = Number(d)
  implicit def stringToNumber(s: String) = Units.getUnit(s).getOrElse(Number(1, Map(s -> 1)))
  implicit def symbolToNumber(s: Symbol) = Units.getUnit(s.name).getOrElse(Number(1, Map(s.name -> 1)))

}
