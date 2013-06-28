//  Copyright (c) Martin Trojer. All rights reserved.
//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

package frins

/** Holder of the global Prefix database (state)
  *
  * StandAlone Prefixes are unit prefixes that can also appear stand alone as a normal unit
  * Prefixes can only appear as a prefix of another (valid) non-prefix unit
  */

import Atom._

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
