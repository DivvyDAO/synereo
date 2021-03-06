package com.biosimilarity.seleKt.model.ill.lang.illtl.Absyn; // Java Package generated by the BNF Converter.

public class IntegerLiteral extends ValueExpr {
  public final Integer integer_;

  public IntegerLiteral(Integer p1) { integer_ = p1; }

  public <R,A> R accept(com.biosimilarity.seleKt.model.ill.lang.illtl.Absyn.ValueExpr.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof com.biosimilarity.seleKt.model.ill.lang.illtl.Absyn.IntegerLiteral) {
      com.biosimilarity.seleKt.model.ill.lang.illtl.Absyn.IntegerLiteral x = (com.biosimilarity.seleKt.model.ill.lang.illtl.Absyn.IntegerLiteral)o;
      return this.integer_.equals(x.integer_);
    }
    return false;
  }

  public int hashCode() {
    return this.integer_.hashCode();
  }


}
