package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class InformationNeed
    extends __OrganisationalModel__
{
    //------------------------------------------------------------------------------
    Collection m_describedBy = new HashSet();

    public void putDescribedBy (__OrganisationalModel__ p_describedBy)
    {
        m_describedBy.add(p_describedBy);
    }

    public Collection getDescribedBy ()
    {
        return m_describedBy;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (!m_describedBy.isEmpty()) {
            sb.append(sIndent+"-> describedBy:\n");
            Iterator it_describedBy = m_describedBy.iterator();
            while (it_describedBy.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_describedBy.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_describedBy.next()).toString(sIndent+"       ") );
            }
        }
    }

    //------------------------------------------------------------------------------
}

