package dfki.rdf.test.nontransitive;

// RDFS2Class: imports
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Iterator;
// RDFS2Class: end of imports

import java.util.Map;


/** RDFS2Class: class B
  * <p>
  */
public class B
    extends dfki.rdf.util.THING
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot toB **/
    java.util.HashSet m_toB = new java.util.HashSet();
    java.util.HashSet m_toB__asURI = new java.util.HashSet();

    /** RDFS2Class: putter for slot toB **/
    public void putToB (B p_toB)
    {
        m_toB.add(p_toB);
    }
    public void putToB (java.util.Collection p_toB)
    {
        m_toB = new java.util.HashSet(p_toB);
    }
    public void putToB (dfki.rdf.util.RDFResource p_toB)
    {
        m_toB__asURI.add(p_toB);
    }
    // RDFS2Class: end of putter for slot toB

    /** RDFS2Class: getter for slot toB **/
    public java.util.Collection getToB ()
    {
        return m_toB;
    }
    public java.util.Collection GetToB__asURI ()
    {
        return m_toB__asURI;
    }
    // RDFS2Class: end of getter for slot toB

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot toA **/
    java.util.HashSet m_toA = new java.util.HashSet();
    java.util.HashSet m_toA__asURI = new java.util.HashSet();

    /** RDFS2Class: putter for slot toA **/
    public void putToA (A p_toA)
    {
        m_toA.add(p_toA);
    }
    public void putToA (java.util.Collection p_toA)
    {
        m_toA = new java.util.HashSet(p_toA);
    }
    public void putToA (dfki.rdf.util.RDFResource p_toA)
    {
        m_toA__asURI.add(p_toA);
    }
    // RDFS2Class: end of putter for slot toA

    /** RDFS2Class: getter for slot toA **/
    public java.util.Collection getToA ()
    {
        return m_toA;
    }
    public java.util.Collection GetToA__asURI ()
    {
        return m_toA__asURI;
    }
    // RDFS2Class: end of getter for slot toA

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        super.toString(sb, sIndent);
        if (!m_toB.isEmpty()) {
            sb.append(sIndent+"-> toB:\n");
            for (Iterator it_toB = m_toB.iterator(); it_toB.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_toB.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_toB.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_toB__asURI.isEmpty()) {
            sb.append(sIndent+"-> toB(URI):\n");
            for (Iterator it_toB__asURI = m_toB__asURI.iterator(); it_toB__asURI.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.RDFResource)it_toB__asURI.next()).toStringShort() + "\n" );
            }
        }
        if (!m_toA.isEmpty()) {
            sb.append(sIndent+"-> toA:\n");
            for (Iterator it_toA = m_toA.iterator(); it_toA.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_toA.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_toA.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_toA__asURI.isEmpty()) {
            sb.append(sIndent+"-> toA(URI):\n");
            for (Iterator it_toA__asURI = m_toA__asURI.iterator(); it_toA__asURI.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.RDFResource)it_toA__asURI.next()).toStringShort() + "\n" );
            }
        }
    }
    // RDFS2Class: end of toString()-stuff
}
// RDFS2Class: end of class B
// EOF

