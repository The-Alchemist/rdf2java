package de.dfki.rdf.ruleml;

import java.util.*;

public class Atom extends Formula {

  String rel;
  ArrayList args = new ArrayList(); // of Term

  public void putRel(String rel) {
    this.rel = rel;
  }

  public void putArg(Term term) {
    args.add(term);
  }

  public void putArgs(Term term) {
    args.add(term);
  }

  public String getRel() {
    return rel;
  }

  public List getArgs() {
    return args;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(rel);
    buffer.append("(");
    for (Iterator termIterator = args.iterator(); termIterator.hasNext();) {
      buffer.append(((Term)termIterator.next()).toString());
      if (termIterator.hasNext()) buffer.append(", ");
    }
    buffer.append(")");
    return buffer.toString();
  }

}

