package dfki.rdf.test.assign;

// RDFS2Class: imports
import java.util.*;
// RDFS2Class: end of imports


/** RDFS2Class: class A
  * <p>
  */
public  class A
    extends dfki.rdf.util.THING
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot toB **/
    protected dfki.rdf.util.PropertyInfo m_toB = dfki.rdf.util.PropertyInfo.createInstanceProperty( "toB", new Class[]{B.class}, true );

    /** RDFS2Class: putter for slot toB **/
    public void putToB (B p_toB)
    {
        m_toB.putValue(p_toB);
    }
    public void putToB (dfki.rdf.util.RDFResource p_toB)
    {
        m_toB.putValue(p_toB);
    }
    public void putToB (java.util.Collection p_toB)
    {
        m_toB.setValues(p_toB);
    }
    public void clearToB ()
    {
        m_toB.clearValue();
    }
    // RDFS2Class: end of putter for slot toB

    /** RDFS2Class: getter for slot toB **/
    public java.util.Collection getToB ()
    {
        return (java.util.Collection)m_toB.getValue();
    }
    // RDFS2Class: end of getter for slot toB

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot subA **/
    protected dfki.rdf.util.PropertyInfo m_subA = dfki.rdf.util.PropertyInfo.createInstanceProperty( "subA", new Class[]{A.class}, true );

    /** RDFS2Class: putter for slot subA **/
    public void putSubA (A p_subA)
    {
        m_subA.putValue(p_subA);
    }
    public void putSubA (dfki.rdf.util.RDFResource p_subA)
    {
        m_subA.putValue(p_subA);
    }
    public void putSubA (java.util.Collection p_subA)
    {
        m_subA.setValues(p_subA);
    }
    public void clearSubA ()
    {
        m_subA.clearValue();
    }
    // RDFS2Class: end of putter for slot subA

    /** RDFS2Class: getter for slot subA **/
    public java.util.Collection getSubA ()
    {
        return (java.util.Collection)m_subA.getValue();
    }
    // RDFS2Class: end of getter for slot subA

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot superA **/
    protected dfki.rdf.util.PropertyInfo m_superA = dfki.rdf.util.PropertyInfo.createInstanceProperty( "superA", new Class[]{A.class}, false );

    /** RDFS2Class: putter for slot superA **/
    public void putSuperA (A p_superA)
    {
        m_superA.putValue(p_superA);
    }
    public void putSuperA (dfki.rdf.util.RDFResource p_superA)
    {
        m_superA.putValue(p_superA);
    }
    // RDFS2Class: end of putter for slot superA

    /** RDFS2Class: getter for slot superA **/
    public A GetSuperA ()
    {
        return (A)m_superA.getValue();
    }
    public dfki.rdf.util.RDFResource getSuperA ()
    {
        return (dfki.rdf.util.RDFResource)m_superA.getValue();
    }
    // RDFS2Class: end of getter for slot superA

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        super.toString(sb, sIndent);
        if (!m_toB.isEmpty()) {
            sb.append(sIndent+"-> toB:\n");
            for (Iterator it_toB = ((java.util.Collection)m_toB.getValue()).iterator(); it_toB.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.RDFResource)it_toB.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.RDFResource)it_toB.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_subA.isEmpty()) {
            sb.append(sIndent+"-> subA:\n");
            for (Iterator it_subA = ((java.util.Collection)m_subA.getValue()).iterator(); it_subA.hasNext(); ) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.RDFResource)it_subA.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.RDFResource)it_subA.next()).toString(sIndent+"       ") );
            }
        }
        if (m_superA.getValue() != null) {
            sb.append(sIndent+"-> superA:\n"+sIndent+"       "+((dfki.rdf.util.RDFResource)m_superA.getValue()).toStringShort()+"\n");
            // sb.append(sIndent+"-> superA:\n"+((dfki.rdf.util.RDFResource)m_superA.getValue()).toString(sIndent+"       "));
        }
    }
    // RDFS2Class: end of toString()-stuff

    //------------------------------------------------------------------------------
    /** RDFS2Class: sub class information **/
    public final static Class[] KNOWN_SUBCLASSES = {};

    // RDFS2Class: end of sub class information

    //------------------------------------------------------------------------------
    /** RDFS2Class: default constructor **/
    public A()
    {
        super();
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_toB );
        ps.addProperty( m_subA );
        ps.addProperty( m_superA );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class A
// EOF

