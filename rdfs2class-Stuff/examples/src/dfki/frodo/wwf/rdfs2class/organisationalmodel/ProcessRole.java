package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class ProcessRole
    extends __OrganisationalModel__
{
    //------------------------------------------------------------------------------
    String m_name;

    public void putName (String p_name)
    {
        m_name = p_name;
    }

    public String getName ()
    {
        return m_name;
    }

    //------------------------------------------------------------------------------
    Collection m_orCondition = new HashSet();

    public void putOrCondition (__OrganisationalModel__ p_orCondition)
    {
        m_orCondition.add(p_orCondition);
    }

    public Collection getOrCondition ()
    {
        return m_orCondition;
    }

    //------------------------------------------------------------------------------
    Collection m_andCondition = new HashSet();

    public void putAndCondition (__OrganisationalModel__ p_andCondition)
    {
        m_andCondition.add(p_andCondition);
    }

    public Collection getAndCondition ()
    {
        return m_andCondition;
    }

    //------------------------------------------------------------------------------
    String m_informalDescription;

    public void putInformalDescription (String p_informalDescription)
    {
        m_informalDescription = p_informalDescription;
    }

    public String getInformalDescription ()
    {
        return m_informalDescription;
    }

    //------------------------------------------------------------------------------
    Collection m_notCondition = new HashSet();

    public void putNotCondition (__OrganisationalModel__ p_notCondition)
    {
        m_notCondition.add(p_notCondition);
    }

    public Collection getNotCondition ()
    {
        return m_notCondition;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_name != null) {
            sb.append(sIndent+"-> name: "+m_name+"\n");
        }
        if (!m_orCondition.isEmpty()) {
            sb.append(sIndent+"-> orCondition:\n");
            Iterator it_orCondition = m_orCondition.iterator();
            while (it_orCondition.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_orCondition.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_orCondition.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_andCondition.isEmpty()) {
            sb.append(sIndent+"-> andCondition:\n");
            Iterator it_andCondition = m_andCondition.iterator();
            while (it_andCondition.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_andCondition.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_andCondition.next()).toString(sIndent+"       ") );
            }
        }
        if (m_informalDescription != null) {
            sb.append(sIndent+"-> informalDescription: "+m_informalDescription+"\n");
        }
        if (!m_notCondition.isEmpty()) {
            sb.append(sIndent+"-> notCondition:\n");
            Iterator it_notCondition = m_notCondition.iterator();
            while (it_notCondition.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_notCondition.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_notCondition.next()).toString(sIndent+"       ") );
            }
        }
    }

    //------------------------------------------------------------------------------
}

