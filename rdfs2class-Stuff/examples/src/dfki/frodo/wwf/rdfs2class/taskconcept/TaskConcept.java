package dfki.frodo.wwf.rdfs2class.taskconcept;

import java.util.*;


public class TaskConcept
    extends __TaskConcept__
{
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
    Collection m_realizedBy = new HashSet();

    public void putRealizedBy (dfki.frodo.wwf.rdfs2class.task.Task p_realizedBy)
    {
        m_realizedBy.add(p_realizedBy);
    }

    public Collection getRealizedBy ()
    {
        return m_realizedBy;
    }

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
    Collection m_isa = new HashSet();

    public void putIsa (TaskConcept p_isa)
    {
        m_isa.add(p_isa);
    }

    public Collection getIsa ()
    {
        return m_isa;
    }

    //------------------------------------------------------------------------------
    Collection m_partOf = new HashSet();

    public void putPartOf (TaskConcept p_partOf)
    {
        m_partOf.add(p_partOf);
    }

    public Collection getPartOf ()
    {
        return m_partOf;
    }

    //------------------------------------------------------------------------------
    Collection m_relatedTo = new HashSet();

    public void putRelatedTo (TaskConcept p_relatedTo)
    {
        m_relatedTo.add(p_relatedTo);
    }

    public Collection getRelatedTo ()
    {
        return m_relatedTo;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_informalDescription != null) {
            sb.append(sIndent+"-> informalDescription: "+m_informalDescription+"\n");
        }
        if (!m_realizedBy.isEmpty()) {
            sb.append(sIndent+"-> realizedBy:\n");
            Iterator it_realizedBy = m_realizedBy.iterator();
            while (it_realizedBy.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_realizedBy.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_realizedBy.next()).toString(sIndent+"       ") );
            }
        }
        if (m_name != null) {
            sb.append(sIndent+"-> name: "+m_name+"\n");
        }
        if (!m_isa.isEmpty()) {
            sb.append(sIndent+"-> isa:\n");
            Iterator it_isa = m_isa.iterator();
            while (it_isa.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_isa.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_isa.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_partOf.isEmpty()) {
            sb.append(sIndent+"-> partOf:\n");
            Iterator it_partOf = m_partOf.iterator();
            while (it_partOf.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_partOf.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_partOf.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_relatedTo.isEmpty()) {
            sb.append(sIndent+"-> relatedTo:\n");
            Iterator it_relatedTo = m_relatedTo.iterator();
            while (it_relatedTo.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_relatedTo.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_relatedTo.next()).toString(sIndent+"       ") );
            }
        }
    }

    //------------------------------------------------------------------------------
}

