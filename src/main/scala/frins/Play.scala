package frins

object Play extends App {

  val n10 = Number(1, Map("m" -> 1, "s" -> -1))
  val n20 = Number(1.0, Map("m" -> 1, "s" -> -1))

  println(n10 == n20)
  println(n10 + n20)

  println(Number(1) / Number(2))

  // -----------------

  val a = Atom(1)
  println(a)

  a.reset(2)
  println(a)

  a.swap(_ + 1)
  println(a)

  // -----------------

  def parseAndStff = {
    val in = getClass.getClassLoader.getResourceAsStream("units.edn")
    val reader = new java.io.BufferedReader(new java.io.InputStreamReader(in))
    val r = EDNReader.readAll(reader).asInstanceOf[Map[String,Any]]
    println(r.map {
      case (k,v:Map[Any,Any]) => (k, v.size)
      case (k,v:Set[Any]) => (k, v.size)})

    println(r(":prefixes"))
  }

  initDatabases()

  println(Units.isFundamental("m"))
  Units.addFundamental("foo")

  println(Units.units.get.take(5))
  println(Units.fundamentalUnits.get.take(5))
  println(Units.fundamentals.get)

  println((Number(1, Map("m" -> 1)) + "inch") / "yard")

}
