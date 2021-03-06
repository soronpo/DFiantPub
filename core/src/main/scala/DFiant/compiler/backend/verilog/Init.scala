package DFiant
package compiler.backend.verilog
import DFiant.DFAny.Token
import compiler.printer.formatter._

private object Init {
  def apply(member : DFAny.Member)(implicit printer : Printer) : String = {
    import printer.config._
    val tokenOption = member match {
      case dcl : DFAny.Dcl => dcl.externalInit match {
        case Some(token +: Nil) if !token.isBubble && !member.isPortIn =>
          Some(token)
        case _ => None
      }
      case const : DFAny.Const => Some(const.token)
      case _ => None
    }
    tokenOption match {
      case Some(token) => token match {
        case DFVector.Token(_, cellTokens) =>
          val cellInit = cellTokens.zipWithIndex
            .map{case (v, i) => s"${member.name}[$i] ${ALGN(0)}= ${Value.const(v)};"}
          s"""$KW initial $KW begin
             |${cellInit.mkString("\n").delim()}
             |$KW end""".stripMargin
        case _ =>
          s" = ${Value.const(token)}"
      }
      case None => ""
    }
  }
}
