package de.dfki.rdf.test.nontransitive;

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
    m_mapNS2Pkg.put("http://dfki.rdf.test/nontransitive#", "dfki.rdf.test.nontransitive");
    m_rdfImport = new de.dfki.rdf.util.RDFImport(m_mapNS2Pkg);
    // m_mapPkg2NS = new HashMap();
    // m_mapPkg2NS.put("dfki.rdf.test.nontransitive", "http://dfki.rdf.test/nontransitive#");
}

//---------------------------------------------------------------------------
void go ()
{
//    readin("nontransitive");

    readin("n_00005");
    readin("n_00006");
    readin("n_00007");
    readin("n_00008");
    readin("n_00009");
    readin("n_00010");
    readin("n_00011");
}

//---------------------------------------------------------------------------
void readin (String sLocalName)
{
    String sFilename = "testdata/nontransitive/" + sLocalName + ".rdf";
    System.out.println("\n\n\n[" + sFilename + "]");

    try
    {
        m_rdfImport.reinit();
        Map mapObjects = m_rdfImport.importObjects(new java.io.FileReader(sFilename), m_kbCachedObjects);
        printoutMapValues(mapObjects);

        m_kbCachedObjects.putAll(mapObjects);
        System.out.println(m_kbCachedObjects.toString());

        m_kbCachedObjects.updateRDFResourceSlots();

        m_kbCachedObjects.putAll(mapObjects);
        System.out.println(m_kbCachedObjects.toString());
    }
    catch (Exception ex)
    {
        System.out.println("Exception in readin: " + ex.getMessage());
        ex.printStackTrace();
        System.exit(1);
    }
}

//---------------------------------------------------------------------------
void printoutMapValues (Map mapObjects)
{
    System.err.flush();
    System.out.flush();
    System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
    for (Iterator it = mapObjects.values().iterator(); it.hasNext(); )
    {
        Object obj = it.next();
        System.out.println(obj);
    }
    System.out.println("---------------------------------------------------------------------------");
}

//---------------------------------------------------------------------------
} // end of class Test

