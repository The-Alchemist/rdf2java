package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class PersonalProfile
    extends Profile
{
    //------------------------------------------------------------------------------
    Collection m_hasInfoNeed = new HashSet();

    public void putHasInfoNeed (__OrganisationalModel__ p_hasInfoNeed)
    {
        m_hasInfoNeed.add(p_hasInfoNeed);
    }

    public Collection getHasInfoNeed ()
    {
        return m_hasInfoNeed;
    }

    //------------------------------------------------------------------------------
    Collection m_hasSkill = new HashSet();

    public void putHasSkill (Skill p_hasSkill)
    {
        m_hasSkill.add(p_hasSkill);
    }

    public Collection getHasSkill ()
    {
        return m_hasSkill;
    }

    //------------------------------------------------------------------------------
    Collection m_hasExperience = new HashSet();

    public void putHasExperience (Experience p_hasExperience)
    {
        m_hasExperience.add(p_hasExperience);
    }

    public Collection getHasExperience ()
    {
        return m_hasExperience;
    }

    //------------------------------------------------------------------------------
    User m_user;

    public void putUser (User p_user)
    {
        m_user = p_user;
    }

    public User getUser ()
    {
        return m_user;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (!m_hasInfoNeed.isEmpty()) {
            sb.append(sIndent+"-> hasInfoNeed:\n");
            Iterator it_hasInfoNeed = m_hasInfoNeed.iterator();
            while (it_hasInfoNeed.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_hasInfoNeed.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_hasInfoNeed.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_hasSkill.isEmpty()) {
            sb.append(sIndent+"-> hasSkill:\n");
            Iterator it_hasSkill = m_hasSkill.iterator();
            while (it_hasSkill.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_hasSkill.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_hasSkill.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_hasExperience.isEmpty()) {
            sb.append(sIndent+"-> hasExperience:\n");
            Iterator it_hasExperience = m_hasExperience.iterator();
            while (it_hasExperience.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_hasExperience.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_hasExperience.next()).toString(sIndent+"       ") );
            }
        }
        if (m_user != null) {
            sb.append(sIndent+"-> user:\n"+sIndent+"       "+m_user.toStringShort()+"\n");
            // sb.append(sIndent+"-> user:\n"+m_user.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

