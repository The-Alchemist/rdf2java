package dfki.frodo.wwf.rdfs2class.task;

import java.util.*;


public class IsFinished
    extends Term
{
    //------------------------------------------------------------------------------
    ExecutableObject m_object;

    public void putObject (ExecutableObject p_object)
    {
        m_object = p_object;
    }

    public ExecutableObject getObject ()
    {
        return m_object;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_object != null) {
            sb.append(sIndent+"-> object:\n"+sIndent+"       "+m_object.toStringShort()+"\n");
            // sb.append(sIndent+"-> object:\n"+m_object.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

