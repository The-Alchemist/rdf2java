package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class UnitProfile
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
    OrganisationalUnit m_unit;

    public void putUnit (OrganisationalUnit p_unit)
    {
        m_unit = p_unit;
    }

    public OrganisationalUnit getUnit ()
    {
        return m_unit;
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
        if (m_unit != null) {
            sb.append(sIndent+"-> unit:\n"+sIndent+"       "+m_unit.toStringShort()+"\n");
            // sb.append(sIndent+"-> unit:\n"+m_unit.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

