package dfki.rdf.ruleml;


public class Ind extends Term {

  String name;

  public void putName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name; // add '' if constant contains special characters!
  }

}

