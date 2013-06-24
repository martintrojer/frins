//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

// All these examples are taken from Frink's genius Sample Calculation page
// http://futureboy.us/frinkdocs/#SampleCalculations
// Alan Eliasen (@aeliasen) deserves all the praise

// In this class you'll find examples of how to use the Number builders, implicits in a number of different
// ways to perform Frins calculations.

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

  // It would take approximately 7181 gallons to fill it. How much would that weigh,
  // if you filled it with water? Frins has the unit "water" which stands for the density of water.

  N(10, 'feet) * N(12, 'feet) * N(8, 'feet) * 'water to 'pounds
  // res1: frins.NumberT = 59930.842153098834  [dimensionless]

  // So it would weigh almost 60,000 pounds. What if you knew that your floor could only
  // support 2 tons? How deep could you fill the room with water?

  N(2, 'tons) / (N(10,'feet) * N(12, 'feet) * 'water) to 'feet
  // res2: frins.NumberT = 0.5339487791320046  [dimensionless]

  // So you could only fill it about 0.53 feet deep. It'll be a pretty sad pool party.

  // =================================================================
  // Liquor

  // Let's say you want to define a new unit representing the amount of alcohol in a can
  // of (quality) 3.2 beer. Keep in mind that 3.2 beer is measured by alcohol/weight,
  // while almost all other liquors (and many beers) are usually measured in alcohol/volume.
  // The density ratio between water and alcohol is given by:

  N('water) / 'alcohol
  // res3: frins.Number[Double] = 1.2669453946534905  [dimensionless]

  // Water is thus 1.267 times denser than alcohol. 3.2 beer (measured by weight) is thus
  // actually 4.0 percent alcohol as measured by volume. Now let's set that variable in terms
  // of a beer's density of alcohol per volume so we can compare:

  Units.addUnit("beer", N(12, 'floz) * 3.2 * 'percent * 'water / 'alcohol)
  // res4: frins.NumberT = 1.4387730079817563E-5 m^3 [volume]

  // We have now added the unit 'beer to the unit database, and can use it as any other unit.
  // Then, you wanted to find out how many beers a big bottle of champagne is equal to:

  N('magnum) * 13.5 * 'percent to 'beer
  // res5: frins.NumberT = 14.07449256252434  [dimensionless]

  // You probably don't want to drink that whole bottle. Now let's say you're mixing Jungle
  // Juice (using a 1.75 liter bottle of Everclear (190 proof!)) and Kool-Aid to fill a
  // 5-gallon bucket (any resemblance to my college parties is completely intentional.)
  // What percent alcohol is that stuff?

  Units.addUnit("junglejuice", N(1.75, 'liter) * 190 * 'proof / 5 * '_gallon)
  // res6: frins.NumberT = 0.08783720740908435  [dimensionless]

  // Please note the use of '_gallon above. If a symbol begins with (_) it's treated as an inverted unit.

  N('junglejuice) to 'percent
  // res7: frins.NumberT = 8.783720740908436  [dimensionless]

  // It's really not that strong. About 8.8%. But if you drink 5 cups of that,
  // at 12 fluid ounces each, how many beers have you had?

  N(5 * 12, 'floz, 'junglejuice) to 'beer
  // res8: frins.NumberT = 10.83279809499848  [dimensionless]

  // Maybe that's why people were getting punched in the head. QED.

  // =================================================================
  // More Liquor

  // How many cases in a keg? (A keg is a normal-sized keg, what those in the beer
  // industry would call a "half barrel," or 1/2 beerbarrel in Frins notation.
  // I don't think they sell full barrels. I've never seen one. It would weigh 258 pounds.
  // A "pony keg" is a "quarter barrel" or, in Frins notation, ponykeg or 1/4 beerbarrel)

  N('keg) to 'case
  // res9: frins.NumberT = 6.888888888888888  [dimensionless]

  // How many 12 fluid ounce drinks (i.e. cans o' beer) in a keg?

  N('keg) to N(12, 'floz)
  // res10: frins.NumberT = 165.33333333333331  [dimensionless]

  // What is the price in dollars per fluid ounce of alcohol when buying a keg of 3.2 beer?
  // (Remember that 3.2 beer is measured in alcohol/weight, so we correct by the density
  // ratio of water/alcohol to get alcohol by volume:

  N(60, 'dollars) / ('keg * 3.2 * 'percent *'water * '_alcohol) to ('dollars, '_floz)
  // res11: frins.NumberT = 0.7459362399193549  [dimensionless]

  // A bottle of cheap wine? (A "winebottle" is the standard 750 ml size.)

  N(6.99, 'dollars) / ('winebottle * 13 *'percent) to ('dollars, '_floz)
  // res12: frins.NumberT = 2.120194580942308  [dimensionless]

  // A big plastic bottle of really bad vodka?

  N(13.99, 'dollars) / (N(1750, 'ml) * 80 * 'proof) to ('dollars, '_floz)
  // res13: frins.NumberT = 0.5910481122562501  [dimensionless]


}
