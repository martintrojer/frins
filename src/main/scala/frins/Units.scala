package frins

class Units(us: UnitMapT, fus: RevUnitMapT, fs: Set[String]) {

  val units = Atom[UnitMapT](us)
  val fundamentalUnits = Atom[RevUnitMapT](fus)
  val fundamentals = Atom[Set[String]](fs)

  def getUnit(name: String) = units.get.get(name)
  def addUnit(name: String, value: NumberT) = units.swap(m => m + (name -> value))
  def getFundamentalUnit(units: UnitT) = fundamentalUnits.get.get(units)
  def addFundamentalUnit(name: String, value: UnitT) =
    fundamentalUnits.swap(m => m + (value -> name))
  def isFundamental(name: String) = fundamentals.get.contains(name)
  def addFundamental(name: String) = fundamentals.swap(s => s + name)

  // ---


}

object Units {
  def apply(units: UnitMapT, fundamentalUnits: RevUnitMapT, fundamentals: Set[String]) =
    new Units(units, fundamentalUnits, fundamentals)
}