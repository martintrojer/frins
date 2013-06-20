package frins

object Play extends App {
  val dn: Double = 1.0
  val bn: BigDecimal = 1.0

  val n1 = new Number(dn, Map("m" -> 1, "s" -> -1))
  val n2 = new Number(bn, Map("m" -> 1, "s" -> -1))

  println(n1 == n2)         // this should be false?
  // println(n1 + n2)

  val n10 = Number(dn, Map("m" -> 1, "s" -> -1))
  val n20 = Number(bn, Map("m" -> 1, "s" -> -1))

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

  val in = getClass.getClassLoader.getResourceAsStream("units.edn")
  val reader = new java.io.BufferedReader(new java.io.InputStreamReader(in))

  val r = EDNReader.readAll(reader).asInstanceOf[Map[String,Any]]
  println(r.map {
    case (k,v:Map[Any,Any]) => (k, v.size)
    case (k,v:Set[Any]) => (k, v.size)})

  val units = Units(
    r(":units").asInstanceOf[UnitMapT],
    r(":fundamental-units").asInstanceOf[Map[Map[String, Double], String]]
      .map { case (u, n) => (u.map { case (k ,v) => (k, v.toInt)}, n)},
    r(":fundamentals").asInstanceOf[Set[String]])

  println(units.isFundamental("m"))
  units.addFundamental("foo")
  println(units.fundamentals.get)

  println(units.units.get.take(5))
  println(units.fundamentalUnits.get.take(5))
  println(units.fundamentals.get)

}
