package dfki.rdf.test.nontransitive;

// RDFS2Class: imports
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Iterator;
// RDFS2Class: end of imports

import java.util.Map;


/** RDFS2Class: class A
  * <p>
  */
public class A
    extends dfki.rdf.util.THING
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot toA **/
    java.util.HashSet m_toA = new java.util.HashSet();

    /** RDFS2Class: putter for slot toA **/
    public void putToA (A p_toA)
    {
        m_toA.add(p_toA);
    }
    public void putToA (java.util.Collection p_toA)
    {
        m_toA = new java.util.HashSet(p_toA);
    }
    // RDFS2Class: end of putter for slot toA

    /** RDFS2Class: getter for slot toA **/
    public java.util.Collection getToA ()
    {
        return m_toA;
    }
    // RDFS2Class: end of getter for slot toA

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot toB **/
    java.util.HashSet m_toB = new java.util.HashSet();

    /** RDFS2Class: putter for slot toB **/
    public void putToB (B p_toB)
    {
        m_toB.add(p_toB);
    }
    public void putToB (java.util.Collection p_toB)
    {
        m_toB = new java.util.HashSet(p_toB);
    }
    // RDFS2Class: end of putter for slot toB

    /** RDFS2Class: getter for slot toB **/
    public java.util.Collection getToB ()
    {
        return m_toB;
    }
    // RDFS2Class: end of getter for slot toB

    //------------------------------------------------------------------------------
    java.util.HashSet m_toA_asURI = new java.util.HashSet();

    public void putToA (dfki.rdf.utils.RDFResource p_toA_asURI)
    {
        m_toA_asURI.add(p_toA_asURI);
    }

    java.util.HashSet m_toB_asURI = new java.util.HashSet();

    public void putToB (dfki.rdf.utils.RDFResource p_toB_asURI)
    {
        m_toB_asURI.add(p_toB_asURI);
    }

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        super.toString(sb, sIndent);
        if (!m_toA.isEmpty()) {
            sb.append(sIndent+"-> toA:\n");
            Iterator it_toA = m_toA.iterator();
            while (it_toA.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_toA.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_toA.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_toA_asURI.isEmpty()) {
            sb.append(sIndent+"-> toA(URI):\n");
            Iterator it_toA = m_toA_asURI.iterator();
            while (it_toA.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.utils.RDFResource)it_toA.next()).toStringShort() + "\n" );
            }
        }
        if (!m_toB.isEmpty()) {
            sb.append(sIndent+"-> toB:\n");
            Iterator it_toB = m_toB.iterator();
            while (it_toB.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_toB.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_toB.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_toB_asURI.isEmpty()) {
            sb.append(sIndent+"-> toB(URI):\n");
            Iterator it_toB = m_toB_asURI.iterator();
            while (it_toB.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.utils.RDFResource)it_toB.next()).toStringShort() + "\n" );
            }
        }
    }
    // RDFS2Class: end of toString()-stuff

    //----------------------------------------------------------------------------------------------------
    public void updateRDFResourceSlots (dfki.rdf.utils.KnowledgeBase kbCachedObjects)
    {
        // Slot toA
        for (Iterator it = m_toA_asURI.iterator(); it.hasNext(); )
        {
            dfki.rdf.utils.RDFResource res = (dfki.rdf.utils.RDFResource)it.next();
            Object obj = kbCachedObjects.get(res);
            if (obj != null && !(obj instanceof dfki.rdf.utils.RDFResource) && (obj instanceof dfki.rdf.util.THING))
            {
                it.remove();
                putToA( (A)obj );
            }
        }
        // Slot toB
        for (Iterator it = m_toB_asURI.iterator(); it.hasNext(); )
        {
            dfki.rdf.utils.RDFResource res = (dfki.rdf.utils.RDFResource)it.next();
            Object obj = kbCachedObjects.get(res);
            if (obj != null && !(obj instanceof dfki.rdf.utils.RDFResource) && (obj instanceof dfki.rdf.util.THING))
            {
                it.remove();
                putToB( (B)obj );
            }
        }
    }

    //----------------------------------------------------------------------------------------------------
}
// RDFS2Class: end of class A
// EOF

