package de.dfki.rdf.test.assign;

import java.util.*;

import de.dfki.rdf.util.KnowledgeBase;

public class Test
{
//---------------------------------------------------------------------------
de.dfki.rdf.util.KnowledgeBase m_kbCachedObjects;

HashMap m_mapNS2Pkg;
de.dfki.rdf.util.RDFImport m_rdfImport;
// HashMap m_mapPkg2NS;


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
    m_mapNS2Pkg.put("http://dfki.rdf.test/assign#", "dfki.rdf.test.assign");
    m_rdfImport = new de.dfki.rdf.util.RDFImport(m_mapNS2Pkg);
    // m_mapPkg2NS = new HashMap();
    // m_mapPkg2NS.put("dfki.rdf.test.nontransitive", "http://dfki.rdf.test/nontransitive#");
}

//---------------------------------------------------------------------------
void go ()
{
    Map mapObjects = readin("data_kb");
    // printoutMapValues("data_kb", mapObjects);
    m_kbCachedObjects.putAll(mapObjects);
    System.out.println("knowledgeBase:\n" + m_kbCachedObjects);

    String sNumber = "00015";
    String sRDFFilename = "data_assign_" + sNumber;
    String sURI = "http://dfki.rdf.test/assign#assign_" + sNumber;
    Map mapObjectToAssign = readin(sRDFFilename);
    de.dfki.rdf.util.THING thingToAssign = (de.dfki.rdf.util.THING)mapObjectToAssign.get(sURI);
    System.out.println("\nthingToAssign:\n" + thingToAssign);

    System.out.println("\n\n################ thingToAssign.getAddress()=" + thingToAssign.getAddress() + "\n\n");

    m_kbCachedObjects.assign(thingToAssign);
    System.out.println("knowledgeBase (after assignment):\n" + m_kbCachedObjects);
}

//---------------------------------------------------------------------------
Map readin (String sLocalName)
{
    String sFilename = "testdata/assign/" + sLocalName + ".rdf";
    System.out.println("\n readin(" + sFilename + ")");

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

