package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class Profile
    extends __OrganisationalModel__
{
    //------------------------------------------------------------------------------
    Collection m_hasInterest = new HashSet();

    public void putHasInterest (Interest p_hasInterest)
    {
        m_hasInterest.add(p_hasInterest);
    }

    public Collection getHasInterest ()
    {
        return m_hasInterest;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (!m_hasInterest.isEmpty()) {
            sb.append(sIndent+"-> hasInterest:\n");
            Iterator it_hasInterest = m_hasInterest.iterator();
            while (it_hasInterest.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_hasInterest.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_hasInterest.next()).toString(sIndent+"       ") );
            }
        }
    }

    //------------------------------------------------------------------------------
}

