package de.dfki.rdf.ruleml;

import java.util.*;

public class Cterm extends Term { // structure

  String ctor;
  ArrayList args = new ArrayList(); // of Term

  public void putCtor(String ctor) {
    this.ctor = ctor;
  }

  public void putArg(Term term) {
    args.add(term);
  }

  public void putArgs(Term term) {
    args.add(term);
  }

  public String getCtor() {
    return ctor;
  }

  public List getArgs() {
    return args;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(ctor);
    buffer.append("["); // or "(" ...
    for (Iterator termIterator = args.iterator(); termIterator.hasNext();) {
      buffer.append(((Term)termIterator.next()).toString());
      if (termIterator.hasNext()) buffer.append(", ");
    }
    buffer.append("]"); // or ")" ...
    return buffer.toString();
  }

}

