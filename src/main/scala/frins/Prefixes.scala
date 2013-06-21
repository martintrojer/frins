package frins

class Prefixes(pfxs: PrefixT, spfxs: PrefixT) {

  val prefixes = Atom[PrefixT](pfxs)
  val standalonePrefixes = Atom[PrefixT](spfxs)

  def allNames(): Set[String] = prefixes.get.keySet ++ standalonePrefixes.get.keySet
  def getPrefix(name: String) = standalonePrefixes.get.get(name) match {
    case d @ Some(_)  => d
    case None         => prefixes.get.get(name)
  }
  def isPrefix(name: String) = allNames.contains(name)
  def addPrefix(name: String, v: NumberT) = prefixes.swap(_ + (name -> v))
  def addStandalonePrefix(name: String, v: NumberT) = standalonePrefixes.swap(_ + (name -> v))

  // Does the unitsDb suggest this function is in the wrong place?

  // Finds the longest prefix in a unit name and replaces it with with factor
  def resolvePrefixedUnit(name: String, unitsDb: Units) = unitsDb.getUnit(name) match {
    case Some(u)  => (Number(1), name)
    case None     => {
      val pfx = allNames
                .toList
                .filter { name.startsWith(_) }
                .sortBy { _.size }
                .reverse
                .filter { n => unitsDb.units.get.contains(name.substring(n.size)) }
      if (pfx != List())
        (getPrefix(pfx.head).get, name.substring(pfx.head.size))
      else
        (Number(1.0), name)
    }
  }

  // Replaces all units with prefix + new unit
  def resolveUnitPrefixes(units: UnitT, unitsDb: Units) =
    units.foldLeft(Number(1)) { (acc, kv) =>
      val (prefix, value) = kv
      val (factNum, newU) = resolvePrefixedUnit(prefix, unitsDb)
      val fact = (factNum ** value.abs)
      acc * Number(1, Map(newU -> value)) * (if (value > 0) fact else 1 / fact)
    }
}

object Prefixes {
  def apply() = new Prefixes(Map(), Map())
  def apply(prefixes: PrefixT, standalonePrefixes: PrefixT) =
    new Prefixes(prefixes, standalonePrefixes)
}