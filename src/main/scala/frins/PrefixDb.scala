package frins

class PrefixDb(pfxs: PrefixT, spfxs: PrefixT) {

  val prefixes = Atom[PrefixT](pfxs)
  val standalonePrefixes = Atom[PrefixT](spfxs)

  def allNames(): Set[String] = prefixes.get.keySet ++ standalonePrefixes.get.keySet
  def getStandAlonePrefix(name: String) = standalonePrefixes.get.get(name)
  def getPrefix(name: String) = standalonePrefixes.get.get(name).orElse(prefixes.get.get(name))
  def isPrefix(name: String) = allNames.contains(name)
  def addPrefix(name: String, v: NumberT) = prefixes.swap(_ + (name -> v))
  def addStandalonePrefix(name: String, v: NumberT) = standalonePrefixes.swap(_ + (name -> v))
}

object PrefixDb {
  def apply() = new PrefixDb(Map(), Map())
  def apply(prefixes: PrefixT, standalonePrefixes: PrefixT) =
    new PrefixDb(prefixes, standalonePrefixes)
}