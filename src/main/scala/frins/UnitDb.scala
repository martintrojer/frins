package frins

class UnitDb(us: UnitMapT, fus: RevUnitMapT, fs: Set[String]) {

  val units = Atom[UnitMapT](us)
  val fundamentalUnits = Atom[RevUnitMapT](fus)
  val fundamentals = Atom[Set[String]](fs)

  def getUnit(name: String) = units.get.get(name)
  def isUnit(name: String) = units.get.contains(name)
  def addUnit(name: String, value: NumberT) = units.swap(m => m + (name -> value))
  def getFundamentalUnit(units: UnitT) = fundamentalUnits.get.get(units)
  def addFundamentalUnit(name: String, value: UnitT) =
    fundamentalUnits.swap(m => m + (value -> name))
  def isFundamental(name: String) = fundamentals.get.contains(name)
  def addFundamental(name: String) = fundamentals.swap(s => s + name)
}

object UnitDb {
  def apply() = new UnitDb(Map(), Map(), Set())
  def apply(units: UnitMapT, fundamentalUnits: RevUnitMapT, fundamentals: Set[String]) =
    new UnitDb(units, fundamentalUnits, fundamentals)
}