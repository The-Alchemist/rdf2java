package dfki.rdf.test.example1;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class Person
  * <p>
  */
public  class Person
    extends dfki.rdf.util.THING
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot hasParent **/
    protected dfki.rdf.util.PropertyInfo m_hasParent = dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://org.dfki/rdf2java/example1#", "hasParent", new Class[]{Person.class}, true );

    /** RDFS2Class: putter for slot hasParent **/
    public void putHasParent (Person p_hasParent)
    {
        m_hasParent.putValue(p_hasParent);
    }
    public void putHasParent (dfki.rdf.util.RDFResource p_hasParent)
    {
        m_hasParent.putValue(p_hasParent);
    }
    public void putHasParent (java.util.Collection p_hasParent)
    {
        m_hasParent.setValues(p_hasParent);
    }
    public void clearHasParent ()
    {
        m_hasParent.clearValue();
    }
    // RDFS2Class: end of putter for slot hasParent

    /** RDFS2Class: getter for slot hasParent **/
    public java.util.Collection getHasParent ()
    {
        return (java.util.Collection)m_hasParent.getValue();
    }
    // RDFS2Class: end of getter for slot hasParent

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot name **/
    protected dfki.rdf.util.PropertyInfo m_name = dfki.rdf.util.PropertyInfo.createStringProperty( "http://org.dfki/rdf2java/example1#", "name", false );

    /** RDFS2Class: putter for slot name **/
    public void putName (String p_name)
    {
        m_name.putValue(p_name);
    }
    // RDFS2Class: end of putter for slot name

    /** RDFS2Class: getter for slot name **/
    public String getName ()
    {
        return (String)m_name.getValue();
    }
    // RDFS2Class: end of getter for slot name

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
    public Person()
    {
        super();
        putRDFSClass( new dfki.rdf.util.RDFResource( "http://org.dfki/rdf2java/example1#", "Person" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_hasParent );
        ps.addProperty( m_name );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class Person
// EOF

