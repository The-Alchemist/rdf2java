package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class Role
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
    RoleProfile m_hasRoleProfile;

    public void putHasRoleProfile (RoleProfile p_hasRoleProfile)
    {
        m_hasRoleProfile = p_hasRoleProfile;
    }

    public RoleProfile getHasRoleProfile ()
    {
        return m_hasRoleProfile;
    }

    //------------------------------------------------------------------------------
    Collection m_enactedBy = new HashSet();

    public void putEnactedBy (User p_enactedBy)
    {
        m_enactedBy.add(p_enactedBy);
    }

    public Collection getEnactedBy ()
    {
        return m_enactedBy;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_name != null) {
            sb.append(sIndent+"-> name: "+m_name+"\n");
        }
        if (m_informalDescription != null) {
            sb.append(sIndent+"-> informalDescription: "+m_informalDescription+"\n");
        }
        if (m_hasRoleProfile != null) {
            sb.append(sIndent+"-> hasRoleProfile:\n"+sIndent+"       "+m_hasRoleProfile.toStringShort()+"\n");
            // sb.append(sIndent+"-> hasRoleProfile:\n"+m_hasRoleProfile.toString(sIndent+"       "));
        }
        if (!m_enactedBy.isEmpty()) {
            sb.append(sIndent+"-> enactedBy:\n");
            Iterator it_enactedBy = m_enactedBy.iterator();
            while (it_enactedBy.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_enactedBy.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_enactedBy.next()).toString(sIndent+"       ") );
            }
        }
    }

    //------------------------------------------------------------------------------
}

