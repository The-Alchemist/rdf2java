package dfki.rdf.util.rdfs2class.example1;

// RDFS2Class: imports
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Iterator;
// RDFS2Class: end of imports


/** RDFS2Class: class Animal
  * <p>
  */
public class Animal
    extends dfki.rdf.util.THING
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot name **/
    String m_name;

    /** RDFS2Class: putter for slot name **/
    public void putName (String p_name)
    {
        m_name = p_name;
    }
    // RDFS2Class: end of putter for slot name

    /** RDFS2Class: getter for slot name **/
    public String getName ()
    {
        return m_name;
    }
    // RDFS2Class: end of getter for slot name

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        super.toString(sb, sIndent);
        if (m_name != null) {
            sb.append(sIndent+"-> name: "+m_name+"\n");
        }
    }
    // RDFS2Class: end of toString()-stuff

}
// RDFS2Class: end of class Animal


// EOF

