package de.dfki.rdf.test.nontransitive;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports

import java.util.Map;


/** RDFS2Class: class A
  * <p>
  */
public  class A
    extends de.dfki.rdf.util.THING
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot toA **/
    protected de.dfki.rdf.util.PropertyInfo m_toA = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.rdf.test/nontransitive#", "toA", new Class[]{A.class}, true );

    /** RDFS2Class: putter for slot toA **/
    public void putToA (A p_toA)
    {
        m_toA.putValue(p_toA);
    }
    public void putToA (de.dfki.rdf.util.RDFResource p_toA)
    {
        m_toA.putValue(p_toA);
    }
    public void putToA (java.util.Collection p_toA)
    {
        m_toA.setValues(p_toA);
    }
    public void clearToA ()
    {
        m_toA.clearValue();
    }
    // RDFS2Class: end of putter for slot toA

    /** RDFS2Class: getter for slot toA **/
    public java.util.Collection getToA ()
    {
        return (java.util.Collection)m_toA.getValue();
    }
    // RDFS2Class: end of getter for slot toA

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot toB **/
    protected de.dfki.rdf.util.PropertyInfo m_toB = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.rdf.test/nontransitive#", "toB", new Class[]{B.class}, true );

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
    /** RDFS2Class: slot toTheA **/
    protected de.dfki.rdf.util.PropertyInfo m_toTheA = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.rdf.test/nontransitive#", "toTheA", new Class[]{A.class}, false );

    /** RDFS2Class: putter for slot toTheA **/
    public void putToTheA (A p_toTheA)
    {
        m_toTheA.putValue(p_toTheA);
    }
    public void putToTheA (de.dfki.rdf.util.RDFResource p_toTheA)
    {
        m_toTheA.putValue(p_toTheA);
    }
    // RDFS2Class: end of putter for slot toTheA

    /** RDFS2Class: getter for slot toTheA **/
    public A GetToTheA ()
    {
        return (A)m_toTheA.getValue();
    }
    public de.dfki.rdf.util.RDFResource getToTheA ()
    {
        return (de.dfki.rdf.util.RDFResource)m_toTheA.getValue();
    }
    // RDFS2Class: end of getter for slot toTheA

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
        putRDFSClass( new de.dfki.rdf.util.RDFResource( "http://dfki.rdf.test/nontransitive#", "A" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        de.dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_toA );
        ps.addProperty( m_toB );
        ps.addProperty( m_toTheA );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class A
// EOF

