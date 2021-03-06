package DFiant
package compiler

import DFDesign.DB.Patch
import scala.annotation.tailrec
final class FixAnonymous[D <: DFDesign](c : IRCompilation[D]) {
  private val designDB = c.db
  import designDB.__getset
  def dropUnreferenced : IRCompilation[D] = {
    val patchList = designDB.members.collect {
      case m @ DFAny.NewVar() if !designDB.memberTable.contains(m) && m.externalInit.isEmpty =>
        m -> Patch.Remove
    }
    c.newStage(designDB.patch(patchList))
  }
  def fixAnonymous : IRCompilation[D] = {
    val anonymizeList = designDB.designMemberList.flatMap {
      case (block, members) =>
        //We first filter to scan only members that
        //can be anonymous, are not already anonymous AND are not forcibly named (via .setName)
        members.filter {
          case m : DFAny.CanBeAnonymous => !m.isAnonymous && !m.isNameForced
          case _ => false
        }.groupBy(m => m.tags.meta.namePosition).flatMap {
          //In case an anonymous member got a name from its owner. For example:
          //val ret = DFBits(8).ifdf(cond) {
          //  i & i
          //}
          //The `i & i` function would also get the `ret` name just as the if block itself
          case (pos, gm) if (pos == block.tags.meta.namePosition) =>
            gm
          case (_, gm) if (gm.length > 1) =>
            //In case an anonymous member was used as an argument to an owner. For example:
            //val ret = DFBits(8).ifdf(i & i) {
            //}
            //The `i & i` function would also get the `ret` name just as the if block itself
            if (gm.collectFirst{case x : DFBlock => x}.isDefined)
              gm//.collect {case a : DFAny.CanBeAnonymous => a}
            //In case an anonymous member inside a composition, we anonymize all but the last. For example:
            //val ret = i & i | i
            //Only the final 'Or' operation would be considered for the name `ret`
            else gm.dropRight(1)
          //annoymizing the net for cases like:
          //val x = DFUInt(8) := 0
          case (_, (net : DFNet) :: Nil) => List(net)
          case (_, (net : DFDesign.Control) :: Nil) => List(net)
          case _ => List()
        }
    }
    val patchList = anonymizeList.map(a => a -> Patch.Replace(a.anonymize, Patch.Replace.Config.FullReplacement))
    c.newStage(designDB.patch(patchList))
  }
}