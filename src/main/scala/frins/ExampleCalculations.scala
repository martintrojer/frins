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
  // December 31, 2000	  June 30, 2001
  // $86,481	            $41,601

  Units.addUnit("burnrate", N(86481 - 41601, 'thousand, 'dollars) / ('$2001_06_30 - '$2000_12_31))
  // res16: frins.NumberT = 2.870519610100545 dollars s^-1 []

  // Please note the '$yyyy_MM_dd notation for date 'units' here

  'burnrate to ('dollars, '_day)
  // res17: frins.NumberT = 248012.8943126871  [dimensionless]

  N(41601, 'thousand, 'dollars) / 'burnrate to 'days
  // res18: frins.NumberT = 167.7372465463458  [dimensionless]

  // Using date/time math, starting from the last report date (June 30, 2001) you can
  // find out the exact date this corresponds to:

  '$2001_06_30 + N(41601, 'thousand, 'dollars) / 'burnrate toDate
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

  // =================================================================
  // Junkyard Wars

  // I can't watch Junkyard Wars (or lots of other television shows) without having Frins at
  // my side. This week the team has to float a submerged half-ton Cooper Mini... how many oil
  // barrels will they need to use as floats?

  'half * 'ton to ('barrels,'water)
  // res23: frins.NumberT = 2.8530101742118243  [dimensionless]

  // They're trying to hand-pump air down to the barrels, submerged "2 fathoms" below the water.
  // If the guy can sustain 40 watts of pumping power, how many minutes will it take to fill
  // the barrel?

  'fathoms * 2 * 'water * 'gravity * 'barrel to N(40, 'watts, 'minutes)
  // res24: frins.NumberT = 2.376123072093987  [dimensionless]

  // And how many food Calories (a food Calorie (with a capital 'C') equals 1000 calories with
  // a small 'c') will he burn to fill a barrel?

  'fathoms * 2 * 'water * 'gravity * 'barrel to 'Calories
  // res25: frins.NumberT = 1.3620653895637644  [dimensionless]

  // Better eat a Tic-Tac first.

  // =================================================================
  // Body Heat

  // I've seen lots of figures about how much heat the human body produces. You can easily
  // calculate the upper limit based on how much food you eat a day. Say, you eat 2000 Calories
  // a day (again, food Calories with a capital "C" are equal to 1000 calories with a little "c".)

  N(2000, 'Calories, '_day) to 'watts
  // res26: frins.NumberT = 96.91666666666667  [dimensionless]

  // So, your average power and/or heat output is slightly less than a 100-watt bulb.
  // (Note that your heat is radiated over a much larger area so the temperature is much lower.)
  // Many days I could be replaced entirely with a 100-watt bulb and have no discernible effect
  // on the universe.

  // =================================================================
  // Microwave Cookery

  // I'm heating up yummy mustard greens in my microwave, but I don't want to overheat them.
  // I just want to warm them up. If I run my 1100 watt microwave for 30 seconds, how much will
  // their temperature increase? I have a big 27 ounce (mass) can, and I'll assume that their
  // specific heat is about the same as that of water (1 calorie/gram/degC):

  'W * 1100 * 'sec * 30 / ('oz * 27 * 'calorie / 'gram / 'degC) to 'degF
  // res27: frins.NumberT = 18.53509035279376  [dimensionless]

  // 30 seconds should raise the temperature by no more than 18 degrees Fahrenheit, assuming
  // perfect transfer of microwave energy to heat.
  // Knowing this, I could see how efficiently my microwave actually heats food. I could heat a
  // quantity of water and measure the temperature change in the water. I'll do that sometime if
  // I can find my good thermometer.

  // =================================================================
  // Why is Superman so Lazy?

  // Superman is always rescuing school buses that are falling off of cliffs, flying to the moon,
  // lifting cars over his head, and generally showing off. So why does he still allow so many
  // accidents to happen? Shouldn't he be able to rescue everybody who has a Volkswagen parked
  // on their chest?
  // While searching for answers, I found out three interesting things about Superman:

  // 1. He's 6 feet 3 inches tall.
  // 2. He weighs 225 pounds.
  // 3. He gets his strength from being charged up with solar energy.

  // This is enough information to find some answers. Frins has units called sunpower
  // (the total power radiated by the sun) and sundist (the distance between the earth and the sun.)
  // Thus, we can find the sun's power that strikes an area at the distance of the earth
  // (knowing the surface area of a sphere is 4 pi radius2):

  Units.addUnit("earthpower", 'sunpower / ('pi * 4 * ('sundist ** 2)))
  // res28: frins.NumberT = 1372.5422836662622 kg s^-3 [heat_flux_density]

  // This is about 1372 watts per square meter. Superman is a pretty big guy--let's say the
  // surface area he can present to the sun is 12 square feet. (This is probably a bit high--
  // it makes him an average of 23 inches wide over his entire height.)
  // This allows Superman to charge up at a power of:

  Units.addUnit("chargerate", 'earthpower * 12 * ('ft ** 2))
  // res29: frins.NumberT = 1530.1602081736573 kg s^-3 m^2 [power]

  // Superman thus charges up at the rate of 1530 joules/sec or 1530 watts. At this rate,
  // how long does he have to charge up before he can lift a 2 ton truck over his head?
  // (Knowing energy = mass * height * gravity)

  'ton * 2 * 'feet * 7 * 'gravity / 'chargerate
  // res30: frins.Number[Double] = 24.809756749974778 s [time]

  // So, charging up for 25 seconds allows him to save one dumb kid who is acting as a speed bump.
  // So his power is huge but not infinite. He couldn't sustain a higher rate (unless he showed
  // off less by lifting the car only a foot or two.) Lifting a truck every 30 seconds or so
  // isn't bad, though. He could be saving a lot more people. So why doesn't he?

  // Well, we've all seen the movie. He's using his super-powers to pick up chicks. Literally.
  // Superman decides to take a break from saving lives and takes Lois Lane up in the sky for a
  // joyride. So how long does he have to charge up with solar energy to fly himself and Lois Lane
  // (let's say she weighs 135 pounds) up to 15,000 feet?

  N(225 + 135, 'pounds) * 15000 * 'feet * 'gravity / 'chargerate to 'minutes
  // res31: frins.NumberT = 79.7456466963475  [dimensionless]

  // So, Superman has to charge up with solar energy for an hour to cart Lois up there.
  // With the same energy, he could have saved over 120 trapped kids. Keep in mind that Lois
  // could do her part, too. If she left her purse behind or didn't weigh as much, he'd have
  // more energy left over to save people. If she would manage to shed just two pounds of
  // cargo weight, Superman would have enough energy to save another kid's life.

  // Sure, he's a great guy, and, sure, he's the Defender of Truth, Justice, and the American Way,
  // but can't he find a better use for his super-powers than schlepping some shiksa into the
  // stratosphere? Shovel my walk, he could, in 3 seconds--and me with the sciatica.

  // =================================================================
  // Fart Jokes

  // "if you fart continuously for 6 years and 9 months, you'll have enough gas to create the
  // equivalent of an atomic bomb." Hee hee. Cute.
  // The Hiroshima bomb had a yield of 12.5 kilotons of TNT, which is a very small bomb by today's
  // standards. How many horsepower would that be?

  'TNT * 'kilotons * 12.5 / ('years * 6 + 'months * 9) to 'horsepower
  // res32: frins.NumberT = 329.26013859711395  [dimensionless]

  // Can you produce a 329-horsepower blowtorch of a fart? I doubt it. That's the power produced
  // by a Corvette engine running just at its melting point. A one-second fart with that much power
  // could blow me 1000 feet straight up. To produce that kind of energy, how much food would you
  // have to eat a day?

  'TNT * 'kilotons * 12.5 / ('years * 6 + 'months * 9) to ('Calories, '_day)
  // res33: frins.NumberT = 5066811.55086559  [dimensionless]

  // Ummm... can you eat over 5 million Calories a day? (Again, note that these are food Calories
  // with a capital 'c' which are equal to 1000 calories with a small 'c'.) If you were a perfect
  // fart factory, converting food energy into farts with 100% efficiency, and ate a normal 2000
  // Calories/day, how many years would it really take?

  'TNT * 'kilotons * 12.5 / ('Calories * 2000 / 'day) to 'years
  // res34: frins.NumberT = 17100.488984171363  [dimensionless]

  // 17,000 years is still a huge underestimate; I don't know how much of your energy actually goes
  // into fart production. Oh well. To continue the calculations, let's guess your butthole has a
  // diameter of 1 inch (no, you go measure it.) Let's also guess that the gas you actually produce
  // in a fart is only 1/10 as combustible as pure natural gas. What would be the velocity of the gas
  // coming out?

  'TNT * 'kilotons * 12.5 / 'naturalgas / ('years * 6 + 'months * 9) / ('pi * (('in * 0.5) ** 2)) * 10 to 'mph
  // res35: frins.NumberT = 281.5904462031102  [dimensionless]

  // Nobody likes sitting next to a 280-mile-per-hour fart-machine. Lesson: Even the smallest atomic
  // bombs are really unbelievably powerful and whoever originally calculated this isn't any fun
  // to be around if they really fart that much.

  // =================================================================
  // Advanced Farting

  // What do you think are the most flammable gases in a fart? Most people think it's methane,
  // but I found some medical studies that disprove this. Most people hardly have any methane in
  // their intestines. For example, one study stated that only 4 out of 11 people had any detectable
  // methane in their intestines! So what's the rest of the gas?

  // Gas	          Percent by Volume
  // Nitrogen	      64%
  // Carbon Dioxide 14%
  // Hydrogen	      19%
  // Methane	      3.2%
  // Oxygen	        0.7%

  // These studies also note that the average person has 100 milliliters of gas is present in their
  // intestinal tract at any given time. The average person expels 400-2000 ml of gas daily
  // (and I'm not talking about through the mouth and nose.)

  // Okay, that's almost enough information to figure out available fart energy. Now all we need
  // to know is the energy of combustion of the flammable gases. Of the above, only hydrogen and
  // methane are readily combustible. Looking up their energies of combustion:

  // Gas	                Energy of Combustion in kJ/mol
  // Hydrogen (H2)        285.8
  // Methane (CH4)        890.8

  // Okay, that's plenty enough information to find out how much energy is released in a day of
  // farting! Say you're on the farty end of the scale, and you produce the 2000 ml of gas each day.

  // Note that the energies above are given in kJ/mol, but we have volumes in milliliters.
  // As you may have learned in chemistry class, a mole of any gas at standard temperature and
  // pressure takes up the same volume. Frins knows this as molarvolume.

  // The total energy in the hydrogen (keeping in mind that hydrogen makes up 19% of the 2000
  // ml volume) is given by:

  Units.addUnit("h2energy", N(2000, 'ml) / 'molarvolume * 19 * 'percent * 285.8 * 'kJ / 'mol)
  // res36: frins.NumberT = 4845.371389928871 m^2 kg s^-2 [energy]

  // The combustible hydrogen thus produces 4800 joules (per day.) Now, for the methane,
  // which makes a smaller percentage, but releases more energy per mole:

  Units.addUnit("methanenergy", N(2000, 'ml) / 'molarvolume * 3.2 * 'percent * 890.8 * 'kJ / 'mol)
  // res37: frins.NumberT = 2543.5567509991606 m^2 kg s^-2 [energy]

  // The energy in the combustible methane is thus about 2500 joules (per day), about half the
  // energy produced from the hydrogen. Thus, the grand total of energy produced by combustible
  // farts by a farty person in a day, in food Calories (with a capital C, remember--these are
  // what a physicist would call a kilocalorie) is:

  'methanenergy + 'h2energy to 'Calories
  // res38: frins.NumberT = 1.7648151669360923  [dimensionless]

  // Which gives a result of about 1.76 Calories/day of energy available from burning your farts.
  // (About 1.16 Calories from hydrogen, and about 0.60 Calories from methane.) This is out of the
  // 2000 Calories that an average person eats a day. Or, one part in about 1133 of the energy in
  // the food you eat is available in fart energy, (again, for a gassy person.)

  // Thus, a good estimate to the problem stated above is that a real (gassy) human would need to
  // save their farts for:

  'TNT * 'kilotons * 12.5 / (('methanenergy + 'h2energy) / 'day) to 'days
  // res39: frins.NumberT = 7.07815788738084E9  [dimensionless]

  // or about 7 billion years to make the equivalent of the energy in a (small) atomic bomb!

  // =================================================================
  // Incorrect facts

  // -----------
  // QE2

  // "The cruise liner, Queen Elizabeth II, moves only six inches for each gallon of diesel
  // that it burns."

  // From a page of facts about the QE2, we find that the ship consumes 18 tons of fuel per
  // hour at a service speed of 28 knots. By legislation in many areas, diesel fuel must have
  // a density no higher than 0.85 kg/liter (if it were watered down, it would be higher.)

  'tons * 18 / 'hour / ('knot * 28) / ('kg * 0.85 / 'liter) to ('feet, '_gallon)
  // res40: frins.NumberT = 33.52338503156235  [dimensionless]

  // They're very, very wrong. It actually travels about 33.5 feet per gallon, or 157 gallons/mile.
  // They're only off by a factor of 67. Still not great gas mileage, though.

  // -----------
  // Hamburgers and Cars

  // "pound for pound, hamburgers cost more than new cars."

  // Let's see... let's try with a medium-expensive, light car. A 2001 Corvette Z06 weighs
  // 3,115 pounds and costs $48,055.

  'dollars * 48055 / ('lb * 3115) to ('dollars, '_lb)
  // res41: frins.NumberT = 15.42696629213483  [dimensionless]

  // I know I don't pay $15/lb for hamburger.

  // =================================================================
  // Biblical References

  // So you want to build an ark, do you? And not an Ark of the Covenant, but the boat.
  // How bad was that flood?

  // The bible is also quite precise in its measurement of the flood. Genesis 7:19-20 states
  // that "And the waters prevailed exceedingly upon the earth; and all the mountains, that were
  // under the whole heaven, were covered. Fifteen cubits upward did the waters prevail; and the
  // mountains were covered."

  // Okay, so the highest mountains of the earth were covered, plus an extra 15 cubits
  // (approx 27 feet) for good measure. The current measurements for highest mountain is
  // Mt. Everest at 29030.8 feet (according to the highly dubious and utterly non-trustable 2002
  // Guinness Book of World Records.) I know that Everest is growing slowly,
  // (best estimates are 2.4 inches/year) so we'll discount for that.

  Units.addUnit("depth", 'feet * 29030 + 'biblicalcubits * 15 + ('inches * -2.4 / 'year * 4000 * 'years))
  // res42: frins.NumberT = 8612.8098 m [length]

  // About 28257 feet of water. This was deposited over 40 days. The rainfall was thus:

  'depth / ('days * 40) to ('inch, '_hour)
  // res43: frins.NumberT = 353.21562500000005  [dimensionless]

  // About 29 feet/hour. A good rain around here is about an inch an hour.
  // The very rainiest places on earth like Cherrapunji get about this much rain in a year.
  // (I'm campaigning Colorado farmers to sin a bit more...)

  // =================================================================
  // E=mc2

  // Everyone knows Einstein's E=mc2 equation, but to apply it is often very difficult because
  // the units come out so strange. Let's see, I have mass in pounds, and the speed of light is
  // 186,282 miles/second... ummm... what does that come out to? In Frins the calculation becomes
  // transparently simple.

  // If you took the matter in a teaspoon of water, and converted that to energy, how many gallons
  // of gasoline would that equal?

  'teaspoon * 'water * ('c ** 2) to ('gallons, 'gasoline)
  // res44: frins.NumberT = 3164209.862836101  [dimensionless]

  // Unbelievable. The energy in a teaspoon of water, if we could extract it, is equal to burning
  // more than 3 million gallons of gasoline.

}
