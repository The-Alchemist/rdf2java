package dfki.frodo.wwf.rdfs2class.audit;

import java.util.*;


public class AuditTaskModel
    extends BasicAuditObject
{
    //------------------------------------------------------------------------------
    dfki.rdf.util.THING m_rootTaskModelId;

    public void putRootTaskModelId (dfki.rdf.util.THING p_rootTaskModelId)
    {
        m_rootTaskModelId = p_rootTaskModelId;
    }

    public dfki.rdf.util.THING getRootTaskModelId ()
    {
        return m_rootTaskModelId;
    }

    //------------------------------------------------------------------------------
    dfki.frodo.wwf.rdfs2class.task.TaskModel m_changedTaskModel;

    public void putChangedTaskModel (dfki.frodo.wwf.rdfs2class.task.TaskModel p_changedTaskModel)
    {
        m_changedTaskModel = p_changedTaskModel;
    }

    public dfki.frodo.wwf.rdfs2class.task.TaskModel getChangedTaskModel ()
    {
        return m_changedTaskModel;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_rootTaskModelId != null) {
            sb.append(sIndent+"-> rootTaskModelId:\n"+sIndent+"       "+m_rootTaskModelId.toStringShort()+"\n");
            // sb.append(sIndent+"-> rootTaskModelId:\n"+m_rootTaskModelId.toString(sIndent+"       "));
        }
        if (m_changedTaskModel != null) {
            sb.append(sIndent+"-> changedTaskModel:\n"+sIndent+"       "+m_changedTaskModel.toStringShort()+"\n");
            // sb.append(sIndent+"-> changedTaskModel:\n"+m_changedTaskModel.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

