package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class Skill
    extends __OrganisationalModel__
{
    //------------------------------------------------------------------------------
    Collection m_partOfSkill = new HashSet();

    public void putPartOfSkill (Skill p_partOfSkill)
    {
        m_partOfSkill.add(p_partOfSkill);
    }

    public Collection getPartOfSkill ()
    {
        return m_partOfSkill;
    }

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
    Collection m_neededFor = new HashSet();

    public void putNeededFor (dfki.frodo.wwf.rdfs2class.task.Task p_neededFor)
    {
        m_neededFor.add(p_neededFor);
    }

    public Collection getNeededFor ()
    {
        return m_neededFor;
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
    public void toString (StringBuffer sb, String sIndent)
    {
        if (!m_partOfSkill.isEmpty()) {
            sb.append(sIndent+"-> partOfSkill:\n");
            Iterator it_partOfSkill = m_partOfSkill.iterator();
            while (it_partOfSkill.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_partOfSkill.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_partOfSkill.next()).toString(sIndent+"       ") );
            }
        }
        if (m_name != null) {
            sb.append(sIndent+"-> name: "+m_name+"\n");
        }
        if (!m_neededFor.isEmpty()) {
            sb.append(sIndent+"-> neededFor:\n");
            Iterator it_neededFor = m_neededFor.iterator();
            while (it_neededFor.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_neededFor.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_neededFor.next()).toString(sIndent+"       ") );
            }
        }
        if (m_informalDescription != null) {
            sb.append(sIndent+"-> informalDescription: "+m_informalDescription+"\n");
        }
    }

    //------------------------------------------------------------------------------
}

