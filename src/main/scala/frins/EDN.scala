//  Copyright (c) Martin Trojer. All rights reserved.
//  The use and distribution terms for this software are covered by the
//  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//  which can be found in the file epl-v10.html at the root of this distribution.
//  By using this software in any fashion, you are agreeing to be bound by
//  the terms of this license.
//  You must not remove this notice, or any other, from this software.

// EDN format reader taken from https://github.com/martintrojer/edn-scala

package frins

import scala.util.parsing.combinator._

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
    case "#frinj.core.fjv" ~ (m: Map[_, _]) =>
      Number( m.asInstanceOf[Map[String,Any]](":v").asInstanceOf[Double],
              m.asInstanceOf[Map[String,Any]](":u").asInstanceOf[Map[String, Double]].map {
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

