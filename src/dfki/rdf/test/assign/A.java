package dfki.rdf.test.assign;

// RDFS2Class: imports
import java.util.*;
// RDFS2Class: end of imports


/** RDFS2Class: class A
  * <p>
  */
public class A
    extends dfki.rdf.util.THING
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot subA **/
    java.util.HashSet m_subA = new java.util.HashSet();

    /** RDFS2Class: putter for slot subA **/
    public void putSubA (A p_subA)
    {
        ...
        m_subA.add(p_subA);
    }
    public void putSubA (dfki.rdf.util.RDFResource p_subA)
    {
        m_subA.add(p_subA);
    }
    public void putSubA (java.util.Collection p_subA)
    {
        m_subA = new java.util.HashSet(p_subA);
    }
    public void clearSubA ()
    {
        m_subA = new java.util.HashSet();
    }
    // RDFS2Class: end of putter for slot subA

    /** RDFS2Class: getter for slot subA **/
    public java.util.Collection getSubA ()
    {
        return m_subA;
    }
    // RDFS2Class: end of getter for slot subA

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot toB **/
    java.util.HashSet m_toB = new java.util.HashSet();

    /** RDFS2Class: putter for slot toB **/
    public void putToB (B p_toB)
    {
        m_toB.add(p_toB);
    }
    public void putToB (dfki.rdf.util.RDFResource p_toB)
    {
        m_toB.add(p_toB);
    }
    public void putToB (java.util.Collection p_toB)
    {
        m_toB = new java.util.HashSet(p_toB);
    }
    public void clearToB ()
    {
        m_toB = new java.util.HashSet();
    }
    // RDFS2Class: end of putter for slot toB

    /** RDFS2Class: getter for slot toB **/
    public java.util.Collection getToB ()
    {
        return m_toB;
    }
    // RDFS2Class: end of getter for slot toB

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot superA **/
    dfki.rdf.util.RDFResource m_superA;

    /** RDFS2Class: putter for slot superA **/
    public void putSuperA (A p_superA)
    {
        m_superA = p_superA;
    }
    public void putSuperA (dfki.rdf.util.RDFResource p_superA)
    {
        m_superA = p_superA;
    }
    // RDFS2Class: end of putter for slot superA

    /** RDFS2Class: getter for slot superA **/
    public A GetSuperA ()
    {
        return (A)m_superA;
    }
    public dfki.rdf.util.RDFResource getSuperA ()
    {
        return m_superA;
    }
    // RDFS2Class: end of getter for slot superA

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        super.toString(sb, sIndent);
        if (!m_subA.isEmpty()) {
            sb.append(sIndent+"-> subA:\n");
            for (Iterator it_subA = m_subA.iterator(); it_subA.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.RDFResource)it_subA.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.RDFResource)it_subA.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_toB.isEmpty()) {
            sb.append(sIndent+"-> toB:\n");
            for (Iterator it_toB = m_toB.iterator(); it_toB.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.RDFResource)it_toB.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.RDFResource)it_toB.next()).toString(sIndent+"       ") );
            }
        }
        if (m_superA != null) {
            sb.append(sIndent+"-> superA:\n"+sIndent+"       "+m_superA.toStringShort()+"\n");
            // sb.append(sIndent+"-> superA:\n"+m_superA.toString(sIndent+"       "));
        }
    }
    // RDFS2Class: end of toString()-stuff

}
// RDFS2Class: end of class A
// EOF

