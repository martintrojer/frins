//  Copyright (c) Martin Trojer. All rights reserved.
//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

package frins

/** Holder of the global Units database (state)
  *
  * Units contains a NumberT representation of a given unit
  *
  * Fundamentals is a set of the fundamental units (in Units) that all other units are converted to
  *
  * FundamentalUnits are a reverse lookup of known Unit signatures and a helpful name
  */

import Atom._

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
  def addUnit(name: String, value: NumberT) = {
    units.swap(m => m + (name -> value))
    getUnit(name).get
  }
  def getFundamentalUnit(units: UnitT) = fundamentalUnits.get.get(units)
  def addFundamentalUnit(name: String, value: UnitT) = fundamentalUnits.swap(m => m + (value -> name))
  def isFundamental(name: String) = fundamentals.get.contains(name)
  def addFundamental(name: String) = fundamentals.swap(s => s + name)
}
