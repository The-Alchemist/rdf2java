package de.dfki.rdf.ruleml;

import java.util.*;

public class Imp extends Clause {

  Atom head;
  Formula body;

  public void putHead(Atom head) {
    this.head = head;
  }

  public void putBody(Formula body) {
    this.body = body;
  }

  public Atom getHead() {
    return head;
  }

  public Formula getBody() {
    return body;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(head.toString());
    buffer.append(" <- ");
    buffer.append(body.toString());
    buffer.append(".\n");
    return buffer.toString();

  }

}

