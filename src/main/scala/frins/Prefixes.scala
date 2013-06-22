package frins

object Prefixes {

  val prefixes = Atom[PrefixT](Map())
  val standalonePrefixes = Atom[PrefixT](Map())

  def resetPrefixes(fxs: PrefixT) = prefixes.reset(fxs)
  def resetStandalonePrefixes(sfxs: PrefixT) = standalonePrefixes.reset(sfxs)

  // ---

  def allNames(): Set[String] = prefixes.get.keySet ++ standalonePrefixes.get.keySet
  def getStandAlonePrefix(name: String) = standalonePrefixes.get.get(name)
  def getPrefix(name: String) = standalonePrefixes.get.get(name).orElse(prefixes.get.get(name))
  def isPrefix(name: String) = allNames.contains(name)
  def addPrefix(name: String, v: NumberT) = prefixes.swap(_ + (name -> v))
  def addStandalonePrefix(name: String, v: NumberT) = standalonePrefixes.swap(_ + (name -> v))
}
