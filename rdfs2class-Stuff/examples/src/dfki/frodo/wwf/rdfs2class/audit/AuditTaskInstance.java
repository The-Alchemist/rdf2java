package dfki.frodo.wwf.rdfs2class.audit;

import java.util.*;


public class AuditTaskInstance
    extends BasicAuditObject
{
    //------------------------------------------------------------------------------
    dfki.rdf.util.THING m_taskInstanceId;

    public void putTaskInstanceId (dfki.rdf.util.THING p_taskInstanceId)
    {
        m_taskInstanceId = p_taskInstanceId;
    }

    public dfki.rdf.util.THING getTaskInstanceId ()
    {
        return m_taskInstanceId;
    }

    //------------------------------------------------------------------------------
    dfki.rdf.util.THING m_rootTaskInstanceId;

    public void putRootTaskInstanceId (dfki.rdf.util.THING p_rootTaskInstanceId)
    {
        m_rootTaskInstanceId = p_rootTaskInstanceId;
    }

    public dfki.rdf.util.THING getRootTaskInstanceId ()
    {
        return m_rootTaskInstanceId;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_taskInstanceId != null) {
            sb.append(sIndent+"-> taskInstanceId:\n"+sIndent+"       "+m_taskInstanceId.toStringShort()+"\n");
            // sb.append(sIndent+"-> taskInstanceId:\n"+m_taskInstanceId.toString(sIndent+"       "));
        }
        if (m_rootTaskInstanceId != null) {
            sb.append(sIndent+"-> rootTaskInstanceId:\n"+sIndent+"       "+m_rootTaskInstanceId.toStringShort()+"\n");
            // sb.append(sIndent+"-> rootTaskInstanceId:\n"+m_rootTaskInstanceId.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

