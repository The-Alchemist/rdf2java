package dfki.rdf.ruleml;

import java.util.*;

public class Rulebase {

  ArrayList clauses = new ArrayList(); // of Clause

  public void putClause(Clause clause) {
    clauses.add(clause);
  }

  public void putClauses(Clause clause) {
    clauses.add(clause);
  }

  public List getClauses() {
    return clauses;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    for (Iterator clauseIterator = clauses.iterator();
	 clauseIterator.hasNext();)
      buffer.append(((Clause)clauseIterator.next()).toString());
    return buffer.toString();
  }

}

