package dfki.frodo.wwf.rdfs2class.task;

import java.util.*;


public class Condition
    extends __Task__
{
    //------------------------------------------------------------------------------
    Term m_condTerm;

    public void putCondTerm (Term p_condTerm)
    {
        m_condTerm = p_condTerm;
    }

    public Term getCondTerm ()
    {
        return m_condTerm;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_condTerm != null) {
            sb.append(sIndent+"-> condTerm:\n"+sIndent+"       "+m_condTerm.toStringShort()+"\n");
            // sb.append(sIndent+"-> condTerm:\n"+m_condTerm.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

