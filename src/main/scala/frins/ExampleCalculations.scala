//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

// All these examples are taken from Frink's genius Sample Calculation page
// http://futureboy.us/frinkdocs/#SampleCalculations
// Alan Eliasen (@aeliasen) deserves all the praise

package frins

class ExampleCalculations {

  // setup the databases (only needs to be done once!)
  initDatabases

  // =================================================================
  // Mass and Volume

  // Let's say you wanted to fill your bedroom up with water.How much water would it take?
  // Let's say your room measures 10 feet by 12 feet wide by 8 feet high.

  N(10, 'feet) * N(12, 'feet) * N(8, 'feet) to 'gallons
  // res0: frins.NumberT = 7181.298701298702  [dimensionless]

  // It would take approximately 7181 gallons to fill it. Note that you get both an exact
  // fraction and an approximation. How much would that weigh, if you filled it with water?
  // Frins has the unit "water" which stands for the density of water.

  N(10, 'feet) * N(12, 'feet) * N(8, 'feet) * 'water to 'pounds
  // res1: frins.NumberT = 59930.842153098834  [dimensionless]

  // So it would weigh almost 60,000 pounds. What if you knew that your floor could only
  // support 2 tons? How deep could you fill the room with water?

  N(2, 'tons) / (N(10,'feet) * N(12, 'feet) * 'water) to 'feet
  // res2: frins.NumberT = 0.5339487791320046  [dimensionless]

  // So you could only fill it about 0.53 feet deep. It'll be a pretty sad pool party.



}
