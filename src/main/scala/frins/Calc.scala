package frins

class Calc(val unitsDb: UnitDb, val prefixDb: PrefixDb) {

  // Finds the longest prefix in a unit name and replaces it with with factor
  def resolvePrefixedUnit(name: String) = unitsDb.getUnit(name) match {
    case Some(u)  => (Number(1), name)
    case None     => {
      val pfx = prefixDb.allNames
        .toList
        .filter { name.startsWith(_) }
        .sortBy { _.size }
        .reverse
        .filter { n => unitsDb.isUnit(name.substring(n.size)) }
      if (pfx != List())
        (prefixDb.getPrefix(pfx.head).get, name.substring(pfx.head.size))
      else
        (Number(1.0), name)
    }
  }

  // Replaces all units with prefix + new unit
  // {prefix:u1 1, prefix:u2 -1} -> Number(fact, {u1:1, u2:-1})
  def resolveUnitPrefixes(units: UnitT) =
    units.foldLeft(Number(1)) { (acc, kv) =>
      val (prefix, exp) = kv
      val (factNum, newU) = resolvePrefixedUnit(prefix)
      val fact = factNum ** exp.abs
      acc * Number(1, Map(newU -> exp)) * (if (exp > 0) fact else 1 / fact)
    }

  // Replaces units with already defined ones, and remove zero units
  // (fj-val. fact {u1:1, u2:-1} -> Number(nfact, {u0:1, u2:-1}
  def normalizeUnits(n: NumberT) =
    (n.units.foldLeft(n) { (acc, kv) =>
      val (name, exp) = kv
      unitsDb.getUnit(name).orElse(prefixDb.getStandAlonePrefix(name)) match {
        case Some(num)   =>
          if (unitsDb.isFundamental(name)) acc
          else {
            val fact = num ** exp.abs
            (if (exp > 0) acc * fact else acc / fact) / Number(1, Map(name -> exp))
          }
        case None        => acc
      }
    }).cleanUnits

  // Resolve all units with prefixes, and normalized the result
  def resolveAndNormalizeUnits(us: UnitT) =
    normalizeUnits(resolveUnitPrefixes(us))

}
