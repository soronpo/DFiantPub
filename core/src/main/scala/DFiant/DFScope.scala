package DFiant

import internals._
class DFScope(val customName : Option[String] = None, val nameFlatten : DFOwner.NameFlatten = DFOwner.NameFlatten.UnderscoreSuffix)(implicit ctx : DFBlock.Context) extends DFOwner.Container {
  type Owner = DFScope.Owner
  final protected implicit val __lateConstructionConfig : LateConstructionConfig = LateConstructionConfig.Force(false)
  private[DFiant] final val __ctx : DFBlock.Context = ctx
  private[DFiant] final val owner : Owner = {
    val o = DFScope.Owner(this)(nameFlatten)(ctx)
    val namedOwner = customName match {
      case Some(name) => o.setName(name)
      case None => o
    }
    namedOwner
  }
  protected[DFiant] final implicit val __dir : DFDir = ctx.dir
  protected[DFiant] final implicit lazy val __db : DFDesign.DB.Mutable = ctx.db
  override lazy val typeName : String = __ctx.symbol.value
}

object DFScope {
  final case class Owner(
    nameFlatten : DFOwner.NameFlatten, ownerRef : DFOwner.Ref, tags : DFMember.Tags
  ) extends DFOwner.NameFlattenOwner {
    protected[DFiant] def =~(that : DFMember)(implicit getSet : MemberGetSet) : Boolean = that match {
      case Owner(_, _, tags) => this.tags =~ tags //Deliberately ignoring nameFlatten. Only the final name (in tags) matters.
      case _ => false
    }

    def setTags(tagsFunc : DFMember.Tags => DFMember.Tags)(
      implicit getSet : MemberGetSet
    ) : DFMember = getSet.set(this)(m => m.copy(tags = tagsFunc(m.tags)))

    override lazy val typeName : String = "DFScope.Owner"
  }
  object Owner {
    def apply(container : DFOwner.Container)(nameFlatten: DFOwner.NameFlatten)(
      implicit ctx : DFMember.Context
    ) : Owner = ctx.db.addContainerOwner(container, Owner(nameFlatten, ctx.owner, ctx.meta))
  }
}

object defdf {
  def apply[R](block : => R)(implicit meta : Meta, ctx : DFBlock.Context) : R = {
    new DFScope { //(Some(meta.name))
      ctx.db.OwnershipContext.injectContainer(this)
      val ret = block
      ctx.db.OwnershipContext.clearInjectedContainer()
    }.ret

//    scope.ret match {
//      case dfVal : DFAny =>
//        DFAny.Value(dfVal.setTags(_.copy(meta = ctx.meta)).asInstanceOf[DFAny.Member]).asInstanceOf[R]
//      case x => x
//    }
  }
}