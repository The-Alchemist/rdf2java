package dfki.frodo.wwf.rdfs2class.task;

import java.util.*;


public class TaskInstance
    extends Task
{
    //------------------------------------------------------------------------------
    TaskModel m_instanceOf;

    public void putInstanceOf (TaskModel p_instanceOf)
    {
        m_instanceOf = p_instanceOf;
    }

    public TaskModel getInstanceOf ()
    {
        return m_instanceOf;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_instanceOf != null) {
            sb.append(sIndent+"-> instanceOf:\n"+sIndent+"       "+m_instanceOf.toStringShort()+"\n");
            // sb.append(sIndent+"-> instanceOf:\n"+m_instanceOf.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

