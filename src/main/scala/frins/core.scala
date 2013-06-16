package frins

object core extends App {
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

}
