package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class Interest
    extends __OrganisationalModel__
{
    //------------------------------------------------------------------------------
    Collection m_inTopic = new HashSet();

    public void putInTopic (DomainOntologyElement p_inTopic)
    {
        m_inTopic.add(p_inTopic);
    }

    public Collection getInTopic ()
    {
        return m_inTopic;
    }

    //------------------------------------------------------------------------------
    Profile m_interestBy;

    public void putInterestBy (Profile p_interestBy)
    {
        m_interestBy = p_interestBy;
    }

    public Profile getInterestBy ()
    {
        return m_interestBy;
    }

    //------------------------------------------------------------------------------
    String m_strength;

    public void putStrength (String p_strength)
    {
        m_strength = p_strength;
    }

    public String getStrength ()
    {
        return m_strength;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (!m_inTopic.isEmpty()) {
            sb.append(sIndent+"-> inTopic:\n");
            Iterator it_inTopic = m_inTopic.iterator();
            while (it_inTopic.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_inTopic.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_inTopic.next()).toString(sIndent+"       ") );
            }
        }
        if (m_interestBy != null) {
            sb.append(sIndent+"-> interestBy:\n"+sIndent+"       "+m_interestBy.toStringShort()+"\n");
            // sb.append(sIndent+"-> interestBy:\n"+m_interestBy.toString(sIndent+"       "));
        }
        if (m_strength != null) {
            sb.append(sIndent+"-> strength: "+m_strength+"\n");
        }
    }

    //------------------------------------------------------------------------------
}

