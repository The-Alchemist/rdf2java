package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class User
    extends __OrganisationalModel__
{
    //------------------------------------------------------------------------------
    String m_lastName;

    public void putLastName (String p_lastName)
    {
        m_lastName = p_lastName;
    }

    public String getLastName ()
    {
        return m_lastName;
    }

    //------------------------------------------------------------------------------
    Collection m_enactsRole = new HashSet();

    public void putEnactsRole (Role p_enactsRole)
    {
        m_enactsRole.add(p_enactsRole);
    }

    public Collection getEnactsRole ()
    {
        return m_enactsRole;
    }

    //------------------------------------------------------------------------------
    Collection m_belongsToUnit = new HashSet();

    public void putBelongsToUnit (OrganisationalUnit p_belongsToUnit)
    {
        m_belongsToUnit.add(p_belongsToUnit);
    }

    public Collection getBelongsToUnit ()
    {
        return m_belongsToUnit;
    }

    //------------------------------------------------------------------------------
    String m_firstName;

    public void putFirstName (String p_firstName)
    {
        m_firstName = p_firstName;
    }

    public String getFirstName ()
    {
        return m_firstName;
    }

    //------------------------------------------------------------------------------
    PersonalProfile m_hasPersonalProfile;

    public void putHasPersonalProfile (PersonalProfile p_hasPersonalProfile)
    {
        m_hasPersonalProfile = p_hasPersonalProfile;
    }

    public PersonalProfile getHasPersonalProfile ()
    {
        return m_hasPersonalProfile;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_lastName != null) {
            sb.append(sIndent+"-> lastName: "+m_lastName+"\n");
        }
        if (!m_enactsRole.isEmpty()) {
            sb.append(sIndent+"-> enactsRole:\n");
            Iterator it_enactsRole = m_enactsRole.iterator();
            while (it_enactsRole.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_enactsRole.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_enactsRole.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_belongsToUnit.isEmpty()) {
            sb.append(sIndent+"-> belongsToUnit:\n");
            Iterator it_belongsToUnit = m_belongsToUnit.iterator();
            while (it_belongsToUnit.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_belongsToUnit.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_belongsToUnit.next()).toString(sIndent+"       ") );
            }
        }
        if (m_firstName != null) {
            sb.append(sIndent+"-> firstName: "+m_firstName+"\n");
        }
        if (m_hasPersonalProfile != null) {
            sb.append(sIndent+"-> hasPersonalProfile:\n"+sIndent+"       "+m_hasPersonalProfile.toStringShort()+"\n");
            // sb.append(sIndent+"-> hasPersonalProfile:\n"+m_hasPersonalProfile.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

