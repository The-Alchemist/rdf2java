package de.dfki.rdf.test.datetime;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class Human
  * <p>
  */
public  class Human
    extends de.dfki.rdf.util.THING
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot name **/
    protected de.dfki.rdf.util.PropertyInfo m_name = de.dfki.rdf.util.PropertyInfo.createStringProperty( "http://de.dfki.rdf.test/datetime#", "name", false );

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
    /** RDFS2Class: slot dateOfBirth **/
    protected de.dfki.rdf.util.PropertyInfo m_dateOfBirth = de.dfki.rdf.util.PropertyInfo.createStringProperty( "http://de.dfki.rdf.test/datetime#", "dateOfBirth", false );

    /** RDFS2Class: putter for slot dateOfBirth **/
    public void putDateOfBirth (Date p_dateOfBirth)
    {
        m_dateOfBirth.putValue(de.dfki.rdf.util.RDFTool.dateTime2String(p_dateOfBirth));
    }
    // RDFS2Class: end of putter for slot dateOfBirth

    /** RDFS2Class: getter for slot dateOfBirth **/
    public Date GetDateOfBirth ()
    {
        return de.dfki.rdf.util.RDFTool.string2Date((String)m_dateOfBirth.getValue());
    }
    public String getDateOfBirth ()
    {
        return (String)m_dateOfBirth.getValue();
    }
    // RDFS2Class: end of getter for slot dateOfBirth

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
    public Human()
    {
        super();
        putRDFSClass( new de.dfki.rdf.util.RDFResource( "http://de.dfki.rdf.test/datetime#", "Human" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        de.dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_name );
        ps.addProperty( m_dateOfBirth );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class Human
// EOF

