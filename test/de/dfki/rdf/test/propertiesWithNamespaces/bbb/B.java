package de.dfki.rdf.test.propertiesWithNamespaces.bbb;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class B
  * <p>
  */
public  class B
    extends de.dfki.rdf.util.THING
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot bname **/
    protected de.dfki.rdf.util.PropertyInfo m_bname = de.dfki.rdf.util.PropertyInfo.createStringProperty( "http://dfki.rdf.test/propertiesWithNamespaces/bbb#", "bname", false );

    /** RDFS2Class: putter for slot bname **/
    public void putBname (String p_bname)
    {
        m_bname.putValue(p_bname);
    }
    // RDFS2Class: end of putter for slot bname

    /** RDFS2Class: getter for slot bname **/
    public String getBname ()
    {
        return (String)m_bname.getValue();
    }
    // RDFS2Class: end of getter for slot bname

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
    public B()
    {
        super();
        putRDFSClass( new de.dfki.rdf.util.RDFResource( "http://dfki.rdf.test/propertiesWithNamespaces/bbb#", "B" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        de.dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_bname );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class B
// EOF

