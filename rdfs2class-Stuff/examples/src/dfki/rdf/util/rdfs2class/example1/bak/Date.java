package dfki.rdf.util.rdfs2class.example1;

// RDFS2Class: imports
import java.util.*;
// RDFS2Class: end of imports


/** RDFS2Class: class Date
  * <p>
  */
public class Date
    extends dfki.rdf.util.THING
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot month **/
    String m_month;

    /** RDFS2Class: putter for slot month **/
    public void putMonth (String p_month)
    {
        m_month = p_month;
    }
    // RDFS2Class: end of putter for slot month

    /** RDFS2Class: getter for slot month **/
    public String getMonth ()
    {
        return m_month;
    }
    // RDFS2Class: end of getter for slot month

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot year **/
    String m_year;

    /** RDFS2Class: putter for slot year **/
    public void putYear (String p_year)
    {
        m_year = p_year;
    }
    // RDFS2Class: end of putter for slot year

    /** RDFS2Class: getter for slot year **/
    public String getYear ()
    {
        return m_year;
    }
    // RDFS2Class: end of getter for slot year

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot dayOfMonth **/
    String m_dayOfMonth;

    /** RDFS2Class: putter for slot dayOfMonth **/
    public void putDayOfMonth (String p_dayOfMonth)
    {
        m_dayOfMonth = p_dayOfMonth;
    }
    // RDFS2Class: end of putter for slot dayOfMonth

    /** RDFS2Class: getter for slot dayOfMonth **/
    public String getDayOfMonth ()
    {
        return m_dayOfMonth;
    }
    // RDFS2Class: end of getter for slot dayOfMonth

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_month != null) {
            sb.append(sIndent+"-> month: "+m_month+"\n");
        }
        if (m_year != null) {
            sb.append(sIndent+"-> year: "+m_year+"\n");
        }
        if (m_dayOfMonth != null) {
            sb.append(sIndent+"-> dayOfMonth: "+m_dayOfMonth+"\n");
        }
    }
    // RDFS2Class: end of toString()-stuff

    //------------------------------------------------------------------------------
}
// RDFS2Class: end of class Date


