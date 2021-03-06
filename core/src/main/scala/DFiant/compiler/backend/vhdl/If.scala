package DFiant
package compiler.backend.vhdl
import compiler.printer.formatter._

private object If {
  def apply(cond : String, statements : List[String], closing : String)(
    implicit printer : Printer
  ) : String = {
    import printer.config._
    s"""$KW if $cond $KW then
       |${statements.mkString("\n").delim()}
       |${if(closing.isEmpty) End() else closing}""".stripMargin
  }

  object End {
    def apply()(implicit printer : Printer) : String = {
      import printer.config._
      s"$KW end $KW if;"
    }
  }
  object ElsIf {
    def apply(cond : String, statements : List[String], closing : String)(implicit printer : Printer) : String = {
      import printer.config._
      s"""$KW elsif $cond $KW then
         |${statements.mkString("\n").delim()}
         |${if(closing.isEmpty) End() else closing}""".stripMargin
    }
  }
  object Else {
    def apply(statements : List[String])(implicit printer : Printer) : String = {
      import printer.config._
      s"""$KW else
         |${statements.mkString("\n").delim()}
         |${End()}""".stripMargin
    }
  }
}
