package dfki.rdf.test.deepCopy;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class ComplexClass
  * <p>
  */
public  class ComplexClass
    extends dfki.rdf.util.THING
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot complexSingleValueSlot **/
    protected dfki.rdf.util.PropertyInfo m_complexSingleValueSlot = dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.org/rdf2java/deepCopyExample#", "complexSingleValueSlot", new Class[]{ComplexClass.class}, false );

    /** RDFS2Class: putter for slot complexSingleValueSlot **/
    public void putComplexSingleValueSlot (ComplexClass p_complexSingleValueSlot)
    {
        m_complexSingleValueSlot.putValue(p_complexSingleValueSlot);
    }
    public void putComplexSingleValueSlot (dfki.rdf.util.RDFResource p_complexSingleValueSlot)
    {
        m_complexSingleValueSlot.putValue(p_complexSingleValueSlot);
    }
    // RDFS2Class: end of putter for slot complexSingleValueSlot

    /** RDFS2Class: getter for slot complexSingleValueSlot **/
    public ComplexClass GetComplexSingleValueSlot ()
    {
        return (ComplexClass)m_complexSingleValueSlot.getValue();
    }
    public dfki.rdf.util.RDFResource getComplexSingleValueSlot ()
    {
        return (dfki.rdf.util.RDFResource)m_complexSingleValueSlot.getValue();
    }
    // RDFS2Class: end of getter for slot complexSingleValueSlot

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot simpleSingleValueSlot **/
    protected dfki.rdf.util.PropertyInfo m_simpleSingleValueSlot = dfki.rdf.util.PropertyInfo.createStringProperty( "http://dfki.org/rdf2java/deepCopyExample#", "simpleSingleValueSlot", false );

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
    /** RDFS2Class: slot simpleMultiValueSlot **/
    protected dfki.rdf.util.PropertyInfo m_simpleMultiValueSlot = dfki.rdf.util.PropertyInfo.createStringProperty( "http://dfki.org/rdf2java/deepCopyExample#", "simpleMultiValueSlot", true );

    /** RDFS2Class: putter for slot simpleMultiValueSlot **/
    public void putSimpleMultiValueSlot (String p_simpleMultiValueSlot)
    {
        m_simpleMultiValueSlot.putValue(p_simpleMultiValueSlot);
    }
    public void putSimpleMultiValueSlot (dfki.rdf.util.RDFResource p_simpleMultiValueSlot)
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
    /** RDFS2Class: slot complexMultiValueSlot **/
    protected dfki.rdf.util.PropertyInfo m_complexMultiValueSlot = dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.org/rdf2java/deepCopyExample#", "complexMultiValueSlot", new Class[]{ComplexClass.class}, true );

    /** RDFS2Class: putter for slot complexMultiValueSlot **/
    public void putComplexMultiValueSlot (ComplexClass p_complexMultiValueSlot)
    {
        m_complexMultiValueSlot.putValue(p_complexMultiValueSlot);
    }
    public void putComplexMultiValueSlot (dfki.rdf.util.RDFResource p_complexMultiValueSlot)
    {
        m_complexMultiValueSlot.putValue(p_complexMultiValueSlot);
    }
    public void putComplexMultiValueSlot (java.util.Collection p_complexMultiValueSlot)
    {
        m_complexMultiValueSlot.setValues(p_complexMultiValueSlot);
    }
    public void clearComplexMultiValueSlot ()
    {
        m_complexMultiValueSlot.clearValue();
    }
    // RDFS2Class: end of putter for slot complexMultiValueSlot

    /** RDFS2Class: getter for slot complexMultiValueSlot **/
    public java.util.Collection getComplexMultiValueSlot ()
    {
        return (java.util.Collection)m_complexMultiValueSlot.getValue();
    }
    // RDFS2Class: end of getter for slot complexMultiValueSlot

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
    public ComplexClass()
    {
        super();
        putRDFSClass( new dfki.rdf.util.RDFResource( "http://dfki.org/rdf2java/deepCopyExample#", "ComplexClass" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_complexSingleValueSlot );
        ps.addProperty( m_simpleSingleValueSlot );
        ps.addProperty( m_simpleMultiValueSlot );
        ps.addProperty( m_complexMultiValueSlot );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class ComplexClass
// EOF

