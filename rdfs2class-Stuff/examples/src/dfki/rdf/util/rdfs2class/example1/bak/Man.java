package dfki.rdf.util.rdfs2class.example1;

// RDFS2Class: imports
import java.util.*;
// RDFS2Class: end of imports


/** RDFS2Class: class Man
  * <p>
  */
public class Man
    extends Person
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot isCool **/
    String m_isCool;

    /** RDFS2Class: putter for slot isCool **/
    public void putIsCool (String p_isCool)
    {
        m_isCool = p_isCool;
    }
    // RDFS2Class: end of putter for slot isCool

    /** RDFS2Class: getter for slot isCool **/
    public String getIsCool ()
    {
        return m_isCool;
    }
    // RDFS2Class: end of getter for slot isCool

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_isCool != null) {
            sb.append(sIndent+"-> isCool: "+m_isCool+"\n");
        }
    }
    // RDFS2Class: end of toString()-stuff

    //------------------------------------------------------------------------------
}
// RDFS2Class: end of class Man


