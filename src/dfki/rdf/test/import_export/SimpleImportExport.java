package dfki.rdf.test.import_export;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import dfki.rdf.util.RDFImport;
import dfki.rdf.util.RDFExport;

import org.dfki.rdf2java.example1.Person;


public class SimpleImportExport
{
    public static void main( String[] args )
    {
        try
        {
            final String NAMESPACE_1 = "http://org.dfki/rdf2java/example1#";
            final String PACKAGE_1   = "org.dfki.rdf2java.example1";

            Map mapNamespace2Package = new HashMap();
            mapNamespace2Package.put( NAMESPACE_1, PACKAGE_1 );
            RDFImport rdfImport = new RDFImport( mapNamespace2Package );

            // 1. import from RDF file "example1.rdf"
            Map mapObjects = rdfImport.importObjects( "testdata/example/example1.rdf" );
            printout( mapObjects );

            // 2. make some changes (or additions)
            //    e.g. add an object for "Lisa Simpson"
            final String NAMESPACE_FOR_NEW_INSTANCES = NAMESPACE_1;
            Person lisa = new Person();
            lisa.makeNewURI( NAMESPACE_FOR_NEW_INSTANCES );
            lisa.putName( "Lisa Simpson" );
            // get object for "Homer"; you must know Homer's URI: "http://...#example1_00006
            Person homer = (Person)mapObjects.get( "http://org.dfki/rdf2java/example1#example1_00006" );
            lisa.putHasParent( homer );
            // add lisa object to mapObjects (so she can be exported in step 3 below)
            mapObjects.put( lisa.getURI(), lisa );
            System.out.println( "\n\n----------\nadded lisa\n----------\n" );
            printout( mapObjects );

            // 3. export to RDF file "example1_lisa.rdf"
            Map mapPackage2Namespace = new HashMap();
            mapPackage2Namespace.put( PACKAGE_1, NAMESPACE_1 );
            RDFExport rdfExport = new RDFExport( NAMESPACE_FOR_NEW_INSTANCES, mapPackage2Namespace );
            rdfExport.exportObjects( mapObjects.values(), "testdata/example/example1_lisa.rdf" );
        }
        catch( Exception ex )
        {
            System.err.println( ex );
            ex.printStackTrace( System.err );
        }
    }

    public static void printout( Map mapObjects )   throws org.w3c.rdf.model.ModelException
    {
        for( Iterator it = mapObjects.keySet().iterator(); it.hasNext(); )
        {
            String sURI = (String)it.next();
            Object obj = mapObjects.get( sURI );
            System.out.println( "\n########## " + sURI + " ##########\n" + obj );
        }
    }

} // end of class SimpleImportExport

