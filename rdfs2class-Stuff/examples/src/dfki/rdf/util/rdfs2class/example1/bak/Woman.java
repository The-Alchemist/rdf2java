package dfki.rdf.util.rdfs2class.example1;

// RDFS2Class: imports
import java.util.*;
// RDFS2Class: end of imports


/** RDFS2Class: class Woman
  * <p>
  */
public class Woman
    extends Person
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot isSexy **/
    String m_isSexy;

    /** RDFS2Class: putter for slot isSexy **/
    public void putIsSexy (String p_isSexy)
    {
        m_isSexy = p_isSexy;
    }
    // RDFS2Class: end of putter for slot isSexy

    /** RDFS2Class: getter for slot isSexy **/
    public String getIsSexy ()
    {
        return m_isSexy;
    }
    // RDFS2Class: end of getter for slot isSexy

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_isSexy != null) {
            sb.append(sIndent+"-> isSexy: "+m_isSexy+"\n");
        }
    }
    // RDFS2Class: end of toString()-stuff

    //------------------------------------------------------------------------------
}
// RDFS2Class: end of class Woman


