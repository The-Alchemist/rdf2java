package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class OrganisationalUnit
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
    UnitProfile m_hasProfile;

    public void putHasProfile (UnitProfile p_hasProfile)
    {
        m_hasProfile = p_hasProfile;
    }

    public UnitProfile getHasProfile ()
    {
        return m_hasProfile;
    }

    //------------------------------------------------------------------------------
    Collection m_containsMember = new HashSet();

    public void putContainsMember (__OrganisationalModel__ p_containsMember)
    {
        m_containsMember.add(p_containsMember);
    }

    public Collection getContainsMember ()
    {
        return m_containsMember;
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
    User m_managedBy;

    public void putManagedBy (User p_managedBy)
    {
        m_managedBy = p_managedBy;
    }

    public User getManagedBy ()
    {
        return m_managedBy;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_name != null) {
            sb.append(sIndent+"-> name: "+m_name+"\n");
        }
        if (m_hasProfile != null) {
            sb.append(sIndent+"-> hasProfile:\n"+sIndent+"       "+m_hasProfile.toStringShort()+"\n");
            // sb.append(sIndent+"-> hasProfile:\n"+m_hasProfile.toString(sIndent+"       "));
        }
        if (!m_containsMember.isEmpty()) {
            sb.append(sIndent+"-> containsMember:\n");
            Iterator it_containsMember = m_containsMember.iterator();
            while (it_containsMember.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_containsMember.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_containsMember.next()).toString(sIndent+"       ") );
            }
        }
        if (m_informalDescription != null) {
            sb.append(sIndent+"-> informalDescription: "+m_informalDescription+"\n");
        }
        if (m_managedBy != null) {
            sb.append(sIndent+"-> managedBy:\n"+sIndent+"       "+m_managedBy.toStringShort()+"\n");
            // sb.append(sIndent+"-> managedBy:\n"+m_managedBy.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

