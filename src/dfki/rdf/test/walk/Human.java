package dfki.rdf.test.walk;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class Human
  * <p>
  */
public abstract class Human
    extends dfki.rdf.util.THING
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot name **/
    protected dfki.rdf.util.PropertyInfo m_name = dfki.rdf.util.PropertyInfo.createStringProperty( "http://dfki.rdf.test/walk#", "name", false );

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
    /** RDFS2Class: slot hasFather **/
    protected dfki.rdf.util.PropertyInfo m_hasFather = dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.rdf.test/walk#", "hasFather", new Class[]{Man.class}, false );

    /** RDFS2Class: putter for slot hasFather **/
    public void putHasFather (Man p_hasFather)
    {
        m_hasFather.putValue(p_hasFather);
    }
    public void putHasFather (dfki.rdf.util.RDFResource p_hasFather)
    {
        m_hasFather.putValue(p_hasFather);
    }
    // RDFS2Class: end of putter for slot hasFather

    /** RDFS2Class: getter for slot hasFather **/
    public Man GetHasFather ()
    {
        return (Man)m_hasFather.getValue();
    }
    public dfki.rdf.util.RDFResource getHasFather ()
    {
        return (dfki.rdf.util.RDFResource)m_hasFather.getValue();
    }
    // RDFS2Class: end of getter for slot hasFather

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot hasMother **/
    protected dfki.rdf.util.PropertyInfo m_hasMother = dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.rdf.test/walk#", "hasMother", new Class[]{Woman.class}, false );

    /** RDFS2Class: putter for slot hasMother **/
    public void putHasMother (Woman p_hasMother)
    {
        m_hasMother.putValue(p_hasMother);
    }
    public void putHasMother (dfki.rdf.util.RDFResource p_hasMother)
    {
        m_hasMother.putValue(p_hasMother);
    }
    // RDFS2Class: end of putter for slot hasMother

    /** RDFS2Class: getter for slot hasMother **/
    public Woman GetHasMother ()
    {
        return (Woman)m_hasMother.getValue();
    }
    public dfki.rdf.util.RDFResource getHasMother ()
    {
        return (dfki.rdf.util.RDFResource)m_hasMother.getValue();
    }
    // RDFS2Class: end of getter for slot hasMother

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot hasChild **/
    protected dfki.rdf.util.PropertyInfo m_hasChild = dfki.rdf.util.PropertyInfo.createInstanceProperty( "http://dfki.rdf.test/walk#", "hasChild", new Class[]{Human.class}, true );

    /** RDFS2Class: putter for slot hasChild **/
    public void putHasChild (Human p_hasChild)
    {
        m_hasChild.putValue(p_hasChild);
    }
    public void putHasChild (dfki.rdf.util.RDFResource p_hasChild)
    {
        m_hasChild.putValue(p_hasChild);
    }
    public void putHasChild (java.util.Collection p_hasChild)
    {
        m_hasChild.setValues(p_hasChild);
    }
    public void clearHasChild ()
    {
        m_hasChild.clearValue();
    }
    // RDFS2Class: end of putter for slot hasChild

    /** RDFS2Class: getter for slot hasChild **/
    public java.util.Collection getHasChild ()
    {
        return (java.util.Collection)m_hasChild.getValue();
    }
    // RDFS2Class: end of getter for slot hasChild

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public String toString()
    {
        return toStringPacked();
    }
    // RDFS2Class: end of toString()-stuff

    //------------------------------------------------------------------------------
    /** RDFS2Class: sub class information **/
    public final static Class[] KNOWN_SUBCLASSES = {Woman.class, Man.class};

    // RDFS2Class: end of sub class information

    //------------------------------------------------------------------------------
    /** RDFS2Class: default constructor **/
    public Human()
    {
        super();
        putRDFSClass( new dfki.rdf.util.RDFResource( "http://dfki.rdf.test/walk#", "Human" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_name );
        ps.addProperty( m_hasFather );
        ps.addProperty( m_hasMother );
        ps.addProperty( m_hasChild );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class Human
// EOF

