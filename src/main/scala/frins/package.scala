package object frins {

  type UnitT = Map[String, Int]

  type NumberT = Number[Double]

  type UnitMapT = Map[String, NumberT]
  type RevUnitMapT = Map[UnitT, String]

  type PrefixT = Map[String, NumberT]
}
