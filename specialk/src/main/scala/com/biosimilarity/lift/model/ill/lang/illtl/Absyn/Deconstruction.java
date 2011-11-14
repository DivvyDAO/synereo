package com.biosimilarity.seleKt.model.ill.lang.illtl.Absyn; // Java Package generated by the BNF Converter.

public class Deconstruction extends RLLExpr {
  public final RLLExpr rllexpr_1, rllexpr_2;
  public final RLLPtrn rllptrn_;

  public Deconstruction(RLLExpr p1, RLLPtrn p2, RLLExpr p3) { rllexpr_1 = p1; rllptrn_ = p2; rllexpr_2 = p3; }

  public <R,A> R accept(com.biosimilarity.seleKt.model.ill.lang.illtl.Absyn.RLLExpr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof com.biosimilarity.seleKt.model.ill.lang.illtl.Absyn.Deconstruction) {
      com.biosimilarity.seleKt.model.ill.lang.illtl.Absyn.Deconstruction x = (com.biosimilarity.seleKt.model.ill.lang.illtl.Absyn.Deconstruction)o;
      return this.rllexpr_1.equals(x.rllexpr_1) && this.rllptrn_.equals(x.rllptrn_) && this.rllexpr_2.equals(x.rllexpr_2);
    }
    return false;
  }

  public int hashCode() {
    return 37*(37*(this.rllexpr_1.hashCode())+this.rllptrn_.hashCode())+this.rllexpr_2.hashCode();
  }


}