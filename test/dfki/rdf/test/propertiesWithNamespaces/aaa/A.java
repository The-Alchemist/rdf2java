package dfki.rdf.test.propertiesWithNamespaces.aaa;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class A
  * <p>
  */
public  class A
    extends dfki.rdf.util.THING
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot ccc_a2b **/
    protected dfki.rdf.util.PropertyInfo m_ccc_a2b = dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.rdf.test/propertiesWithNamespaces/ccc#", "a2b", new Class[]{dfki.rdf.test.propertiesWithNamespaces.bbb.B.class}, false );

    /** RDFS2Class: putter for slot ccc_a2b **/
    public void putCcc_a2b (dfki.rdf.test.propertiesWithNamespaces.bbb.B p_ccc_a2b)
    {
        m_ccc_a2b.putValue(p_ccc_a2b);
    }
    public void putCcc_a2b (dfki.rdf.util.RDFResource p_ccc_a2b)
    {
        m_ccc_a2b.putValue(p_ccc_a2b);
    }
    // RDFS2Class: end of putter for slot ccc_a2b

    /** RDFS2Class: getter for slot ccc_a2b **/
    public dfki.rdf.test.propertiesWithNamespaces.bbb.B GetCcc_a2b ()
    {
        return (dfki.rdf.test.propertiesWithNamespaces.bbb.B)m_ccc_a2b.getValue();
    }
    public dfki.rdf.util.RDFResource getCcc_a2b ()
    {
        return (dfki.rdf.util.RDFResource)m_ccc_a2b.getValue();
    }
    // RDFS2Class: end of getter for slot ccc_a2b

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot aname **/
    protected dfki.rdf.util.PropertyInfo m_aname = dfki.rdf.util.PropertyInfo.createStringProperty( "http://dfki.rdf.test/propertiesWithNamespaces/aaa#", "aname", false );

    /** RDFS2Class: putter for slot aname **/
    public void putAname (String p_aname)
    {
        m_aname.putValue(p_aname);
    }
    // RDFS2Class: end of putter for slot aname

    /** RDFS2Class: getter for slot aname **/
    public String getAname ()
    {
        return (String)m_aname.getValue();
    }
    // RDFS2Class: end of getter for slot aname

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot a2b **/
    protected dfki.rdf.util.PropertyInfo m_a2b = dfki.rdf.util.PropertyInfo.createStringProperty( "http://dfki.rdf.test/propertiesWithNamespaces/aaa#", "a2b", false );

    /** RDFS2Class: putter for slot a2b **/
    public void putA2b (String p_a2b)
    {
        m_a2b.putValue(p_a2b);
    }
    // RDFS2Class: end of putter for slot a2b

    /** RDFS2Class: getter for slot a2b **/
    public String getA2b ()
    {
        return (String)m_a2b.getValue();
    }
    // RDFS2Class: end of getter for slot a2b

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
        putRDFSClass( new dfki.rdf.util.RDFResource( "http://dfki.rdf.test/propertiesWithNamespaces/aaa#", "A" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_ccc_a2b );
        ps.addProperty( m_aname );
        ps.addProperty( m_a2b );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class A
// EOF

