package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class Experience
    extends __OrganisationalModel__
{
    //------------------------------------------------------------------------------
    Collection m_experienceObject = new HashSet();

    public void putExperienceObject (dfki.rdf.util.THING p_experienceObject)
    {
        if (!(p_experienceObject instanceof DomainOntologyElement) && !(p_experienceObject instanceof InformationDescription) && !(p_experienceObject instanceof dfki.frodo.wwf.rdfs2class.taskconcept.TaskConcept) && !(p_experienceObject instanceof dfki.frodo.wwf.rdfs2class.task.Task))
            throw new Error("not an allowed class");
        m_experienceObject.add(p_experienceObject);
    }

    public Collection getExperienceObject ()
    {
        return m_experienceObject;
    }

    //------------------------------------------------------------------------------
    String m_experienceLevel;

    public void putExperienceLevel (String p_experienceLevel)
    {
        m_experienceLevel = p_experienceLevel;
    }

    public String getExperienceLevel ()
    {
        return m_experienceLevel;
    }

    //------------------------------------------------------------------------------
    Collection m_experiencedUser = new HashSet();

    public void putExperiencedUser (PersonalProfile p_experiencedUser)
    {
        m_experiencedUser.add(p_experiencedUser);
    }

    public Collection getExperiencedUser ()
    {
        return m_experiencedUser;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (!m_experienceObject.isEmpty()) {
            sb.append(sIndent+"-> experienceObject:\n");
            Iterator it_experienceObject = m_experienceObject.iterator();
            while (it_experienceObject.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_experienceObject.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_experienceObject.next()).toString(sIndent+"       ") );
            }
        }
        if (m_experienceLevel != null) {
            sb.append(sIndent+"-> experienceLevel: "+m_experienceLevel+"\n");
        }
        if (!m_experiencedUser.isEmpty()) {
            sb.append(sIndent+"-> experiencedUser:\n");
            Iterator it_experiencedUser = m_experiencedUser.iterator();
            while (it_experiencedUser.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_experiencedUser.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_experiencedUser.next()).toString(sIndent+"       ") );
            }
        }
    }

    //------------------------------------------------------------------------------
}

