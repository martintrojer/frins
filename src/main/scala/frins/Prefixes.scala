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

  // Finds the longest prefix in a unit name and replaces it with with factor
  def resolvePrefixedUnit(name: String, units: Units): (NumberT, String) = units.getUnit(name) match {
    case Some(u)  => (u, "")
    case None     => {
      val pfx = allNames
                .toList
                .filter { name.startsWith(_) }
                .sortBy {_.size}
                .reverse
                .filter { n => units.units.get.contains(name.substring (n.size)) }
      if (pfx != List())
        (getPrefix(pfx.head).get, name.substring(pfx.head.size))
      else
        (Number(1.0), name)
    }
  }
}


object Prefixes {
  def apply() = new Prefixes(Map(), Map())
  def apply(prefixes: PrefixT, standalonePrefixes: PrefixT) =
    new Prefixes(prefixes, standalonePrefixes)
}