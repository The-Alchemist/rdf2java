package de.dfki.rdf.ruleml;

import java.util.*;

public class Or extends Formula {

  ArrayList args = new ArrayList(); // of Formula

  public void putArg(Formula formula) {
    args.add(formula);
  }

  public void putArgs(Formula formula) {
    args.add(formula);
  }

  public List getArgs() {
    return args;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("(");
    for (Iterator formulaIterator = args.iterator(); 
	 formulaIterator.hasNext();) {
      // do we need ( ) here ???
      buffer.append(((Formula)formulaIterator.next()).toString());
      if (formulaIterator.hasNext()) buffer.append("; ");
    }
    buffer.append(")");
    return buffer.toString();
  }

}

