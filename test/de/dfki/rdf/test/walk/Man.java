package de.dfki.rdf.test.walk;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class Man
  * <p>
  */
public  class Man
    extends Human
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot hasWife **/
    protected de.dfki.rdf.util.PropertyInfo m_hasWife = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://de.dfki.rdf.test/walk#", "hasWife", new Class[]{Woman.class}, false );

    /** RDFS2Class: putter for slot hasWife **/
    public void putHasWife (Woman p_hasWife)
    {
        m_hasWife.putValue(p_hasWife);
    }
    public void putHasWife (de.dfki.rdf.util.RDFResource p_hasWife)
    {
        m_hasWife.putValue(p_hasWife);
    }
    // RDFS2Class: end of putter for slot hasWife

    /** RDFS2Class: getter for slot hasWife **/
    public Woman GetHasWife ()
    {
        return (Woman)m_hasWife.getValue();
    }
    public de.dfki.rdf.util.RDFResource getHasWife ()
    {
        return (de.dfki.rdf.util.RDFResource)m_hasWife.getValue();
    }
    // RDFS2Class: end of getter for slot hasWife

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
    public Man()
    {
        super();
        putRDFSClass( new de.dfki.rdf.util.RDFResource( "http://de.dfki.rdf.test/walk#", "Man" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        de.dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_hasWife );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class Man
// EOF

