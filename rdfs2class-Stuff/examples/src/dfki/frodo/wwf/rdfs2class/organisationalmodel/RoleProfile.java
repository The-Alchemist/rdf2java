package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class RoleProfile
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
    Role m_role;

    public void putRole (Role p_role)
    {
        m_role = p_role;
    }

    public Role getRole ()
    {
        return m_role;
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
        if (m_role != null) {
            sb.append(sIndent+"-> role:\n"+sIndent+"       "+m_role.toStringShort()+"\n");
            // sb.append(sIndent+"-> role:\n"+m_role.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

