package dfki.rdf.ruleml;


public class Var extends Term {

  String name;

  public void putName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name; // add _ or ? ???
  }

}

