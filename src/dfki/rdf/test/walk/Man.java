package dfki.rdf.test.walk;

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
    protected dfki.rdf.util.PropertyInfo m_hasWife = dfki.rdf.util.PropertyInfo.createInstanceProperty( "hasWife", new Class[]{Woman.class}, false );

    /** RDFS2Class: putter for slot hasWife **/
    public void putHasWife (Woman p_hasWife)
    {
        m_hasWife.putValue(p_hasWife);
    }
    public void putHasWife (dfki.rdf.util.RDFResource p_hasWife)
    {
        m_hasWife.putValue(p_hasWife);
    }
    // RDFS2Class: end of putter for slot hasWife

    /** RDFS2Class: getter for slot hasWife **/
    public Woman GetHasWife ()
    {
        return (Woman)m_hasWife.getValue();
    }
    public dfki.rdf.util.RDFResource getHasWife ()
    {
        return (dfki.rdf.util.RDFResource)m_hasWife.getValue();
    }
    // RDFS2Class: end of getter for slot hasWife

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        super.toString(sb, sIndent);
        if (m_hasWife.getValue() != null) {
            sb.append(sIndent+"-> hasWife:\n"+sIndent+"       "+((dfki.rdf.util.RDFResource)m_hasWife.getValue()).toStringShort() + "\n");
            // sb.append(sIndent+"-> hasWife:\n"+((dfki.rdf.util.RDFResource)m_hasWife.getValue()).toString(sIndent+"       "));
        }
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
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_hasWife );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class Man
// EOF

