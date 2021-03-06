///*
// *     This file is part of DFiant.
// *
// *     DFiant is free software: you can redistribute it and/or modify
// *     it under the terms of the GNU Lesser General Public License as published by
// *     the Free Software Foundation, either version 3 of the License, or
// *     any later version.
// *
// *     DFiant is distributed in the hope that it will be useful,
// *     but WITHOUT ANY WARRANTY; without even the implied warranty of
// *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *     GNU Lesser General Public License for more details.
// *
// *     You should have received a copy of the GNU Lesser General Public License
// *     along with DFiant.  If not, see <https://www.gnu.org/licenses/>.
// */
//
//import DFiant._
//trait IODesignConn1 extends DFDesign {
//  val i = DFUInt(8) <> IN init(1,2)
//  val o = DFUInt(8) <> OUT
//  val o2 = DFUInt(8) <> OUT
//  val o3 = DFBits(8) <> OUT
//  o3 == b0s
////  val o2 = DFBits(8) <> OUT
////  val temp = DFUInt(8) init 0
////  val temp_b = temp.bits(3,0)
////  temp := i + 1
//
////  temp_b := b"1001"
//  val temp2 = DFUInt(8) init 5
//
////  val temp_u = temp.uint
////  temp_u := 0
////  val temp2_b = temp2.bits
////  temp2_b := b"11110000"
//
////  matchdf (i)
////    .casedf(1 to 5, 20 to 25) {temp2 := 1}
////    .casedf(?)(temp2 := 5)
//
////  o2 <> temp2_b
////  o <> i
//}
//
//trait IODesignConn1b extends DFDesign {
//  val i = DFUInt(8) <> IN init(1,2)
//  val o = DFBool() <> OUT
//  val b = (i < 2) && (i > 0)
//  o <> b
//}
//
//trait IODesignIf extends DFDesign {
//  val i1 = DFUInt(8) <> IN init (1, 1, Bubble, 1)
//  val i2 = DFUInt(8) <> IN init (2, Bubble)
//  val o1 = DFUInt(8) <> OUT
//  val o2 = DFUInt(8) <> OUT
//  val b = DFBool() <> IN init (false, true, true, true)
//  val myIf = ifdf (b) {
//    val myIf2 = ifdf (b) {
//      o1 := i1
//    }.elseifdf(b) {
//      o1 := i1
//    }
//  }.elsedf {
//    o1 := i1
//  }
//  val ret = DFUInt(8).ifdf (b) {
//    DFUInt(8).ifdf (i1 < 8) {
//      i1
//    }.elseifdf(b) {
//      i2
//    }.elsedf {
//      i1
//    }
//  }.elsedf {
//    i2
//  }
//  o2 <> ret
//}
//
//trait IODesignMatch extends DFDesign {
//  val i1 = DFUInt(8) <> IN init (1, 1, Bubble, 1)
//  val i2 = DFUInt(8) <> IN init (2, 8, 7, 11, 21)
//  val o1 = DFUInt(8) <> OUT
//  val myMatch = matchdf (i2, MatchConfig.AllowOverlappingCases)
//    .casedf(1 to 5, 10 to 20) {o1 := i1}
//    .casedf(7){o1 := i2}
//    .casedf(11){o1 := i2}
//    .casedf(?){o1 := i2}
//
//  val o2 = DFUInt(8) <> OUT
//  val ret = DFUInt(8).matchdf(i2)
//    .casedf(1 to 5, 10 to 20) {i1}
//    .casedf(7){75}
//    .casedf(?){88}
//  o2 <> ret
//
//  val i3 = DFEnum(Foo) <> IN init (Foo.Baz0, Foo.Baz3)
//  val o3 = DFUInt(8) <> OUT
//  val myEnumMatch = matchdf (i3)
//    .casedf(Foo.Baz0) {o3 := 1}
//    .casedf(Foo.Baz1) {o3 := 0}
//}
//
//class RTx2(width : Int)(implicit ctx : RTComponent.Context) extends RTComponent {
//  final val I = DFUInt(width) <> IN
//  final val O = DFUInt(width) <> OUT
//  final override protected val blackBoxFunctions = Map(O -> BlackBoxFunction(O)(I, I)((l, r) => l + r))
//}
//
//trait Comp extends DFComponent[Comp] {
//  val i = DFUInt(8) <> IN
//  val o = DFUInt(8) <> OUT
//  final override protected val blackBoxFunctions = Map(o -> BlackBoxFunction(o)(i, i)((l, r) => l + r))
//}
//object Comp {
//  implicit val ev : Comp => Unit = ifc => {
//    import ifc._
//    val rt = new RTx2(8)
//    rt.I <> i
//    rt.O <> o
//  }
//}
//
//trait IODesignConn2 extends DFDesign{
//  val i = DFUInt(8) <> IN init 1
//  val o = DFUInt(8) <> OUT
//
//  val io = new Comp {}
//  i <> io.i
//  o <> io.o
//}
//
//
//trait IODesignConn3 extends DFDesign {
//  val i = DFUInt(8) <> IN init 5
//  val o = DFUInt(8) <> OUT
//  val plusOne = i + 0
//  val timesFive = plusOne * 5
//  o <> timesFive
//}
//
//class RTAdd(aWidth : Int, bWidth : Int, sWidth : Int)(implicit ctx : RTComponent.Context) extends RTComponent {
//  final val A = DFUInt(aWidth) <> IN
//  final val B = DFUInt(bWidth) <> IN
//  final val S = DFUInt(sWidth) <> OUT
//  final override protected val blackBoxFunctions = Map(S -> BlackBoxFunction(S)(A, B)((l, r) => l + r))
//}
//
//
//trait IODesignConn4 extends DFDesign {
//  val i = DFUInt(8) <> IN init(1, 2, 3, 4, Bubble)
//  val o = DFUInt(8) <> OUT
////  val temp = i.prev()
//  o <> i.prev.prev.prev.prev
//}
//
//trait IODesignConn5 extends DFDesign {
//  val myloop = for (i <- 0 to 2) {
//    val i = DFUInt(8) <> IN init(1, 2, 3, 4, Bubble)
//    val o = DFUInt(8) <> OUT
//    o <> i.prev.prev.prev.prev
//  }
//}
//
//trait IODesignConn6 extends DFDesign {
//  val in = DFBits(7) <> IN init b"10000000"
//  val out = DFBits(9) <> OUT
//
//  out <> b"0" ## in ## b"1"
//}
//
//trait IODesignConn7 extends DFDesign {
//  val in = DFBits(8) <> IN init b"00000010"
//  val out = DFUInt(8) <> OUT
//
//  val s = in.as(DFUInt(8))
//  out <> s
//}
//
//trait ContainerConn1 extends DFDesign {
//  val i = DFUInt(8) <> IN
//  val o = DFUInt(8) <> OUT
//  val io = new IODesignConn1 {}
//  i    <> io.i //Connecting between owner input and child input
//  io.o.prev <> o    //Connecting between child output and owner output
//}
//
//trait ContainerConnLoop extends DFDesign {
//  val i = DFUInt(8) <> IN
//  val o = DFUInt(8) <> OUT
//  val io = new IODesignConn1 {}
//  io.i <> io.o
//  o <> io.o
//}
//
//trait ContainerConn3 extends DFDesign {
//  val i = DFUInt(8) <> IN
//  val o = DFUInt(8) <> OUT
//  val io1 = new IODesignConn1 {}
//  val io2 = new IODesignConn1 {}
////  val temp = DFUInt(8)
//  i     <> io1.i //Connecting between owner input and child input
////  temp := io1.o
////  temp <> io2.i //Connecting between siblings (output <> input)
//  io1.o <> io2.i
//  io2.o <> o     //Connecting between child output and owner output
//}
//
//trait ContainerConn4 extends DFDesign {
//  val i = DFUInt(7) <> IN
//  val o = DFUInt(8) <> OUT
//  val ob = DFBool() <> OUT
//  val io = new IODesignConn1 {}
//  i <> io.i
//  io.o <> o
//  true <> ob
//}
//
//object Foo extends Enum.Auto {
//  val Baz0, Baz1, Baz2, Baz3, Baz4 = Entry
//}
//
//trait IODesignConn8 extends DFDesign {
//  val i = DFEnum(Foo) <> IN// init Foo.Baz3
//  val o = DFEnum(Foo) <> OUT
//  val b = DFBool() <> OUT
//  o := i
//  val c = i != Foo.Baz0
//  c.keep
////  b := c
////  o <> i
//}
//
//trait IODesignConn9 extends DFDesign {
//  val b = DFBits(8) <> IN init (h"01", h"08")
//  val s = DFSInt(8) <> IN init (-1, -4)
//  val ishift = DFUInt(3) <> IN init(1, 4)
//  val ob = DFBits(8) <> OUT
//  ob <> (b << ishift)
//  val os = DFSInt(8) <> OUT
//  os <> (s << ishift)
//}
//
//trait IODesignConn10 extends DFDesign {
//  val i = DFBits(2) <> IN
//  val o = DFBits(2) <> OUT
//  val res = DFBits(2).selectdf(i(1))(i, b0s)
//  o <> res
////  o := oo
//}
////
////trait IODesign1 extends DFDesign {
////  val i = DFUInt(8) <> IN
////  val o = DFUInt(8) <> OUT
////  val tmp = DFUInt(8)
////  tmp := i
////  o := tmp
////}
////
////trait IODesign2 extends DFDesign {
////  val i1 = DFUInt(8) <> IN
////  val o1 = DFUInt(8) <> OUT
////  val i2 = DFUInt(8) <> IN
////  val o2 = DFUInt(8) <> OUT
////  o1 := i1
////  o2 := i2
////}
////
////
//
//trait IO extends DFDesign {
//  val i = DFUInt(8) <> IN init 5
//  val o = DFUInt(8) <> OUT
//  val c = DFUInt(8) init 0
//  c := c + i
////  c.pipe
////  c2.pipe
////  c := c2
////  c.pipe
//  val d = DFUInt(8)
//  d := (c + 2).pipe
//
//  val e = c + d
//  e.pipe
//  val f = e + c
//  o <> f
//}
//
//trait IOTest extends DFSimulator {
//  val io = new IO {}
//  val i = DFUInt(8) init 0
//  io.i <> i
//  ifdf (i == 10) {
//    sim.report(dfs"i reached 10", sim.Error)
//  }
//  i := i + 1
//}
//
//object BasicTest extends DFApp {
//  import UnofficialXilinx.FPGAs.`XC7VX485T-2FFG1761C`._
//  implicit val a = DFAnyConfiguration.foldedLatency
////  val top_ioDesignConn1 = new IODesignConn1 {}.printVHDLString
//
//  //  val top_ioDesignConn1b = new IODesignConn1b {}.printCodeString
////  val top_ioDesignConn2 = new IODesignConn2 {}
////  val top_ioDesignConn3 = new IODesignConn3 {}.printCodeString
////  val top_ioDesignConn4 = new IODesignConn4 {}
////  val top_ioDesignConn5 = new IODesignConn5 {}
////  val top_ioDesignConn6 = new IODesignConn6 {}
////    val top_ioDesignConn7 = new IODesignConn7 {}
//    val top_ioDesignConn8 = new IODesignConn8 {}.printCodeString.printVHDLString
////  val top_ioDesignConn10 = new IO {}.printCodeString//.compileToVHDL.print().toFile("test.vhd")
//
//  //  val top_containerConn1 = new ContainerConn1 {}
////  val top_containerConn3 = new ContainerConn3 {}.printVHDLString
////  val top_containerConn4 = new ContainerConn4 {}
////  val top_ioDesignIf = new IODesignIf {}.printCodeString
//
//
////  val top_ioDesignMatch = new IODesignMatch {}.printCodeString
////  val topLoop = new ContainerConnLoop {}.codeString
//
////  val top_ioDesignConn9 = new IODesignConn9 {}.printCodeString
////  import GlobalDesign._
////  println(aa.pattern(1, 2 to 20, 21 to 40))
//
//  //  trait MyDesign extends DFDesign{
////    val in = DFSInt(4) <> IN init -1
////    val out = DFSInt(8) <> OUT
////
////    out <> (b"1111", in).bits.sint
////  }
////
////  val myDesign = new MyDesign {}
////  println(myDesign.codeString)
//}
