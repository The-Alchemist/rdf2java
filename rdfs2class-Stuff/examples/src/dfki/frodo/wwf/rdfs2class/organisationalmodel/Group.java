package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class Group
    extends OrganisationalUnit
{
    //------------------------------------------------------------------------------
    Collection m_belongsTo = new HashSet();

    public void putBelongsTo (OrganisationalUnit p_belongsTo)
    {
        m_belongsTo.add(p_belongsTo);
    }

    public Collection getBelongsTo ()
    {
        return m_belongsTo;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (!m_belongsTo.isEmpty()) {
            sb.append(sIndent+"-> belongsTo:\n");
            Iterator it_belongsTo = m_belongsTo.iterator();
            while (it_belongsTo.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_belongsTo.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_belongsTo.next()).toString(sIndent+"       ") );
            }
        }
    }

    //------------------------------------------------------------------------------
}

