package de.dfki.rdf.test.deepCopy;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class ComplexClass2
  * <p>
  */
public  class ComplexClass2
    extends de.dfki.rdf.util.THING
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot complexSingleValueSlot2 **/
    protected de.dfki.rdf.util.PropertyInfo m_complexSingleValueSlot2 = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://de.dfki.rdf.test/rdf2java/deepCopyExample#", "complexSingleValueSlot2", new Class[]{ComplexClass2.class}, false );

    /** RDFS2Class: putter for slot complexSingleValueSlot2 **/
    public void putComplexSingleValueSlot2 (ComplexClass2 p_complexSingleValueSlot2)
    {
        m_complexSingleValueSlot2.putValue(p_complexSingleValueSlot2);
    }
    public void putComplexSingleValueSlot2 (de.dfki.rdf.util.RDFResource p_complexSingleValueSlot2)
    {
        m_complexSingleValueSlot2.putValue(p_complexSingleValueSlot2);
    }
    // RDFS2Class: end of putter for slot complexSingleValueSlot2

    /** RDFS2Class: getter for slot complexSingleValueSlot2 **/
    public ComplexClass2 GetComplexSingleValueSlot2 ()
    {
        return (ComplexClass2)m_complexSingleValueSlot2.getValue();
    }
    public de.dfki.rdf.util.RDFResource getComplexSingleValueSlot2 ()
    {
        return (de.dfki.rdf.util.RDFResource)m_complexSingleValueSlot2.getValue();
    }
    // RDFS2Class: end of getter for slot complexSingleValueSlot2

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
    public ComplexClass2()
    {
        super();
        putRDFSClass( new de.dfki.rdf.util.RDFResource( "http://de.dfki.rdf.test/rdf2java/deepCopyExample#", "ComplexClass2" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        de.dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_complexSingleValueSlot2 );
        ps.addProperty( m_simpleSingleValueSlot );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class ComplexClass2
// EOF

