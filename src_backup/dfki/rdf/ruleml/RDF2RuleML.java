package dfki.rdf.ruleml;

import java.util.*;
import org.w3c.rdf.model.*;
import dfki.rdf.utils.*;


public class RDF2RuleML {

  public static void main(String args[]) {
    // arg 1: rdf file

    if (args.length >= 1) {

      String rdfFilename = args[0];
      String namespace = "http://www.ruleml.org/rdf#";
      String pkg = "dfki.rdf.ruleml";
      // String namespace = args[1];
      // String pkg = args[2];

      try
      {
          Map objects = (new RDFImport(namespace, pkg)).importObjects(rdfFilename);
          ArrayList rulebases = new ArrayList();

          for (Iterator objIterator = objects.keySet().iterator();
               objIterator.hasNext();) {
            Resource resource = (Resource)objIterator.next();
            Object object = objects.get(resource);
            if (object instanceof dfki.rdf.ruleml.Rulebase) {
              rulebases.add(object);
              System.out.println(object);
            }
          }

          // export test

          String exportFilename = rdfFilename + ".exported";
          (new RDFExport(namespace)).exportObjects(rulebases, exportFilename);
          System.out.println("Exported to " + exportFilename + "\n");
          System.exit(0);
      } catch (Exception ex) {
        System.out.println("Exception occurred: " + ex.getMessage()); ex.printStackTrace(); System.exit(1);
      }

    } else {

      System.err.println("Usage: <ruleml rdf file>");

    }

  }


}


