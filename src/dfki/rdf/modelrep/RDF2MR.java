package dfki.rdf.modelrep;

import java.util.*;
import org.w3c.rdf.model.*;
import dfki.rdf.util.*;

import java.io.*;
import att.grappa.*;


public class RDF2MR
{
//----------------------------------------------------------------------------------------------------

public static void main(String args[])
{
    try
    {
        String path = "c:/java/rdf2java/src/dfki/rdf/modelrep/";
        String rdfFilename = path + "mr_test.rdf";
        if (args.length >= 1)
            rdfFilename = path + args[0];
        String namespace = "http://dfki.uni-kl.de/schwarz/ModelRep#";
        String pkg = "dfki.rdf.modelrep";
        // String rdfFilename = args[0];
        // String namespace = args[1];
        // String pkg = args[2];

        Map objects = (new RDFImport(namespace, pkg)).importObjects(rdfFilename);
        ArrayList alObjectsForExport = new ArrayList();
        ModelRep MR = null;

        for (Iterator objIterator = objects.keySet().iterator(); objIterator.hasNext(); )
        {
            Resource resource = (Resource)objIterator.next();
            Object object = objects.get(resource);
            if (object instanceof dfki.rdf.modelrep.ModelRep)
            {
                MR = (ModelRep)object;
                alObjectsForExport.add(object);
                System.out.println(object);
            }
        }

/*
        // export again (just for fun)
        String rdfExportFilename = path + "mr_test_export.rdf";
        System.out.println("\nexporting database to " + rdfExportFilename + "...");
        (new RDFExport(namespace)).exportObjects(alObjectsForExport, rdfExportFilename);
*/

        GrappaFrm frm = new GrappaFrm(MR);
        frm.setVisible(true);


    //    long iSleepTime = 10*1000;
    //    System.out.println("\nexiting in " + iSleepTime/1000 + "seconds...");
    //    try { Thread.sleep(iSleepTime); } catch (Exception ex) {}
    //    System.out.println("exiting.");
    //    System.exit(0);
    }
    catch (Exception ex)
    {
        System.err.println("Exception occurred: "+ex.getMessage());
        ex.printStackTrace();
    }
}


//----------------------------------------------------------------------------------------------------
}

















