package de.dfki.rdf.ruleml;


public class Fact extends Clause {

  Atom head;

  public void putHead(Atom head) {
    this.head = head;
  }

  public Atom getHead() {
    return head;
  }

  public String toString() {
    return head.toString() + ".\n";
  }

}

