package de.dfki.rdf.test.assign;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class A
  * <p>
  */
public  class A
    extends de.dfki.rdf.util.THING
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot toB **/
    protected de.dfki.rdf.util.PropertyInfo m_toB = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.rdf.test/assign#", "toB", new Class[]{B.class}, true );

    /** RDFS2Class: putter for slot toB **/
    public void putToB (B p_toB)
    {
        m_toB.putValue(p_toB);
    }
    public void putToB (de.dfki.rdf.util.RDFResource p_toB)
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
    /** RDFS2Class: slot superA **/
    protected de.dfki.rdf.util.PropertyInfo m_superA = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.rdf.test/assign#", "superA", new Class[]{A.class}, false );

    /** RDFS2Class: putter for slot superA **/
    public void putSuperA (A p_superA)
    {
        m_superA.putValue(p_superA);
    }
    public void putSuperA (de.dfki.rdf.util.RDFResource p_superA)
    {
        m_superA.putValue(p_superA);
    }
    // RDFS2Class: end of putter for slot superA

    /** RDFS2Class: getter for slot superA **/
    public A GetSuperA ()
    {
        return (A)m_superA.getValue();
    }
    public de.dfki.rdf.util.RDFResource getSuperA ()
    {
        return (de.dfki.rdf.util.RDFResource)m_superA.getValue();
    }
    // RDFS2Class: end of getter for slot superA

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot subA **/
    protected de.dfki.rdf.util.PropertyInfo m_subA = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.rdf.test/assign#", "subA", new Class[]{A.class}, true );

    /** RDFS2Class: putter for slot subA **/
    public void putSubA (A p_subA)
    {
        m_subA.putValue(p_subA);
    }
    public void putSubA (de.dfki.rdf.util.RDFResource p_subA)
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
    /** RDFS2Class: toString()-stuff **/
    public String toString()
    {
        return toStringPacked();
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
        putRDFSClass( new de.dfki.rdf.util.RDFResource( "http://dfki.rdf.test/assign#", "A" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        de.dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_toB );
        ps.addProperty( m_superA );
        ps.addProperty( m_subA );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class A
// EOF

