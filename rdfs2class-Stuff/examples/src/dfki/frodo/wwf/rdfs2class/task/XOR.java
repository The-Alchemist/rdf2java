package dfki.frodo.wwf.rdfs2class.task;

import java.util.*;


public class XOR
    extends Term
{
    //------------------------------------------------------------------------------
    Collection m_term = new HashSet();

    public void putTerm (Term p_term)
    {
        m_term.add(p_term);
    }

    public Collection getTerm ()
    {
        return m_term;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (!m_term.isEmpty()) {
            sb.append(sIndent+"-> term:\n");
            Iterator it_term = m_term.iterator();
            while (it_term.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_term.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_term.next()).toString(sIndent+"       ") );
            }
        }
    }

    //------------------------------------------------------------------------------
}

