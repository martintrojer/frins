package frins

import scala.util.parsing.combinator._
import java.util.UUID
import java.text.DateFormat

object EDN extends JavaTokenParsers {
  val set: Parser[Set[Any]] = "#{" ~> rep(elem) <~ "}" ^^ (Set() ++ _)
  val map: Parser[Map[Any, Any]] = "{" ~> rep(pair) <~ "}" ^^ (Map() ++ _)
  val vector: Parser[Vector[Any]] = "[" ~> rep(elem) <~ "]" ^^ (Vector() ++ _)
  val list: Parser[List[Any]] = "(" ~> rep(elem) <~ ")"
  val keyword: Parser[String] = """:[^,#\{\}\[\]\s]+""".r
  lazy val pair: Parser[(Any, Any)] = elem ~ elem ^^ {
    case key ~ value => (key, value)
  }
  lazy val tagElem: Parser[Any] = """#[^,#\{\}\[\]\s]+""".r ~ elem ^^ {
    case "#uuid" ~ (value: String) => UUID.fromString(value.tail.init)
    case "#inst" ~ (value: String) => DateFormat.getDateInstance(DateFormat.SHORT)
                                      .parse(value.tail.init)
    case "#frinj.core.fjv" ~ (m: Map[String, Any]) =>
      Number( m(":v").asInstanceOf[Double],
              m(":u").asInstanceOf[Map[String, Double]].map {case (k,v) => (k, v.toInt)})
    case name ~ value => (name, value)
  }
  val ratio: Parser[Double] = floatingPointNumber ~ "/" ~ floatingPointNumber ^^ {
    case num ~ _ ~ denom => num.toDouble / denom.toDouble
  }

  val ednElem: Parser[Any] =  set | map | vector | list | keyword | tagElem | ratio |
                              floatingPointNumber ^^ (_.toDouble) |
                              "nil"               ^^ (_ => null)  |
                              "true"              ^^ (_ => true)  |
                              "false"             ^^ (_ => false) |
                              stringLiteral

  val elem: Parser[Any] = ednElem | "," ~> elem | "N" ~> elem

  def parse(reader: java.io.Reader) = parseAll(elem, reader)
}
