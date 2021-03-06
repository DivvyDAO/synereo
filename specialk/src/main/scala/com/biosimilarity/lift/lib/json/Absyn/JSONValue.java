package com.biosimilarity.lift.lib.json.Absyn; // Java Package generated by the BNF Converter.

public abstract class JSONValue implements java.io.Serializable {
  public abstract <R,A> R accept(JSONValue.Visitor<R,A> v, A arg);
  public interface Visitor <R,A> {
    public R visit(com.biosimilarity.lift.lib.json.Absyn.JStr p, A arg);
    public R visit(com.biosimilarity.lift.lib.json.Absyn.JNum p, A arg);
    public R visit(com.biosimilarity.lift.lib.json.Absyn.JObj p, A arg);
    public R visit(com.biosimilarity.lift.lib.json.Absyn.JArr p, A arg);
    public R visit(com.biosimilarity.lift.lib.json.Absyn.JTru p, A arg);
    public R visit(com.biosimilarity.lift.lib.json.Absyn.JFal p, A arg);
    public R visit(com.biosimilarity.lift.lib.json.Absyn.JNul p, A arg);

  }

}
