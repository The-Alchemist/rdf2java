package de.dfki.rdf.test.deepCopy;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class SimpleClass
  * <p>
  */
public  class SimpleClass
    extends de.dfki.rdf.util.THING
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot simpleMultiValueSlot **/
    protected de.dfki.rdf.util.PropertyInfo m_simpleMultiValueSlot = de.dfki.rdf.util.PropertyInfo.createStringProperty( "http://de.dfki.rdf.test/rdf2java/deepCopyExample#", "simpleMultiValueSlot", true );

    /** RDFS2Class: putter for slot simpleMultiValueSlot **/
    public void putSimpleMultiValueSlot (String p_simpleMultiValueSlot)
    {
        m_simpleMultiValueSlot.putValue(p_simpleMultiValueSlot);
    }
    public void putSimpleMultiValueSlot (de.dfki.rdf.util.RDFResource p_simpleMultiValueSlot)
    {
        m_simpleMultiValueSlot.putValue(p_simpleMultiValueSlot);
    }
    public void putSimpleMultiValueSlot (java.util.Collection p_simpleMultiValueSlot)
    {
        m_simpleMultiValueSlot.setValues(p_simpleMultiValueSlot);
    }
    public void clearSimpleMultiValueSlot ()
    {
        m_simpleMultiValueSlot.clearValue();
    }
    // RDFS2Class: end of putter for slot simpleMultiValueSlot

    /** RDFS2Class: getter for slot simpleMultiValueSlot **/
    public java.util.Collection getSimpleMultiValueSlot ()
    {
        return (java.util.Collection)m_simpleMultiValueSlot.getValue();
    }
    // RDFS2Class: end of getter for slot simpleMultiValueSlot

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot simpleSingleValueSlot **/
    protected de.dfki.rdf.util.PropertyInfo m_simpleSingleValueSlot = de.dfki.rdf.util.PropertyInfo.createStringProperty( "http://de.dfki.rdf.test/rdf2java/deepCopyExample#", "simpleSingleValueSlot", false );

    /** RDFS2Class: putter for slot simpleSingleValueSlot **/
    public void putSimpleSingleValueSlot (String p_simpleSingleValueSlot)
    {
        m_simpleSingleValueSlot.putValue(p_simpleSingleValueSlot);
    }
    // RDFS2Class: end of putter for slot simpleSingleValueSlot

    /** RDFS2Class: getter for slot simpleSingleValueSlot **/
    public String getSimpleSingleValueSlot ()
    {
        return (String)m_simpleSingleValueSlot.getValue();
    }
    // RDFS2Class: end of getter for slot simpleSingleValueSlot

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
    public SimpleClass()
    {
        super();
        putRDFSClass( new de.dfki.rdf.util.RDFResource( "http://de.dfki.rdf.test/rdf2java/deepCopyExample#", "SimpleClass" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        de.dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_simpleMultiValueSlot );
        ps.addProperty( m_simpleSingleValueSlot );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class SimpleClass
// EOF

