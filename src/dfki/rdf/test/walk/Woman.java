package dfki.rdf.test.walk;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class Woman
  * <p>
  */
public  class Woman
    extends Human
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot hasHusband **/
    protected dfki.rdf.util.PropertyInfo m_hasHusband = dfki.rdf.util.PropertyInfo.createInstanceProperty( "hasHusband", new Class[]{Man.class}, false );

    /** RDFS2Class: putter for slot hasHusband **/
    public void putHasHusband (Man p_hasHusband)
    {
        m_hasHusband.putValue(p_hasHusband);
    }
    public void putHasHusband (dfki.rdf.util.RDFResource p_hasHusband)
    {
        m_hasHusband.putValue(p_hasHusband);
    }
    // RDFS2Class: end of putter for slot hasHusband

    /** RDFS2Class: getter for slot hasHusband **/
    public Man GetHasHusband ()
    {
        return (Man)m_hasHusband.getValue();
    }
    public dfki.rdf.util.RDFResource getHasHusband ()
    {
        return (dfki.rdf.util.RDFResource)m_hasHusband.getValue();
    }
    // RDFS2Class: end of getter for slot hasHusband

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        super.toString(sb, sIndent);
        if (m_hasHusband.getValue() != null) {
            sb.append(sIndent+"-> hasHusband:\n"+sIndent+"       "+((dfki.rdf.util.RDFResource)m_hasHusband.getValue()).toStringShort() + "\n");
            // sb.append(sIndent+"-> hasHusband:\n"+((dfki.rdf.util.RDFResource)m_hasHusband.getValue()).toString(sIndent+"       "));
        }
    }
    // RDFS2Class: end of toString()-stuff

    //------------------------------------------------------------------------------
    /** RDFS2Class: sub class information **/
    public final static Class[] KNOWN_SUBCLASSES = {};

    // RDFS2Class: end of sub class information

    //------------------------------------------------------------------------------
    /** RDFS2Class: default constructor **/
    public Woman()
    {
        super();
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_hasHusband );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class Woman
// EOF
