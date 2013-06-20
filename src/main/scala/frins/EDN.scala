package frins

import scala.util.parsing.combinator._
import java.util.UUID
import java.util.{Date, GregorianCalendar, Calendar, TimeZone}
import java.text.SimpleDateFormat

object Instant {
  val timestamp = """(\d\d\d\d)(?:-(\d\d)(?:-(\d\d)(?:[T](\d\d)(?::(\d\d)(?::(\d\d)(?:[.](\d+))?)?)?)?)?)?(?:[Z]|([-+])(\d\d):(\d\d))?""".r

  def read(src: String) = {
    val timestamp(years, months, days, hours, minutes, seconds, nanoseconds, offsetSign, offsetHours, offsetMinutes) = src
    val cal = new GregorianCalendar(years.toInt, months.toInt - 1, days.toInt, hours.toInt, minutes.toInt, seconds.toInt)
    cal.set(Calendar.MILLISECOND, nanoseconds.toInt/1000000)
    cal.setTimeZone(TimeZone.getTimeZone(format("GMT%s%02d:%02d", offsetSign, offsetHours.toInt, offsetMinutes.toInt)))
    cal.getTime
  }
}

object EDNReader extends JavaTokenParsers {
  val set: Parser[Set[Any]] = "#{" ~> rep(elem) <~ "}" ^^ (Set() ++ _)
  val map: Parser[Map[Any, Any]] = "{" ~> rep(pair) <~ "}" ^^ (Map() ++ _)
  val vector: Parser[Vector[Any]] = "[" ~> rep(elem) <~ "]" ^^ (Vector() ++ _)
  val list: Parser[List[Any]] = "(" ~> rep(elem) <~ ")"
  val keyword: Parser[String] = """:[^,#\"\{\}\[\]\s]+""".r
  lazy val pair: Parser[(Any, Any)] = elem ~ elem ^^ {
    case key ~ value => (key, value)
  }
  lazy val tagElem: Parser[Any] = """#[^,#\"\{\}\[\]\s]+""".r ~ elem ^^ {
    case "#uuid" ~ (value: String) => UUID.fromString(value)
    case "#inst" ~ (value: String) => Instant.read(value)
    case "#frinj.core.fjv" ~ (m: Map[String, Any]) =>
      Number( m(":v").asInstanceOf[Double],
              m(":u").asInstanceOf[Map[String, Double]].map {
                case (k,v) => (k, v.toInt)})
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
    stringLiteral       ^^ { case "" => ""; case s => s.tail.init }

  val elem: Parser[Any] = ednElem | "," ~> elem | "N" ~> elem

  def readAll(reader: java.io.Reader) = parseAll(elem, reader).get
  def readAll(str: String) = parseAll(elem, str).get
}

