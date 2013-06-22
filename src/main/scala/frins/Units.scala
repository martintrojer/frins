package frins

// The global unit state

object Units {

  val units = Atom[UnitMapT](Map())
  val fundamentalUnits = Atom[RevUnitMapT](Map())
  val fundamentals = Atom[Set[String]](Set())

  def resetUnits(us: UnitMapT) = units.reset(us)
  def resetFundamentals(fs: Set[String]) = fundamentals.reset(fs)
  def resetFundamentalUnits(fus: RevUnitMapT) = fundamentalUnits.reset(fus)

  // ---

  def getUnit(name: String) = units.get.get(name)
  def isUnit(name: String) = units.get.contains(name)
  def addUnit(name: String, value: NumberT) = units.swap(m => m + (name -> value))
  def getFundamentalUnit(units: UnitT) = fundamentalUnits.get.get(units)
  def addFundamentalUnit(name: String, value: UnitT) =
    fundamentalUnits.swap(m => m + (value -> name))
  def isFundamental(name: String) = fundamentals.get.contains(name)
  def addFundamental(name: String) = fundamentals.swap(s => s + name)
}
