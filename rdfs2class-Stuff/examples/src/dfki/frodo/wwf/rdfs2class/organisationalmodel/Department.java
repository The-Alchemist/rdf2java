package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class Department
    extends OrganisationalUnit
{
    //------------------------------------------------------------------------------
    Collection m_secretary = new HashSet();

    public void putSecretary (User p_secretary)
    {
        m_secretary.add(p_secretary);
    }

    public Collection getSecretary ()
    {
        return m_secretary;
    }

    //------------------------------------------------------------------------------
    Department m_partOf;

    public void putPartOf (Department p_partOf)
    {
        m_partOf = p_partOf;
    }

    public Department getPartOf ()
    {
        return m_partOf;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (!m_secretary.isEmpty()) {
            sb.append(sIndent+"-> secretary:\n");
            Iterator it_secretary = m_secretary.iterator();
            while (it_secretary.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_secretary.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_secretary.next()).toString(sIndent+"       ") );
            }
        }
        if (m_partOf != null) {
            sb.append(sIndent+"-> partOf:\n"+sIndent+"       "+m_partOf.toStringShort()+"\n");
            // sb.append(sIndent+"-> partOf:\n"+m_partOf.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

