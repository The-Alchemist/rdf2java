package dfki.frodo.wwf.rdfs2class.task;

import java.util.*;


public class Task
    extends ExecutableObject
{
    //------------------------------------------------------------------------------
    Condition m_preCondition;

    public void putPreCondition (Condition p_preCondition)
    {
        m_preCondition = p_preCondition;
    }

    public Condition getPreCondition ()
    {
        return m_preCondition;
    }

    //------------------------------------------------------------------------------
    Collection m_subTask = new HashSet();

    public void putSubTask (ExecutableObject p_subTask)
    {
        m_subTask.add(p_subTask);
    }

    public Collection getSubTask ()
    {
        return m_subTask;
    }

    //------------------------------------------------------------------------------
    Collection m_needsInfo = new HashSet();

    public void putNeedsInfo (dfki.frodo.wwf.rdfs2class.organisationalmodel.InformationNeed p_needsInfo)
    {
        m_needsInfo.add(p_needsInfo);
    }

    public Collection getNeedsInfo ()
    {
        return m_needsInfo;
    }

    //------------------------------------------------------------------------------
    Collection m_describedBy = new HashSet();

    public void putDescribedBy (dfki.frodo.wwf.rdfs2class.taskconcept.TaskConcept p_describedBy)
    {
        m_describedBy.add(p_describedBy);
    }

    public Collection getDescribedBy ()
    {
        return m_describedBy;
    }

    //------------------------------------------------------------------------------
    Collection m_needsSkill = new HashSet();

    public void putNeedsSkill (dfki.frodo.wwf.rdfs2class.organisationalmodel.Skill p_needsSkill)
    {
        m_needsSkill.add(p_needsSkill);
    }

    public Collection getNeedsSkill ()
    {
        return m_needsSkill;
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
    Condition m_postCondition;

    public void putPostCondition (Condition p_postCondition)
    {
        m_postCondition = p_postCondition;
    }

    public Condition getPostCondition ()
    {
        return m_postCondition;
    }

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
    dfki.frodo.wwf.rdfs2class.organisationalmodel.ProcessRole m_performedBy;

    public void putPerformedBy (dfki.frodo.wwf.rdfs2class.organisationalmodel.ProcessRole p_performedBy)
    {
        m_performedBy = p_performedBy;
    }

    public dfki.frodo.wwf.rdfs2class.organisationalmodel.ProcessRole getPerformedBy ()
    {
        return m_performedBy;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_preCondition != null) {
            sb.append(sIndent+"-> preCondition:\n"+sIndent+"       "+m_preCondition.toStringShort()+"\n");
            // sb.append(sIndent+"-> preCondition:\n"+m_preCondition.toString(sIndent+"       "));
        }
        if (!m_subTask.isEmpty()) {
            sb.append(sIndent+"-> subTask:\n");
            Iterator it_subTask = m_subTask.iterator();
            while (it_subTask.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_subTask.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_subTask.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_needsInfo.isEmpty()) {
            sb.append(sIndent+"-> needsInfo:\n");
            Iterator it_needsInfo = m_needsInfo.iterator();
            while (it_needsInfo.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_needsInfo.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_needsInfo.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_describedBy.isEmpty()) {
            sb.append(sIndent+"-> describedBy:\n");
            Iterator it_describedBy = m_describedBy.iterator();
            while (it_describedBy.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_describedBy.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_describedBy.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_needsSkill.isEmpty()) {
            sb.append(sIndent+"-> needsSkill:\n");
            Iterator it_needsSkill = m_needsSkill.iterator();
            while (it_needsSkill.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_needsSkill.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_needsSkill.next()).toString(sIndent+"       ") );
            }
        }
        if (m_name != null) {
            sb.append(sIndent+"-> name: "+m_name+"\n");
        }
        if (m_postCondition != null) {
            sb.append(sIndent+"-> postCondition:\n"+sIndent+"       "+m_postCondition.toStringShort()+"\n");
            // sb.append(sIndent+"-> postCondition:\n"+m_postCondition.toString(sIndent+"       "));
        }
        if (m_informalDescription != null) {
            sb.append(sIndent+"-> informalDescription: "+m_informalDescription+"\n");
        }
        if (m_performedBy != null) {
            sb.append(sIndent+"-> performedBy:\n"+sIndent+"       "+m_performedBy.toStringShort()+"\n");
            // sb.append(sIndent+"-> performedBy:\n"+m_performedBy.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

