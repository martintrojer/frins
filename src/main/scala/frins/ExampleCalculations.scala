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

  'feet * 10 * 'feet * 12 * 'feet * 8 to 'gallons
  // res0: frins.NumberT = 7181.298701298702  [dimensionless]

  // It would take approximately 7181 gallons to fill it. How much would that weigh,
  // if you filled it with water? Frins has the unit "water" which stands for the density of water.

  'feet * 10 * 'feet * 12 * 'feet * 8 * 'water to 'pounds
  // res1: frins.NumberT = 59930.842153098834  [dimensionless]

  // So it would weigh almost 60,000 pounds. What if you knew that your floor could only
  // support 2 tons? How deep could you fill the room with water?

  N(2, 'tons) / ('feet * 10 * 'feet * 12 * 'water) to 'feet
  // res2: frins.NumberT = 0.5339487791320046  [dimensionless]

  // So you could only fill it about 0.53 feet deep. It'll be a pretty sad pool party.

  // =================================================================
  // Liquor

  // Let's say you want to define a new unit representing the amount of alcohol in a can
  // of (quality) 3.2 beer. Keep in mind that 3.2 beer is measured by alcohol/weight,
  // while almost all other liquors (and many beers) are usually measured in alcohol/volume.
  // The density ratio between water and alcohol is given by:

  'water / 'alcohol
  // res3: frins.Number[Double] = 1.2669453946534905  [dimensionless]

  // Water is thus 1.267 times denser than alcohol. 3.2 beer (measured by weight) is thus
  // actually 4.0 percent alcohol as measured by volume. Now let's set that variable in terms
  // of a beer's density of alcohol per volume so we can compare:

  Units.addUnit("beer", N(12, 'floz) * 3.2 * 'percent * 'water / 'alcohol)
  // res4: frins.NumberT = 1.4387730079817563E-5 m^3 [volume]

  // We have now added the unit 'beer to the unit database, and can use it as any other unit.
  // Then, you wanted to find out how many beers a big bottle of champagne is equal to:

  'magnum * 13.5 * 'percent to 'beer
  // res5: frins.NumberT = 14.07449256252434  [dimensionless]

  // You probably don't want to drink that whole bottle. Now let's say you're mixing Jungle
  // Juice (using a 1.75 liter bottle of Everclear (190 proof!)) and Kool-Aid to fill a
  // 5-gallon bucket (any resemblance to my college parties is completely intentional.)
  // What percent alcohol is that stuff?

  Units.addUnit("junglejuice", 'liter * 1.75 * 'proof * 190 / 5 * '_gallon)
  // res6: frins.NumberT = 0.08783720740908435  [dimensionless]

  // Please note the use of '_gallon above. If a symbol begins with (_) it's treated as an inverted unit.

  'junglejuice to 'percent
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

  'keg to 'case
  // res9: frins.NumberT = 6.888888888888888  [dimensionless]

  // How many 12 fluid ounce drinks (i.e. cans o' beer) in a keg?

  'keg to N(12, 'floz)
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

  // =================================================================
  // Movie magic

  // In the movie Independence Day, the alien mother ship is said to be 500 km in diameter
  // and have a mass 1/4 that of earth's moon. If the mother ship were a sphere, what would
  // its density be? (The volume of a sphere is 4/3 pi radius3)

  N(1.0/4, 'moonmass) / N(4.0/3, 'pi) / (N(500.0/2, 'km) ** 3) to 'water
  // res14: frins.NumberT = 280.6843843973219  [dimensionless]

  // Please note the use of a prefix here "km" = 1000 "m"

  // This makes the ship 280 times denser than water. This is 36 times denser than iron and
  // more than 12 times denser than any known element! As the ship is actually more a thin disc
  // than a sphere, it would actually be even denser. Since it contains lots of empty space,
  // parts of it would have to be much, much denser.

  // If the object is this dense and has such a large mass, what is its surface gravity?
  // Surface gravity is given by G mass / radius2, where G is the gravitational constant
  // (which Frins knows about):

  'G * 1.0/4 * 'moonmass / (N(500.0/2, 'km) ** 2) to 'gravity
  // res15: frins.NumberT = 2.000331549387406  [dimensionless]

  // The surface gravity of the spaceship is thus at least twice earth's gravity-- and that's
  // on the rim where gravity is weakest. It would actually be much higher since it's much,
  // much flatter than a sphere. I hope you're not the alien that has to go outside and paint it.

  // =================================================================
  // Fiscal Calculations

  // You can calculate the day that your company will run out of cash, based on their financial
  // statements. The following is an example for a real company, based on SEC filings, which
  // read as the following:
  // Cash and Cash Equivalents (in thousands)
  // December 31, 2000	June 30, 2001
  // $86,481	        $41,601

  Units.addUnit("burnrate", N(86481 - 41601, 'thousand, 'dollars) / ('$2001_06_30 - '$2000_12_31))
  // res16: frins.NumberT = 2.870519610100545 dollars s^-1 []

  // Please note the '$yyyy_MM_dd notation for date 'units' here

  'burnrate to ('dollars, '_day)
  // res17: frins.NumberT = 248012.8943126871  [dimensionless]

  (N(41601, 'thousand, 'dollars) / 'burnrate) to 'days
  // res18: frins.NumberT = 167.7372465463458  [dimensionless]

  // Using date/time math, starting from the last report date (June 30, 2001) you can
  // find out the exact date this corresponds to:

  ('$2001_06_30 + N(41601, 'thousand, 'dollars) / 'burnrate) toDate
  // res19: java.util.Date = Fri Dec 14 16:41:38 GMT 2001

  // =================================================================
  // Ouch!

  // At the moment, I'm watching CNN which is discussing some land-mines used in Afghanistan.
  // They showed a very small mine (about the size of a bran muffin) containing "51 grams of TNT"
  // and they asked how much destructive force that carries. Frins's data file includes how much
  // energy is in a mass of TNT, specified by the unit "TNT". How many feet in the air could 51
  // grams of TNT throw me, assuming perfect efficiency, and knowing energy = mass * gravity * height?

  'TNT * 51 * 'grams to N(185, 'pounds, 'gravity, 'feet)
  // res20: frins.NumberT = 937.7628167428614  [dimensionless]

  // Yikes. 937 feet. But the only difference between explosives and other combustible fuels
  // is the rapidity of combustion, not in the quantity of energy. How much gasoline contains
  // the same amount of energy?

  'TNT * 51 * 'grams to ('teaspoon, 'gasoline)
  // res21: frins.NumberT = 1.290325559425589  [dimensionless]

  // 1.29 teaspoons? That's not much at all. You're buying a huge amount of energy when you fill
  // up your car.

  // =================================================================
  // Sniping eBay Auctions

  // I need a monocle, but I don't want to pay a lot for it. The eBay monocle auction ends in
  // 7 hours and 44 minutes... what time do I need to set the alarm clock for to remind me?

  '$now + 'hours * 7 + 'minutes * 44 toDate

}
