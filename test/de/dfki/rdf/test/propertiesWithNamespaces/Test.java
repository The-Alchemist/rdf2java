package de.dfki.rdf.test.propertiesWithNamespaces;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.dfki.rdf.util.KnowledgeBase;
import de.dfki.rdf.util.RDFExport;
import de.dfki.rdf.util.RDFImport;
import de.dfki.rdf.util.THING;

public class Test
{
//---------------------------------------------------------------------------
de.dfki.rdf.util.KnowledgeBase m_kbCachedObjects;

HashMap m_mapNS2Pkg;
HashMap m_mapPkg2NS;
RDFImport m_rdfImport;
RDFExport m_rdfExport;


//---------------------------------------------------------------------------
public static void main (String[] args)
{
    Test test1 = new Test();
    test1.go();
}

//---------------------------------------------------------------------------
Test ()
{
    m_kbCachedObjects = new KnowledgeBase();

    m_mapNS2Pkg = new HashMap();
    m_mapNS2Pkg.put("http://dfki.rdf.test/propertiesWithNamespaces/aaa#", "dfki.rdf.test.propertiesWithNamespaces.aaa");
    m_mapNS2Pkg.put("http://dfki.rdf.test/propertiesWithNamespaces/bbb#", "dfki.rdf.test.propertiesWithNamespaces.bbb");
    m_mapNS2Pkg.put("http://dfki.rdf.test/propertiesWithNamespaces/ccc#", "dfki.rdf.test.propertiesWithNamespaces.ccc");

    m_mapPkg2NS = new HashMap();
    m_mapPkg2NS.put("dfki.rdf.test.propertiesWithNamespaces.aaa", "http://dfki.rdf.test/propertiesWithNamespaces/aaa#");
    m_mapPkg2NS.put("dfki.rdf.test.propertiesWithNamespaces.bbb", "http://dfki.rdf.test/propertiesWithNamespaces/bbb#");
    m_mapPkg2NS.put("dfki.rdf.test.propertiesWithNamespaces.ccc", "http://dfki.rdf.test/propertiesWithNamespaces/ccc#");

    m_rdfImport = new RDFImport( m_mapNS2Pkg );
    m_rdfExport = new RDFExport( "http://dfki.rdf.test/propertiesWithNamespaces#", m_mapPkg2NS );
}

//---------------------------------------------------------------------------
void go ()
{
    Map mapObjects = readin();
    // printoutMapValues("data_kb", mapObjects);
    m_kbCachedObjects.putAll(mapObjects);
    System.out.println("knowledgeBase:\n" + m_kbCachedObjects);
    
    THING thing = (THING)m_kbCachedObjects.get( "http://dfki.rdf.test/propertiesWithNamespaces#propertiesWithNamespaces_00021" );
    System.out.println( "thing 'A' as RDF:\n" + thing.toStringAsRdf() );
    
    writeout();
}

//---------------------------------------------------------------------------
Map readin ()
{
    String sFilename = "testdata/propertiesWithNamespaces/propertiesWithNamespaces.rdf";
    System.out.println("\nreadin(" + sFilename + ")");

    try
    {
        m_rdfImport.reinit();
        Map mapObjects = m_rdfImport.importObjects(new java.io.FileReader(sFilename), m_kbCachedObjects);
        return mapObjects;
    }
    catch (Exception ex)
    {
        System.out.println("Exception in readin: " + ex.getMessage());
        ex.printStackTrace();
        System.exit(1);
        return null;
    }
}

//---------------------------------------------------------------------------
void writeout ()
{
    String sFilename = "testdata/propertiesWithNamespaces/propertiesWithNamespaces.rdf.out.rdf";
    System.out.println("\nwriteout(" + sFilename + ")");

    try
    {
        m_rdfExport.exportObjects( m_kbCachedObjects.values(), sFilename );
    }
    catch (Exception ex)
    {
        System.out.println("Exception in writeout: " + ex.getMessage());
        ex.printStackTrace();
        System.exit(1);
    }
}

//---------------------------------------------------------------------------
void printoutMapValues (String str, Map mapObjects)
{
    System.err.flush();
    System.out.flush();
    System.out.println("\n" + str + "\nvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
    for (Iterator it = mapObjects.values().iterator(); it.hasNext(); )
    {
        Object obj = it.next();
        System.out.println(obj);
    }
    System.out.println("---------------------------------------------------------------------------\n");
}

//---------------------------------------------------------------------------
} // end of class Test

