package dfki.rdf.util.rdfs2class.example1;

// RDFS2Class: imports
import java.util.*;
// RDFS2Class: end of imports


/** RDFS2Class: class Person
  * <p>
  */
public class Person
    extends dfki.rdf.util.THING
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot dateOfBirth **/
    Date m_dateOfBirth;

    /** RDFS2Class: putter for slot dateOfBirth **/
    public void putDateOfBirth (Date p_dateOfBirth)
    {
        m_dateOfBirth = p_dateOfBirth;
    }
    // RDFS2Class: end of putter for slot dateOfBirth

    /** RDFS2Class: getter for slot dateOfBirth **/
    public Date getDateOfBirth ()
    {
        return m_dateOfBirth;
    }
    // RDFS2Class: end of getter for slot dateOfBirth

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot relatedTo **/
    Collection m_relatedTo = new LinkedList();

    /** RDFS2Class: putter for slot relatedTo **/
    public void putRelatedTo (Person p_relatedTo)
    {
        m_relatedTo.add(p_relatedTo);
    }
    // RDFS2Class: end of putter for slot relatedTo

    /** RDFS2Class: getter for slot relatedTo **/
    public Collection getRelatedTo ()
    {
        return m_relatedTo;
    }
    // RDFS2Class: end of getter for slot relatedTo

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot lastName **/
    String m_lastName;

    /** RDFS2Class: putter for slot lastName **/
    public void putLastName (String p_lastName)
    {
        m_lastName = p_lastName;
    }
    // RDFS2Class: end of putter for slot lastName

    /** RDFS2Class: getter for slot lastName **/
    public String getLastName ()
    {
        return m_lastName;
    }
    // RDFS2Class: end of getter for slot lastName

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot firstName **/
    String m_firstName;

    /** RDFS2Class: putter for slot firstName **/
    public void putFirstName (String p_firstName)
    {
        m_firstName = p_firstName;
    }
    // RDFS2Class: end of putter for slot firstName

    /** RDFS2Class: getter for slot firstName **/
    public String getFirstName ()
    {
        return m_firstName;
    }
    // RDFS2Class: end of getter for slot firstName

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot loves **/
    Collection m_loves = new LinkedList();

    /** RDFS2Class: putter for slot loves **/
    public void putLoves (dfki.rdf.util.THING p_loves)
    {
        if (!(p_loves instanceof Person) && !(p_loves instanceof Animal))
            throw new Error("not an allowed class");
        m_loves.add(p_loves);
    }
    // RDFS2Class: end of putter for slot loves

    /** RDFS2Class: getter for slot loves **/
    public Collection getLoves ()
    {
        return m_loves;
    }
    // RDFS2Class: end of getter for slot loves

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_dateOfBirth != null) {
            sb.append(sIndent+"-> dateOfBirth:\n"+sIndent+"       "+m_dateOfBirth.toStringShort()+"\n");
            // sb.append(sIndent+"-> dateOfBirth:\n"+m_dateOfBirth.toString(sIndent+"       "));
        }
        if (!m_relatedTo.isEmpty()) {
            sb.append(sIndent+"-> relatedTo:\n");
            Iterator it_relatedTo = m_relatedTo.iterator();
            while (it_relatedTo.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_relatedTo.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_relatedTo.next()).toString(sIndent+"       ") );
            }
        }
        if (m_lastName != null) {
            sb.append(sIndent+"-> lastName: "+m_lastName+"\n");
        }
        if (m_firstName != null) {
            sb.append(sIndent+"-> firstName: "+m_firstName+"\n");
        }
        if (!m_loves.isEmpty()) {
            sb.append(sIndent+"-> loves:\n");
            Iterator it_loves = m_loves.iterator();
            while (it_loves.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_loves.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_loves.next()).toString(sIndent+"       ") );
            }
        }
    }
    // RDFS2Class: end of toString()-stuff

    //------------------------------------------------------------------------------
}
// RDFS2Class: end of class Person


