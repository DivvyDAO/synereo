package com.biosimilarity.lift.lib.term.Prolog.Absyn; // Java Package generated by the BNF Converter.

public class TAtom extends Term {
  public final Atom atom_;

  public TAtom(Atom p1) { atom_ = p1; }

  public <R,A> R accept(com.biosimilarity.lift.lib.term.Prolog.Absyn.Term.Visitor<R,A> v, A arg) { return v.visit(this, arg); }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof com.biosimilarity.lift.lib.term.Prolog.Absyn.TAtom) {
      com.biosimilarity.lift.lib.term.Prolog.Absyn.TAtom x = (com.biosimilarity.lift.lib.term.Prolog.Absyn.TAtom)o;
      return this.atom_.equals(x.atom_);
    }
    return false;
  }

  public int hashCode() {
    return this.atom_.hashCode();
  }


}
